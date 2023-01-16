package com.github.khoben.zen_elements.ui.rate

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.khoben.zen_elements.databinding.RateFragmentLayoutBinding
import com.github.khoben.zen_elements.ui.base.BaseFragment

class RateFragment : BaseFragment<RateFragmentLayoutBinding>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        RateFragmentLayoutBinding.inflate(inflater, container, false)


    companion object {
        val TAG: String = RateFragment::class.java.simpleName
    }
}