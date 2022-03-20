package com.setyofp.githubuserfinalapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GithubUser::class], exportSchema = false, version = 1)
abstract class UserRoomDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var mInstance: UserRoomDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): UserRoomDatabase {
            if (mInstance == null) {
                synchronized(UserRoomDatabase::class.java) {
                    mInstance = Room.databaseBuilder(
                        context.applicationContext,
                        UserRoomDatabase::class.java,
                        "User.db"
                    ).build()
                }
            }
            return mInstance as UserRoomDatabase
        }
    }
}
