package com.example.cryptowatchlist.presentation.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cryptowatchlist.databinding.FragmentWatchlistBinding
import com.example.cryptowatchlist.presentation.core.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel

class WatchlistFragment : Fragment() {
    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WatchlistViewModel by viewModel()
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView) { v, insets ->
            val bars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            v.updatePadding(
                left = bars.left,
                right = bars.right,
                bottom = bars.bottom,
            )
            WindowInsetsCompat.CONSUMED
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.frameLayout) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<MarginLayoutParams> {
                leftMargin = insets.left
                topMargin = insets.top + dpToPx(64f, requireContext())
                rightMargin = insets.right
                bottomMargin = insets.bottom
            }
            WindowInsetsCompat.CONSUMED
        }

        val adapter =
            WatchlistAdapter(
                onClickListener =
                    WatchlistAdapter.OnClickListener(
                        clickListener = {
                            // TODO: Navigate to coin details screen
                        },
                        deleteFromWatchlistClickListener = {
                            viewModel.onEvent(WatchlistUiEvent.DeleteCoinFromWatchlist(it))
                        },
                    ),
            )
        binding.recyclerView.adapter = adapter

        viewModel.watchlist.observe(viewLifecycleOwner) { watchlist ->
            if (watchlist.isNotEmpty()) {
                adapter.submitList(watchlist) {
                    binding.progressCircular.visibility = View.GONE
                }
            } else {
                binding.progressCircular.visibility = View.GONE
                binding.emptyText.visibility = View.VISIBLE
            }
        }

        binding.topAppBar.setNavigationOnClickListener {
            navController.navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
