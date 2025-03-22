package com.example.cryptowatchlist.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowatchlist.R
import com.example.cryptowatchlist.databinding.ListItemSearchCoinBinding
import com.example.cryptowatchlist.domain.model.Coin
import java.util.Locale

class SearchAdapter(
    private val context: Context,
    private val onClickListener: OnClickListener,
) : ListAdapter<Coin, SearchAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ListItemSearchCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        private val binding: ListItemSearchCoinBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(coin: Coin) {
            binding.apply {
                name.text =
                    buildString {
                        append(coin.rank)
                        append(". ")
                        append(coin.name)
                    }
                symbol.text = coin.symbol
                price.text =
                    buildString {
                        append("$")
                        append(coin.priceUsd?.toDoubleOrNull()?.let(::formatNumber))
                    }
                coin.changePercent24Hr?.toDoubleOrNull()?.let {
                    if (it < 0) {
                        percentage.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.red,
                            ),
                        )
                    } else {
                        percentage.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.green,
                            ),
                        )
                    }
                    percentage.text =
                        buildString {
                            append(it.round(2))
                            append("%")
                        }
                }

                root.setOnClickListener { onClickListener.onClick(coin) }

                watchlistButton.setOnClickListener {
                    watchlistButton.isSelected = !watchlistButton.isSelected
                }
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
