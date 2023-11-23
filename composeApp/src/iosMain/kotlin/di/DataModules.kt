package di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import data.TodoRepository
import data.local.db.LocalDb

fun DataModules.Factory.createTodoRepository(): TodoRepository {
    return TodoRepository(
        sqlDriver = createSqliteDriver()
    )
}

private fun createSqliteDriver() = NativeSqliteDriver(LocalDb.Schema, "Local.db")