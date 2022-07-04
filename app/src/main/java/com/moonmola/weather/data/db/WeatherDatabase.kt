package com.moonmola.weather.data.db

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.moonmola.weather.data.WeatherResponse

@Database(entities = [WeatherResponse::class], version = 1, exportSchema = false)
@TypeConverters(
    value = [
        HourWeatherListTypeConverter::class,
        DayWeatherTypeConverter::class,
        HourWeatherTypeConverter::class
    ]
)
abstract class WeatherDatabase() : RoomDatabase(), Parcelable {

    abstract  fun getWeatherDAO(): WeatherDAO

//    companion object {
//
//        // For Singleton instantiation
//        @Volatile private var instance: WeatherDatabase? = null
//
//        fun getInstance(context: Context): WeatherDatabase {
//            return instance ?: synchronized(this) {
//                instance ?: buildDatabase(context).also { instance = it }
//            }
//        }
//
//        // Create and pre-populate the database. See this article for more details:
//        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
//        private fun buildDatabase(context: Context): WeatherDatabase {
//            return Room.databaseBuilder(context, WeatherDatabase::class.java, DATABASE_NAME)
//                .addCallback(
//                    object : RoomDatabase.Callback() {
//                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            super.onCreate(db)
//                            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
//                                .setInputData(workDataOf(KEY_FILENAME to PLANT_DATA_FILENAME))
//                                .build()
//                            WorkManager.getInstance(context).enqueue(request)
//                        }
//                    }
//                )
//                .build()
//        }
//    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }
//
//    companion object CREATOR : Parcelable.Creator<WeatherDatabase> {
//        override fun createFromParcel(parcel: Parcel): WeatherDatabase {
//            return WeatherDatabase(parcel)
//        }
//
//        override fun newArray(size: Int): Array<WeatherDatabase?> {
//            return arrayOfNulls(size)
//        }
//    }
}