package com.example.cryptowatchlist.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.example.cryptowatchlist.R
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

        val adapter = initRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    when (loadStates.refresh) {
                        is LoadState.Loading -> {
                            binding.apply {
                                progressCircular.isVisible = adapter.itemCount == 0
                                errorView.visibility = View.GONE
                                recyclerView.visibility = View.GONE
                            }
                        }

                        is LoadState.Error -> {
                            binding.apply {
                                progressCircular.visibility = View.GONE
                                errorView.apply {
                                    val error = (loadStates.refresh as LoadState.Error).error
                                    errorText.text = error.localizedMessage
                                    retryButton.visibility = View.VISIBLE
                                    root.visibility = View.VISIBLE

                                    retryButton.setOnClickListener {
                                        adapter.refresh()
                                    }
                                }
                            }
                        }

                        is LoadState.NotLoading -> {
                            binding.apply {
                                progressCircular.visibility = View.GONE
                                if (adapter.itemCount == 0) {
                                    recyclerView.visibility = View.GONE
                                    errorText.text = getString(R.string.no_coins_found)
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

        viewModel.coins.observe(viewLifecycleOwner) { items ->
            adapter.submitData(viewLifecycleOwner.lifecycle, items)
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
        binding.recyclerView.adapter = concatAdapter
        return homeAdapter
    }
}
