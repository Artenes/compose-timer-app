package dev.artenes.timer.android.doubles

import dev.artenes.timer.core.interfaces.ValuesRepository

class ValuesRepositoryDouble: ValuesRepository {

    override fun get(id: Int, vararg args: Any): String {
        return "NOT IMPLEMENTED"
    }

}