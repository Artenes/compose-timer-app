package dev.artenes.template.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.artenes.template.data.dao.SampleDao

@Database(entities = [SampleEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sampleDao(): SampleDao

}
