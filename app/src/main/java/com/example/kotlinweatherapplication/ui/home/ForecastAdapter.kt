package com.example.kotlinweatherapplication.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinweatherapplication.Utils.Formatting.getDate
import com.example.kotlinweatherapplication.Utils.Formatting.getTemp
import com.example.kotlinweatherapplication.databinding.ForecastItemBinding
import com.example.kotlinweatherapplication.networking.openweathermap.forecast_models.Daily

class ForecastAdapter : ListAdapter<Daily, ForecastAdapter.ViewHolder>(differCallback) {

    inner class ViewHolder(private val binding: ForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(daily: Daily) {
            binding.apply{
                tvDate.text = getDate(daily.dt)
                tvTemp.text = getTemp(daily.temp.day)
                Glide.with(itemView)
                    .load("https://openweathermap.org/img/wn/" + daily.weather[0].icon + "@2x.png")
                    .into(binding.ivIcon)
            }
        }
    }

    object differCallback : DiffUtil.ItemCallback<Daily>(){
        override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean = oldItem.dt == newItem.dt
        override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean = oldItem.dt == newItem.dt
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ForecastItemBinding.inflate(
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