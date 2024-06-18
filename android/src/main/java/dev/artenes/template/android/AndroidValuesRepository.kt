package dev.artenes.template.android

import android.content.Context
import dev.artenes.template.core.interfaces.ValuesRepository

class AndroidValuesRepository(private val context: Context) : ValuesRepository {

    override fun get(id: Int, vararg args: Any) = context.getString(id, *args)

}