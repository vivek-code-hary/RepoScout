package com.example.ashrut.reposcout.data.local.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ashrut.reposcout.data.local.dao.RepositoryDao
import com.example.ashrut.reposcout.data.local.entity.RepositoryEntity

@Database(
    entities = [RepositoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RepoScoutDatabase : RoomDatabase() {

    abstract fun repositoryDao(): RepositoryDao

    companion object {

        @Volatile
        private var INSTANCE: RepoScoutDatabase? = null

        fun getInstance(context: Context): RepoScoutDatabase {

            return INSTANCE ?: synchronized(this) {

                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RepoScoutDatabase::class.java,
                    "reposcout_database"
                )
                    .build()
                    .also { database ->
                        INSTANCE = database
                    }
            }
        }
    }
}