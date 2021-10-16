package com.example.kotlinweatherapplication.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapplication.databinding.CityItemBinding
import com.example.kotlinweatherapplication.networking.vk.cities_models.Item

class CitiesAdapter(private val listener: CitiesOnItemClickListener?) : ListAdapter<Item, CitiesAdapter.ViewHolder>(differCallback) {

    inner class ViewHolder(private val binding: CityItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(city: Item) {
            Log.d("CitiesAdapter", "Binding "+city.title)
            binding.apply{
                tvCity.text = city.title
                root.setOnClickListener { view ->
                    listener?.onItemClick(city)
                }
            }
        }
    }

    object differCallback : DiffUtil.ItemCallback<Item>(){
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem.id == newItem.id
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CityItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let{
            holder.bind(it)
        }
    }

}