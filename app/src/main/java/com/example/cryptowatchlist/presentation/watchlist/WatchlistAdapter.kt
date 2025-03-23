package com.example.cryptowatchlist.presentation.watchlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowatchlist.databinding.ListItemWatchlistCoinBinding
import com.example.cryptowatchlist.domain.model.Coin
import java.util.Locale

class WatchlistAdapter(
    private val onClickListener: OnClickListener,
) : ListAdapter<Coin, WatchlistAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ListItemWatchlistCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
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

    inner class ViewHolder(
        private val binding: ListItemWatchlistCoinBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(coin: Coin) {
            binding.apply {
                name.text =
                    buildString {
                        append(coin.rank)
                        append(". ")
                        append(coin.name)
                    }
                coin.volumeUsd24Hr?.toDoubleOrNull()?.let {
                    volume.text =
                        buildString {
                            append("Vol(24h) $")
                            append(it.let(::formatNumber))
                        }
                    volume.visibility = View.VISIBLE
                }
                price.text =
                    buildString {
                        append("$")
                        append(coin.priceUsd?.toDoubleOrNull()?.let(::formatNumber))
                    }
                coin.changePercent24Hr?.toDoubleOrNull()?.let {
                    percentage.text =
                        buildString {
                            append(it.round(2))
                            append("%")
                        }
                }

                root.setOnClickListener { onClickListener.onClick(coin) }

                watchlistButton.setOnClickListener {
                    onClickListener.onDeleteFromWatchlistClick(coin)
                }
            }
        }
    }

    class OnClickListener(
        val clickListener: (Coin) -> Unit,
        val deleteFromWatchlistClickListener: (Coin) -> Unit,
    ) {
        fun onClick(coin: Coin) = clickListener(coin)

        fun onDeleteFromWatchlistClick(coin: Coin) = deleteFromWatchlistClickListener(coin)
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
