package dev.artenes.timer.android

import android.content.Context
import dev.artenes.timer.core.interfaces.ValuesRepository

class AndroidValuesRepository(private val context: Context) : ValuesRepository {

    override fun get(id: Int, vararg args: Any) = context.getString(id, *args)

}