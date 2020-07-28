package makov.besttravel.search.ui.main

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.LayoutRes
import makov.besttravel.search.domain.model.City

class NoFilterCitiesArrayAdapter(
    context: Context,
    @LayoutRes layoutRes: Int
) : ArrayAdapter<City>(context, layoutRes) {

    fun submitData(cities: List<City>) {
        clear()
        addAll(cities)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?) = FilterResults()
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) = Unit
        }
    }
}