package com.example.cryptowatchlist.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowatchlist.databinding.ListItemCoinBinding
import com.example.cryptowatchlist.domain.model.Coin
import java.util.Locale

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
                name.text =
                    buildString {
                        append(coin.rank)
                        append(". ")
                        append(coin.name)
                    }
                volume.text =
                    buildString {
                        append("Vol(24h) $")
                        coin.volumeUsd24Hr?.toDoubleOrNull()
                        append(coin.volumeUsd24Hr?.toDoubleOrNull()?.let(::formatNumber))
                    }
                price.text =
                    buildString {
                        append("$")
                        append(coin.priceUsd?.toDoubleOrNull()?.let(::formatNumber))
                    }
                percentage.text =
                    buildString {
                        append(coin.changePercent24Hr?.toDoubleOrNull()?.round(2))
                        append("%")
                    }

                root.setOnClickListener { onClickListener.onClick(coin) }
            }
        }
    }

    class OnClickListener(
        val clickListener: (Coin) -> Unit,
    ) {
        fun onClick(coin: Coin) = clickListener(coin)
    }

    fun Double.round(decimals: Int = 2) = "%.${decimals}f".format(this)

    fun formatNumber(value: Double): String =
        when {
            value >= 1_000_000_000_000 -> {
                String.format(Locale.getDefault(), "%.2fT", value / 1_000_000_000_000.0)
            }

            value >= 1_000_000_000 -> {
                String.format(Locale.getDefault(), "%.2fB", value / 1_000_000_000.0)
            }

            value >= 1_000_000 -> String.format(Locale.getDefault(), "%.2fM", value / 1_000_000.0)
            value >= 1_000 -> String.format(Locale.getDefault(), "%.2fK", value / 1_000.0)
            else -> value.round(2)
        }
}
