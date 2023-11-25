package com.ring.ring.kmptodo.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import data.ScreenSettingsRepository
import data.TodoRepository
import data.local.db.LocalDb
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModules {
    @Singleton
    @Provides
    fun provideSqlDriver(@ApplicationContext context: Context): SqlDriver =
        AndroidSqliteDriver(LocalDb.Schema, context, "Local.db")

    @Singleton
    @Provides
    fun provideTodoRepository(sqlDriver: SqlDriver): TodoRepository =
        TodoRepository(sqlDriver)

    @Singleton
    @Provides
    fun provideScreenSettingsRepository(): ScreenSettingsRepository =
        ScreenSettingsRepository()
}