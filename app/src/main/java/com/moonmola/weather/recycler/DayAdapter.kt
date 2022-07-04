package com.moonmola.weather.recycler

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moonmola.weather.BuildConfig
import com.moonmola.weather.data.DayWeather
import com.moonmola.weather.data.HourWeather
import com.moonmola.weather.databinding.ListviewDayBinding
import com.moonmola.weather.databinding.ListviewHourBinding
import com.moonmola.weather.di.GlideApp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class DayAdapter @Inject constructor(): RecyclerView.Adapter<DayAdapter.VH>() {
    var itemList: List<DayWeather> = ArrayList()
    var min = 100
    var max = -100

    inner class VH(private val binding: ListviewDayBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(dayWeather: DayWeather) {
            val timeInDate = Date(dayWeather.dt.toLong()*1000)
            val timeFormat = SimpleDateFormat("E", Locale.KOREA)
            val curMin = (dayWeather.temp.min -273.15).roundToInt()
            val curMax = (dayWeather.temp.max -273.15).roundToInt()
            binding.dayOfWeek.text = timeFormat.format(timeInDate)
            binding.dayTempMin.text = "${(dayWeather.temp.min -273.15).roundToInt()}°"
            binding.dayTempMax.text = "${(dayWeather.temp.max -273.15).roundToInt()}°"
            if ( dayWeather.pop >= 0.4) {
                binding.dayPop.visibility = View.VISIBLE
                binding.dayPop.text = "${(dayWeather.pop*100).roundToInt()}%"
            } else {
                binding.dayPop.visibility = View.GONE
            }

            val unit = binding.dayTempBarBackground.layoutParams.width/(max-min)
            binding.dayTempBarColor.layoutParams.width = unit*(curMax - curMin)
            (binding.dayTempBarColor.layoutParams as ViewGroup.MarginLayoutParams).marginStart = unit*(curMin-min)

            GlideApp.with(binding.root)
                .load(BuildConfig.IMAGE_URL + dayWeather.weather[0].icon+".png")
                .into(binding.dayIcon)
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
        val binding = ListviewDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val photoItem: DayWeather = itemList[position]
        holder.bind(photoItem)
//        holder.setClickListener(photoItem.id)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}