package com.lit.remindme.feature_events.data.data_sources

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.lit.remindme.feature_events.domain.model.Event

@Database(
    entities = [Event::class],
    version = 2,
    autoMigrations = [
    AutoMigration(from = 1, to = 2)
    ]
)
abstract class InternalEventDb: RoomDatabase() {
    abstract val internalDataDao: InternalDataDao

    class MigrateNone : AutoMigrationSpec

    companion object {
        const val DATABASE_NAME = "events.db"
    }
}