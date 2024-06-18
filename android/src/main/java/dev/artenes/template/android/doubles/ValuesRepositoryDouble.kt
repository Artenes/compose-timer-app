package dev.artenes.template.android.doubles

import dev.artenes.template.core.interfaces.ValuesRepository

class ValuesRepositoryDouble: ValuesRepository {

    override fun get(id: Int, vararg args: Any): String {
        return "NOT IMPLEMENTED"
    }

}