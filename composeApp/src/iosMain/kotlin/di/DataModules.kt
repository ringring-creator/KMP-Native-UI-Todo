package di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import data.ScreenSettingsRepository
import data.TodoRepository
import data.local.db.LocalDb

fun DataModules.Factory.createTodoRepository(): TodoRepository {
    return TodoRepository(
        sqlDriver = createSqliteDriver()
    )
}

fun DataModules.Factory.createScreenSettingsRepository(): ScreenSettingsRepository {
    return ScreenSettingsRepository()
}

private fun createSqliteDriver() = NativeSqliteDriver(LocalDb.Schema, "Local.db")