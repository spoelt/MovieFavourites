package com.spoelt.moviefavourites.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.spoelt.moviefavourites.data.model.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    // the database needs to know about the DAO
    abstract val movieDao: MovieDao

    //  companion object allows clients to access the methods for creating or getting the database
    //  without instantiating the class. Since the only purpose of this class is to provide a
    //  database, there is no reason to ever instantiate it!
    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase {
            synchronized(this) {
                var instance = INSTANCE

                // check if there is already a database, if not -> build it!
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java,
                        "movie_database"
                    )
                        // Add the required migration strategy to the builder
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }

        }
    }
}