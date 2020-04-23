package io.github.konohiroaki.learnreactivespring.itemstreamingapi.handler

import io.github.konohiroaki.learnreactivespring.itemstreamingapi.document.CappedItem
import io.github.konohiroaki.learnreactivespring.itemstreamingapi.repository.ItemReactiveCappedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ItemStreamHandler {

    @Autowired
    lateinit var repository: ItemReactiveCappedRepository

    fun getItemsStream(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(repository.findCappedItemBy(), CappedItem::class.java)
    }
}
