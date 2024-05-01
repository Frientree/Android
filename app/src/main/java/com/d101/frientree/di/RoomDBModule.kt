package com.d101.frientree.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.d101.data.roomdb.AppDatabase
import com.d101.data.roomdb.dao.CalendarFruitDao
import com.d101.data.roomdb.dao.FruitDao
import com.d101.data.roomdb.dao.JuiceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {
    @Singleton
    @Provides
    fun provideRoomDB(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "frientree_db",
        ).addMigrations(migrationFrom1To2).build()
    }

    @Provides
    fun provideFruitDao(database: AppDatabase): FruitDao {
        return database.fruitDao()
    }

    @Provides
    fun provideCalendarFruitDao(database: AppDatabase): CalendarFruitDao {
        return database.calendarFruitDao()
    }

    @Provides
    fun provideJuiceDao(database: AppDatabase): JuiceDao {
        return database.juiceDao()
    }

    private val migrationFrom1To2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE JuiceEntity " +
                    "ADD COLUMN juiceBackgroundImageUrl TEXT NOT NULL DEFAULT ''",
            )
        }
    }
}
