package com.d101.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.d101.presentation.databinding.BottomSheetPermissionCheckBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PermissionCheckBottomSheetDialog(
    private val cancelDialog: (Boolean) -> Unit,
    private val permissionConfirm: () -> Unit,
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetPermissionCheckBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetPermissionCheckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.confirmButton.setOnClickListener {
            permissionConfirm()
        }
        binding.cancelButton.setOnClickListener {
            cancelDialog(binding.neverShowAgainCheckBox.isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
