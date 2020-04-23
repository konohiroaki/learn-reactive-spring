package io.github.konohiroaki.learnreactivespring.itemstreamingapi.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class CappedItem(
        @Id
        val id: String?,
        val desc: String,
        val price: Double
)
