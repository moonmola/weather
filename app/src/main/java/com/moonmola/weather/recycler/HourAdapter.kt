package com.moonmola.weather.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moonmola.weather.BuildConfig
import com.moonmola.weather.data.HourWeather
import com.moonmola.weather.databinding.ListviewHourBinding
import com.moonmola.weather.di.GlideApp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class HourAdapter @Inject constructor(): RecyclerView.Adapter<HourAdapter.VH>() {
    var itemList: List<HourWeather> = ArrayList()

    inner class VH(private val binding: ListviewHourBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hourWeather: HourWeather) {
            val timeInDate = Date(hourWeather.dt.toLong()*1000);
            val timeFormat = SimpleDateFormat("a h시", Locale.KOREA)
            binding.hourTime.text = timeFormat.format(timeInDate)
            binding.hourTemp.text = (hourWeather.temp -273.15).roundToInt().toString() + "°"
            if ( hourWeather.pop >= 0.4) {
                binding.hourPop.visibility = View.VISIBLE
                binding.hourPop.text = "${(hourWeather.pop*100).roundToInt()}%"
            } else {
                binding.hourPop.visibility = View.GONE
            }
            GlideApp.with(binding.root)
                .load(BuildConfig.IMAGE_URL + hourWeather.weather[0].icon+".png")
                .into(binding.hourIcon)
            Log.e("?Dabong", BuildConfig.IMAGE_URL + hourWeather.weather[0].icon+".png")
            binding.hourIcon
        }

        fun setClickListener(id: Int) {
            binding.root.setOnClickListener {
//                val directions = ListFragmentDirections.actionListFragmentToDetailPagerFragment(id)
//                val controller = Navigation.findNavController(it)
//                controller.navigate(directions)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ListviewHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val photoItem: HourWeather = itemList[position]
        holder.bind(photoItem)
//        holder.setClickListener(photoItem.id)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}