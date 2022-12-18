package com.github.khoben.dzen_onboarding.ui.screen.onboarding

import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dboy.chips.ChipsLayoutManager
import com.github.khoben.dzen_onboarding.databinding.OnboardingFragmentLayoutBinding
import com.github.khoben.dzen_onboarding.sl.diContainer
import com.github.khoben.dzen_onboarding.ui.screen.base.BaseFragment
import com.github.khoben.dzen_onboarding.ui.screen.onboarding.adapter.CategoriesListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OnBoardingFragment : BaseFragment<OnboardingFragmentLayoutBinding>() {

    private val viewModel by viewModels<OnBoardingViewModel> {
        OnBoardingViewModel.Factory(fetchCategoriesUseCase = diContainer.fetchCategoriesUseCase)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = OnboardingFragmentLayoutBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesAdapter = CategoriesListAdapter(
            onItemClicked = viewModel::onCategoryClicked
        )

        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        with(binding.categories) {
            layoutManager = ChipsLayoutManager.newBuilder(context)
                .setOrientation(
                    if (isPortrait) {
                        ChipsLayoutManager.HORIZONTAL
                    } else {
                        ChipsLayoutManager.VERTICAL
                    }
                )
                .apply {
                    if (isPortrait) {
                        setMaxViewsInRow(MAX_ITEMS_IN_ROW_PORTRAIT)
                    }
                }
                .setScrollingEnabled(true)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .setChildGravity(Gravity.LEFT)
                .build()
            adapter = categoriesAdapter
        }

        binding.laterButton.setOnClickListener { viewModel.onLaterClicked() }
        binding.continueButton.setOnClickListener { viewModel.onContinueClicked() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collectLatest { uiState ->
                        when (uiState.dataState) {
                            OnBoardingViewState.DataState.Empty -> {
                                // noop
                            }
                            is OnBoardingViewState.DataState.Error -> {
                                // noop
                            }
                            is OnBoardingViewState.DataState.List -> {
                                binding.continueButton.isVisible = uiState.dataState.showContinue
                                categoriesAdapter.submitList(uiState.dataState.data)
                            }
                        }
                    }
                }
                launch {
                    viewModel.uiEffects.collectLatest { uiEffect ->
                        when (uiEffect) {
                            is OnBoardingViewState.UiEffect.ShowToast -> showToast(
                                uiEffect.message,
                                uiEffect.duration
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        val TAG: String = OnBoardingFragment::class.java.simpleName

        private const val MAX_ITEMS_IN_ROW_PORTRAIT = 4
    }
}