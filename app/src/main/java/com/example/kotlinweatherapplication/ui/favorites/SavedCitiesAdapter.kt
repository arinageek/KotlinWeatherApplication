package com.example.kotlinweatherapplication.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinweatherapplication.Utils.Formatting.getTemp
import com.example.kotlinweatherapplication.databinding.SavedCityItemBinding
import com.example.kotlinweatherapplication.networking.openweathermap.current_weather_models.CurrentWeatherResponse

class SavedCitiesAdapter : ListAdapter<CurrentWeatherResponse, SavedCitiesAdapter.ViewHolder>(
    differCallback
) {

    inner class ViewHolder(private val binding: SavedCityItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(weather: CurrentWeatherResponse) {
            binding.apply{
                tvCity.text = weather.name
                tvDegrees.text = getTemp(weather.main.temp)
                Glide.with(itemView)
                    .load("https://openweathermap.org/img/wn/" + weather.weather[0].icon + "@2x.png")
                    .into(ivIcon)
            }
        }
    }

    object differCallback : DiffUtil.ItemCallback<CurrentWeatherResponse>(){
        override fun areItemsTheSame(oldItem: CurrentWeatherResponse, newItem: CurrentWeatherResponse): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: CurrentWeatherResponse, newItem: CurrentWeatherResponse): Boolean = oldItem.id == newItem.id
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SavedCityItemBinding.inflate(
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