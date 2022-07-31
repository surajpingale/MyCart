package com.example.mycart.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.example.mycart.databinding.DialogPleaseWaitBinding

object CustomDialog {

    private lateinit var dialog: Dialog

    fun showDialog(context: Context) {
        dialog = Dialog(context)
        val layoutInflater = LayoutInflater.from(context)
        val dialogBinding = DialogPleaseWaitBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialog.setCancelable(false)
        dialog.show()
    }

    fun hideDialog() {
        dialog.dismiss()
    }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}