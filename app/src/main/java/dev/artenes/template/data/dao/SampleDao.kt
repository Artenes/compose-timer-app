package dev.artenes.template.data.dao

import androidx.room.Dao
import androidx.room.Insert
import dev.artenes.template.data.SampleEntity

@Dao
interface SampleDao {

    @Insert
    fun save(entity: SampleEntity)

}