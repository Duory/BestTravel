package makov.besttravel.search.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import makov.besttravel.databinding.ItemCityBinding
import makov.besttravel.search.domain.model.Airport

class AirportRecyclerViewAdapter(
    private val onItemClicked: (Airport) -> Unit
) : ListAdapter<Airport, AirportRecyclerViewAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Airport>() {
        override fun areItemsTheSame(oldItem: Airport, newItem: Airport): Boolean {
            return oldItem.iata == oldItem.iata
        }

        override fun areContentsTheSame(oldItem: Airport, newItem: Airport) = oldItem == newItem
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemCityBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(airport: Airport) {
            binding.root.setOnClickListener { onItemClicked(airport) }
            binding.name.text = airport.city
            binding.iata.text = airport.iata
        }
    }
}