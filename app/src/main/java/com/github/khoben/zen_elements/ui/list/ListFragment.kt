package com.github.khoben.zen_elements.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.khoben.zen_elements.R
import com.github.khoben.zen_elements.databinding.ListFragmentLayoutBinding
import com.github.khoben.zen_elements.ui.base.BaseFragment
import com.github.khoben.zen_elements.ui.draggablefill.DraggableFillFragment
import com.github.khoben.zen_elements.ui.fill.FillFragment
import com.github.khoben.zen_elements.ui.match.MatchFragment
import com.github.khoben.zen_elements.ui.poll.PollFragment
import com.github.khoben.zen_elements.ui.rate.RateFragment

class ListFragment : BaseFragment<ListFragmentLayoutBinding>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        ListFragmentLayoutBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fill.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, FillFragment(), FillFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
        binding.draggableFill.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, DraggableFillFragment(), DraggableFillFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
        binding.match.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, MatchFragment(), MatchFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
        binding.poll.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, PollFragment(), PollFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
        binding.rate.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, RateFragment(), RateFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        val TAG: String = ListFragment::class.java.simpleName
    }
}