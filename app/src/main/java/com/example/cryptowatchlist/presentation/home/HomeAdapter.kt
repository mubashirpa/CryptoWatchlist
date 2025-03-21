package com.example.cryptowatchlist.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowatchlist.databinding.ListItemCoinBinding
import com.example.cryptowatchlist.domain.model.Coin

class HomeAdapter(
    private val onClickListener: OnClickListener,
) : PagingDataAdapter<Coin, HomeAdapter.CoinViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CoinViewHolder {
        val binding =
            ListItemCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CoinViewHolder,
        position: Int,
    ) {
        getItem(position)?.let(holder::bindTo)
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<Coin>() {
                override fun areItemsTheSame(
                    oldItem: Coin,
                    newItem: Coin,
                ): Boolean = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: Coin,
                    newItem: Coin,
                ): Boolean = oldItem == newItem
            }
    }

    inner class CoinViewHolder(
        private val binding: ListItemCoinBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(coin: Coin) {
            binding.apply {
                headline.text = coin.name
                supportingText.text = coin.symbol
                trailingText.text = coin.priceUsd?.toIntOrNull()?.toString()

                root.setOnClickListener { onClickListener.onClick(coin) }
            }
        }
    }

    class OnClickListener(
        val clickListener: (Coin) -> Unit,
    ) {
        fun onClick(coin: Coin) = clickListener(coin)
    }
}
