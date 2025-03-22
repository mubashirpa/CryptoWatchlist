package com.example.cryptowatchlist.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.example.cryptowatchlist.R
import com.example.cryptowatchlist.core.Result
import com.example.cryptowatchlist.databinding.FragmentHomeBinding
import com.example.cryptowatchlist.presentation.components.LoaderStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.frameLayout) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<MarginLayoutParams> {
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
            }
            WindowInsetsCompat.CONSUMED
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.searchFrameLayout) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<MarginLayoutParams> {
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
            }
            WindowInsetsCompat.CONSUMED
        }

        val adapter = initRecyclerView()

        viewModel.coins.observe(viewLifecycleOwner) { items ->
            adapter.submitData(viewLifecycleOwner.lifecycle, items)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    binding.apply {
                        when (val refresh = loadStates.refresh) {
                            is LoadState.Loading -> {
                                errorView.visibility = View.GONE
                                recyclerView.visibility = View.GONE
                                progressCircular.isVisible = adapter.itemCount == 0
                            }

                            is LoadState.Error -> {
                                progressCircular.visibility = View.GONE
                                errorText.text = refresh.error.localizedMessage
                                retryButton.visibility = View.VISIBLE
                                errorView.visibility = View.VISIBLE

                                retryButton.setOnClickListener {
                                    adapter.refresh()
                                }
                            }

                            is LoadState.NotLoading -> {
                                progressCircular.visibility = View.GONE
                                if (adapter.itemCount == 0) {
                                    recyclerView.visibility = View.GONE
                                    errorText.text = getString(R.string.no_coins_found)
                                    retryButton.visibility = View.GONE
                                    errorView.visibility = View.VISIBLE
                                } else {
                                    errorView.visibility = View.GONE
                                    recyclerView.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
            }
        }

        val searchAdapter = initSearchRecyclerView()

        viewModel.coinsResult.observe(viewLifecycleOwner) { result ->
            binding.apply {
                when (result) {
                    is Result.Empty -> {
                        searchProgressCircular.visibility = View.GONE
                        searchRecyclerView.visibility = View.GONE
                        searchErrorView.visibility = View.GONE
                    }

                    is Result.Error -> {
                        searchProgressCircular.visibility = View.GONE
                        searchRecyclerView.visibility = View.GONE

                        searchErrorText.text = result.message!!.asString(requireContext())
                        searchRetryButton.visibility = View.VISIBLE
                        searchErrorView.visibility = View.VISIBLE

                        searchRetryButton.setOnClickListener {
                            viewModel.onEvent(
                                HomeUiEvent.Search(
                                    query = binding.searchView.text.toString(),
                                ),
                            )
                        }
                    }

                    is Result.Loading -> {
                        searchErrorView.visibility = View.GONE
                        searchRecyclerView.visibility = View.GONE
                        searchProgressCircular.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        val coins = result.data
                        if (coins.isNullOrEmpty()) {
                            searchProgressCircular.visibility = View.GONE
                            searchErrorText.text = getString(R.string.no_coins_found)
                            searchRetryButton.visibility = View.GONE
                            searchErrorView.visibility = View.VISIBLE
                        } else {
                            searchAdapter.submitList(result.data) {
                                searchProgressCircular.visibility = View.GONE
                                searchRecyclerView.visibility = View.VISIBLE
                                searchRecyclerView.smoothScrollToPosition(0)
                            }
                        }
                    }
                }
            }
        }

        binding.searchView.editText.doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (it.isBlank()) {
                    // Clear current items in the adapter
                    searchAdapter.submitList(emptyList())
                }
                viewModel.onEvent(
                    HomeUiEvent.Search(
                        query = it.toString(),
                        delay = 500,
                    ),
                )
            }
        }

        binding.searchView
            .getEditText()
            .setOnEditorActionListener { v, actionId, event ->
                false
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView(): HomeAdapter {
        val homeAdapter =
            HomeAdapter(
                onClickListener =
                    HomeAdapter.OnClickListener {
                        // TODO: Implement navigation
                    },
            )
        val loaderStateAdapter =
            LoaderStateAdapter {
                homeAdapter.retry()
            }
        val concatAdapter = homeAdapter.withLoadStateFooter(loaderStateAdapter)

        binding.recyclerView.apply {
            ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
                val bars =
                    insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
                v.updatePadding(
                    left = bars.left,
                    right = bars.right,
                    bottom = bars.bottom,
                )
                WindowInsetsCompat.CONSUMED
            }
            adapter = concatAdapter
        }

        return homeAdapter
    }

    private fun initSearchRecyclerView(): SearchAdapter {
        val searchAdapter =
            SearchAdapter(
                context = requireContext(),
                onClickListener =
                    SearchAdapter.OnClickListener {
                        // TODO: Implement navigation
                    },
            )

        binding.searchRecyclerView.apply {
            ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
                val bars =
                    insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
                v.updatePadding(
                    left = bars.left,
                    right = bars.right,
                    bottom = bars.bottom,
                )
                WindowInsetsCompat.CONSUMED
            }
            adapter = searchAdapter
        }

        return searchAdapter
    }
}
