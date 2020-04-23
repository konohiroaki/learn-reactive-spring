package io.github.konohiroaki.learnreactivespring.itemapi.repository

import io.github.konohiroaki.learnreactivespring.itemapi.document.Item
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface ItemReactiveRepository : ReactiveMongoRepository<Item, String> {

    fun findByDesc(desc: String): Flux<Item>
}
