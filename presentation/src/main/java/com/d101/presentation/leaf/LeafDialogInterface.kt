package com.d101.presentation.leaf

import androidx.fragment.app.DialogFragment

interface LeafDialogInterface {
    companion object {
        lateinit var dialog: DialogFragment
        var dialogShowState = false
    }
}
