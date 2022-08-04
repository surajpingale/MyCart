package com.example.mycart.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.dishapp.interfaces.CustomToolbar
import com.example.mycart.R
import com.example.mycart.application.MyCartApplication
import com.example.mycart.databinding.FragmentProfileBinding
import com.example.mycart.utils.Constants
import com.example.mycart.utils.CustomDialog
import com.example.mycart.utils.Permissions
import com.example.mycart.utils.Response
import com.example.mycart.viewmodel.AddProductViewModel
import com.example.mycart.viewmodel.AddProductViewModelFactory
import com.example.mycart.viewmodel.UserDetailViewModel
import com.example.mycart.viewmodel.UserViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null

    private val binding: FragmentProfileBinding
        get() = _binding!!

    private val productViewModel: AddProductViewModel by viewModels {
        AddProductViewModelFactory(
            (requireActivity().application as MyCartApplication).fireStoreRepository
        )
    }

    private val userDetailViewModel: UserDetailViewModel by viewModels {
        UserViewModelFactory(
            (requireActivity().application as
                    MyCartApplication).fireStoreRepository
        )
    }
    private var gender = Constants.MALE

    private var imageUrl: String = ""

    private val getImageFromGallery = registerForActivityResult(
        ActivityResultContracts
            .GetContent()
    ) { uri ->

        if (uri != null) {
            productViewModel.uploadImageToCloud(
                context!!, uri
            )
            productViewModel.imageLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is Response.Success -> {
                        imageUrl = it.data!!
                        Glide.with(requireActivity())
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_user_placeholder)
                            .into(binding.include.ivProfile)
                    }
                    is Response.Error -> {
                        Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private val permissionsResult = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        permissionsMap.entries.forEach {
            val permissionName = it.key
            val isGranted = it.value

            if (permissionName == Constants.permissionsArray[0]) {
                if (!isGranted) {
                    Permissions.requestPermissions(
                        requireActivity(),
                        Constants.permissionsArray, Constants.PERMISSION_REQUEST_CODE
                    )
                } else {
                    getImageFromGallery.launch("image/*")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        setUpToolbar()
        initViews()

        return binding.root
    }

    private fun setUpToolbar() {
        activity?.let {
            (activity as CustomToolbar).onToolbarLoad(
                Constants.FRAGMENT_PROFILE,
                "Profile"
            )
        }
    }

    private fun initViews() {

        binding.include.ivProfile.setOnClickListener(this)
        binding.include.btnUpdateProfile.setOnClickListener(this)
        binding.include.tvGenderMale.setOnClickListener(this)
        binding.include.tvGenderFemale.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        val include = binding.include

        when (view!!.id) {
            include.ivProfile.id -> {
                if (Permissions.checkStoragePermission(requireActivity())) {
                    getImageFromGallery.launch("image/*")
                } else {
                    Permissions.requestStoragePermission(requireActivity())
                }
            }

            include.tvGenderMale.id -> {
                setGenderMale()
            }

            include.tvGenderFemale.id -> {
                setGenderFemale()
            }

            include.btnUpdateProfile.id -> {
                if (validateEntries()) {
                    CustomDialog.showDialog(requireActivity())
                    updateUser()
                }
            }
        }
    }

    private fun updateUser() {

        val include = binding.include

        val mobileNo = include.etTvProfileMobileNo.editText.text.toString().trim { it <= ' ' }

        val userHashMap = HashMap<String, Any>()

        if (mobileNo.isNotEmpty()) {
            userHashMap[Constants.MOBILE] = mobileNo.toLong()
        }

        userHashMap[Constants.GENDER] = gender

        if (imageUrl != null) {
            userHashMap[Constants.IMAGE] = imageUrl!!
        }

        userDetailViewModel.updateUserProfile(userHashMap)
        userDetailViewModel.updateUserProfileLiveData.observe(viewLifecycleOwner) { state ->

            when (state) {
                is Response.Success -> {
                    CustomDialog.hideDialog()
                    CustomDialog.showToast(requireActivity(), "Updated successfully")
                }
                is Response.Error -> {
                    CustomDialog.hideDialog()
                    CustomDialog.showToast(requireActivity(), "Something wrong..")
                }
            }

        }

    }

    private fun validateEntries(): Boolean {
        val include = binding.include

        val mobileNo = include.etTvProfileMobileNo.editText.text.toString().trim { it <= ' ' }

        if (mobileNo.isEmpty()) {
            include.etTvProfileMobileNo.errorText.visibility = View.VISIBLE
            include.etTvProfileMobileNo.errorText.text =
                resources.getString(R.string.error_msg_mobile_no)
            return false
        } else {
            include.etTvProfileMobileNo.errorText.visibility = View.INVISIBLE
        }

        return true
    }

    private fun setGenderFemale() {
        gender = Constants.FEMALE

        binding.include.tvGenderMale.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.grey_gender_color
            )
        )

        binding.include.tvGenderFemale.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.white
            )
        )

        binding.include.tvGenderFemale.background =
            ContextCompat.getDrawable(requireActivity(), R.drawable.bg_gender_pressed)
        binding.include.tvGenderMale.background =
            ContextCompat.getDrawable(requireActivity(), R.drawable.bg_gender_not_pressed)
    }

    private fun setGenderMale() {
        gender = Constants.MALE

        binding.include.tvGenderMale.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.white
            )
        )

        binding.include.tvGenderFemale.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.grey_gender_color
            )
        )

        binding.include.tvGenderMale.background =
            ContextCompat.getDrawable(requireActivity(), R.drawable.bg_gender_pressed)
        binding.include.tvGenderFemale.background =
            ContextCompat.getDrawable(requireActivity(), R.drawable.bg_gender_not_pressed)
    }

    override fun onDestroyView() {
        super.onDestroy()
        _binding = null
    }

}