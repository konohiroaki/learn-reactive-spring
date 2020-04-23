package io.github.konohiroaki.learnreactivespring.itemapi.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Item(
        @Id
        val id: String?,
        val desc: String,
        val price: Double
)
