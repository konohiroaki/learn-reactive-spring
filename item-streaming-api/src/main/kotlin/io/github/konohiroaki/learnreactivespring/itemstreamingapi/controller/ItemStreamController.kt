package io.github.konohiroaki.learnreactivespring.itemstreamingapi.controller

import io.github.konohiroaki.learnreactivespring.itemstreamingapi.document.CappedItem
import io.github.konohiroaki.learnreactivespring.itemstreamingapi.repository.ItemReactiveCappedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class ItemStreamController {

    @Autowired
    lateinit var repository: ItemReactiveCappedRepository

    @GetMapping(value = ["/v1/items"], produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    fun getItemsStream(): Flux<CappedItem> {
        return repository.findCappedItemBy()
    }

}
