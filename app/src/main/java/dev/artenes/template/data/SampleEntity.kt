package dev.artenes.template.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime
import java.util.UUID

@Entity(tableName = "entities")
data class SampleEntity(
    @PrimaryKey
    val id: UUID,
    @ColumnInfo("created_at")
    val createdAt: OffsetDateTime,
    @ColumnInfo("updated_at")
    val updatedAt: OffsetDateTime
)