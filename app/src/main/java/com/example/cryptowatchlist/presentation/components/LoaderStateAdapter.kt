package com.example.cryptowatchlist.presentation.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowatchlist.databinding.LayoutPagingLoaderBinding

class LoaderStateAdapter(
    private val onRetry: (() -> Unit)? = null,
) : LoadStateAdapter<LoaderStateAdapter.LoaderViewHolder>() {
    override fun onBindViewHolder(
        holder: LoaderViewHolder,
        loadState: LoadState,
    ) = holder.bindTo(loadState)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ): LoaderViewHolder {
        val binding =
            LayoutPagingLoaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoaderViewHolder(binding)
    }

    inner class LoaderViewHolder(
        private val binding: LayoutPagingLoaderBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(loadState: LoadState) {
            binding.apply {
                when (loadState) {
                    is LoadState.Loading -> motionLayout.transitionToEnd()

                    else -> motionLayout.transitionToStart()
                }

                retryButton.setOnClickListener {
                    onRetry?.invoke()
                }
            }
        }
    }
}
