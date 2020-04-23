package io.github.konohiroaki.learnreactivespring.itemapi.handler

import io.github.konohiroaki.learnreactivespring.itemapi.document.Item
import io.github.konohiroaki.learnreactivespring.itemapi.repository.ItemReactiveRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ItemHandler {

    @Autowired
    lateinit var repository: ItemReactiveRepository

    fun getAllItems(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(repository.findAll(), Item::class.java)
    }

    fun getItemByID(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id")
        val item = repository.findById(id)
        return item.flatMap {
            ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(it)
        }.switchIfEmpty(ServerResponse.notFound().build())
    }

    fun createItem(request: ServerRequest): Mono<ServerResponse> {
        val item = request.bodyToMono(Item::class.java)
        return item.flatMap {
            ServerResponse.status(HttpStatus.CREATED) // TODO: how to put location header?
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(repository.save(it), Item::class.java)
        }
    }

    fun deleteItem(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id")
        val deletedItem = repository.deleteById(id)
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(deletedItem, Void::class.java)
    }

    fun updateItem(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id")
        val updatedItem = request.bodyToMono(Item::class.java).flatMap { update ->
            repository.findById(id).flatMap {
                repository.save(Item(id, update.desc, update.price))
            }
        }
        return updatedItem.flatMap {
            ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(updatedItem, Item::class.java)
        }.switchIfEmpty(ServerResponse.notFound().build())
    }

    fun error(request: ServerRequest): Mono<ServerResponse> {
        throw RuntimeException("Exception occurred.")
    }
}
