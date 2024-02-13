package com.d101.presentation.main.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.d101.presentation.databinding.BottomSheetPermissionCheckBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PermissionCheckBottomSheetDialog : BottomSheetDialogFragment() {

    private var _binding: BottomSheetPermissionCheckBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetPermissionCheckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.btnRequestPermission.setOnClickListener {
//            // 권한 요청 로직
//            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
