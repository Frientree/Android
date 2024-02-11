package utils

import android.app.Dialog
import android.content.Context
import androidx.viewbinding.ViewBinding
import com.d101.presentation.R

object DialogUtils {
    private var dialog: Dialog? = null
    fun createAndShowDialog(context: Context, viewBinding: ViewBinding) {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
        dialog = Dialog(context, R.style.Base_FTR_FullScreenDialog).apply {
            window?.setBackgroundDrawableResource(R.drawable.bg_white_radius_30dp)
        }
        dialog?.setContentView(viewBinding.root)
        dialog?.show()
    }
}
