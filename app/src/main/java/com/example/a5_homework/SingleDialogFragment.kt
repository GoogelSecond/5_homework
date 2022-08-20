package com.example.a5_homework

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.example.a5_homework.databinding.SingleDialogFragmentBinding

class SingleDialogFragment : DialogFragment() {

    private var _binding: SingleDialogFragmentBinding? = null
    private val binding: SingleDialogFragmentBinding
        get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = SingleDialogFragmentBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext()).setView(binding.root).create()

        dialog.setOnShowListener {
            binding.buttonPositiveDialog.setOnClickListener {
                parentFragmentManager.setFragmentResult(
                    DEFAULT_REQUEST_KEY,
                    bundleOf(KEY_ANSWER_RESPONSE to true)
                )
                dismiss()
            }
            binding.buttonNegativeDialog.setOnClickListener {
                parentFragmentManager.setFragmentResult(
                    DEFAULT_REQUEST_KEY,
                    bundleOf(KEY_ANSWER_RESPONSE to false)
                )
                dismiss()
            }
        }
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = SingleDialogFragment::class.java.simpleName

        private const val KEY_ANSWER_RESPONSE = "KEY_ANSWER_RESPONSE"

        private val DEFAULT_REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(manager: FragmentManager) {
            val dialogFragment = SingleDialogFragment()
            dialogFragment.show(manager, TAG)
        }

        fun setupListener(
            manager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: (answer: Boolean) -> Unit
        ) {
            manager.setFragmentResultListener(DEFAULT_REQUEST_KEY, lifecycleOwner) { _, result ->
                listener.invoke(result.getBoolean(KEY_ANSWER_RESPONSE))
            }
        }
    }
}