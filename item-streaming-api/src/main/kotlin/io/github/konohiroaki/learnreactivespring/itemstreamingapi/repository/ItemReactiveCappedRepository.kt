package io.github.konohiroaki.learnreactivespring.itemstreamingapi.repository

import io.github.konohiroaki.learnreactivespring.itemstreamingapi.document.CappedItem
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.mongodb.repository.Tailable
import reactor.core.publisher.Flux

interface ItemReactiveCappedRepository : ReactiveMongoRepository<CappedItem, String> {

    //tailable cursor: don't close connection when all result are returned. but instead wait for new data insertion.
    @Tailable
    fun findCappedItemBy(): Flux<CappedItem>
}
