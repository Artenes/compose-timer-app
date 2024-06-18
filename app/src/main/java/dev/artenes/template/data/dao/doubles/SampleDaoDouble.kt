package dev.artenes.template.data.dao.doubles

import dev.artenes.template.data.SampleEntity
import dev.artenes.template.data.dao.SampleDao

class SampleDaoDouble: SampleDao {

    override fun save(entity: SampleEntity) {
        //fake save
    }
}