package com.example.mycart.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.mycart.R
import com.example.mycart.databinding.ItemAddressBinding
import com.example.mycart.model.Address

class AddressAdapter(
    private val context: Context,
    private val addressList: List<Address>,
    private val onAddressClick: OnAddressClick
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAddressBinding.inflate(layoutInflater, parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = addressList[position]

        holder.binding.apply {
            tvFullName.text = address.fullName
            tvAddress.text = address.address
            tvMobileNo.text = address.mobileNumber
        }

        holder.itemView.setOnClickListener {
            onAddressClick.onAddressClicked(address)
        }

        holder.itemView.setOnLongClickListener {
            popUpInflater(holder.itemView, address)
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return addressList.size
    }


    inner class AddressViewHolder(itemView: ItemAddressBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: ItemAddressBinding

        init {
            binding = itemView
        }
    }

    interface OnAddressClick {
        fun onAddressClicked(address: Address)
        fun onAddressEditClicked(address: Address)
        fun onAddressDeleteClicked(address: Address)

    }

    private fun popUpInflater(
        ivPopUp: View,
        address: Address
    ) {
        val popupMenu = PopupMenu(context, ivPopUp)
        popupMenu.menuInflater.inflate(R.menu.pop_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->

            if (menuItem.itemId == R.id.menu_edit) {
                onAddressClick.onAddressEditClicked(address)
            } else {
                onAddressClick.onAddressDeleteClicked(address)
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }
}