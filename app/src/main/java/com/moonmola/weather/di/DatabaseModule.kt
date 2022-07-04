package com.moonmola.weather.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.moonmola.weather.data.db.DayWeatherTypeConverter
import com.moonmola.weather.data.db.HourWeatherListTypeConverter
import com.moonmola.weather.data.db.HourWeatherTypeConverter
import com.moonmola.weather.data.db.WeatherDAO
import com.moonmola.weather.data.db.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private const val DB_NAME = "weather.db"

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context, gson: Gson): WeatherDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java,
            DB_NAME
        ).addTypeConverter(HourWeatherListTypeConverter(gson))
            .addTypeConverter(DayWeatherTypeConverter(gson))
            .addTypeConverter(HourWeatherTypeConverter(gson))
            .build()
    }

    @Provides
    fun providePlantDao(appDatabase: WeatherDatabase): WeatherDAO {
        return appDatabase.getWeatherDAO()
    }

}