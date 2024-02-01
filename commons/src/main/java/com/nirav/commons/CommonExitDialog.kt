package com.nirav.commons

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.nirav.commons.databinding.DialogExitBinding

object CommonExitDialog {

    private var dialog: Dialog? = null

    fun init(activity: Activity) {
        dialog = Dialog(activity)
        val binding = DialogExitBinding.inflate(LayoutInflater.from(activity))
        dialog?.setContentView(binding.root)
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 40)
        dialog?.window?.setBackgroundDrawable(inset)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog?.setCancelable(false)
        binding.btnNo.setOnClickListener {
            dialog?.dismiss()
        }
        binding.btnYes.setOnClickListener {
            dialog?.dismiss()
            activity.finish()
        }
    }

    fun show(activity: Activity) {
        if (dialog == null) {
            activity.finish()
        } else {
            dialog?.show()
        }
    }

}