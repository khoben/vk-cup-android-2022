package com.github.khoben.dzen_onboarding.ui.screen.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.github.khoben.dzen_onboarding.common.PlatformString

abstract class BaseFragment<B : ViewBinding> : Fragment() {
    private var _binding: B? = null
    protected val binding get() = _binding!!

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return _binding?.root
    }

    fun showToast(@StringRes messageResId: Int, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), getString(messageResId), duration).show()
    }

    fun showToast(message: PlatformString, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message.string(requireContext()), duration).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}