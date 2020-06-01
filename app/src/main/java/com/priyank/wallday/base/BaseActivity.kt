package com.priyank.wallday.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import com.priyank.wallday.R
import com.priyank.wallday.custom.showToast


abstract class BaseActivity : AppCompatActivity() {

    private var progressDialog: Dialog? = null

    fun showProgressDialog() {
        if (progressDialog == null || !progressDialog?.isShowing!!) {
            progressDialog = Dialog(this)
            progressDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog?.setContentView(R.layout.custom_progressbar)
            progressDialog?.setCancelable(false)
            progressDialog?.show()
        }
    }

    fun hideProgressDialog() {
        try {
            if (progressDialog != null && progressDialog?.isShowing!!) {
                progressDialog?.dismiss()
            }
        } catch (throwable: Throwable) {

        } finally {
            progressDialog?.dismiss()
        }
    }

    fun <T> manageAPIResource(resource: APIResource<T>, successListener: (T, String) -> Unit) {
        when (resource.status) {
            Status.LOADING -> {
                showProgressDialog()
            }
            Status.ERROR -> {
                hideProgressDialog()
                resource.message?.let {
                    showToast(it)
                }
            }
            Status.NO_NETWORK -> {
                hideProgressDialog()
                showToast(R.string.no_network_available)
            }
            else -> {
                hideProgressDialog()
                successListener.invoke(resource.data!!, resource.message!!)
            }
        }
    }
}