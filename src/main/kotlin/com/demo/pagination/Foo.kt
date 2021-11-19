package com.demo.pagination

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Foo(
    val name: String? = null
) {
    @Id
    var id: ObjectId? = null
}
