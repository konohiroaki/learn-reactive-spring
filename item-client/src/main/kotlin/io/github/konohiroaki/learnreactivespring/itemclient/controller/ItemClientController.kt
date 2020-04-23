package io.github.konohiroaki.learnreactivespring.itemclient.controller

import io.github.konohiroaki.learnreactivespring.itemclient.domain.Item
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class ItemClientController {

    val client = WebClient.create("http://localhost:8080")

    @GetMapping("/items")
    fun getAllItems(): Flux<Item> {
        return client.get().uri("/v2/items")
                .retrieve() // retrieve() gives body, and exchange() will give ClientResponse which has status also
                .bodyToFlux(Item::class.java)
    }

    @GetMapping("/items/singleItem")
    fun getItem(): Mono<Item> {
        val id = "ABC"
        return client.get().uri("/v2/items/{id}", id)
                .retrieve()
                .bodyToMono(Item::class.java)
    }

    @PostMapping("/items")
    fun createItem(@RequestBody item: Item): Mono<Item> {
        return client.post().uri("/v2/items")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(item)
                .retrieve()
                .bodyToMono(Item::class.java)
                .log()
    }

    @DeleteMapping("/items/{id}")
    fun deleteItem(@PathVariable id: String): Mono<Void> {
        return client.delete().uri("/v2/items/{id}", id)
                .retrieve()
                .bodyToMono(Void::class.java)
                .log()
    }

    @PutMapping("/items/{id}")
    fun updateItem(@PathVariable id: String, @RequestBody item: Item): Mono<Item> {
        return client.put().uri("/v2/items/{id}", id)
                .bodyValue(item)
                .retrieve()
                .bodyToMono(Item::class.java)
                .log()
    }

    @GetMapping("/items/error")
    fun error(): Flux<Item> {
        return client.get().uri("/v2/error")
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError) {
                    val str = it.bodyToMono(String::class.java)
                    str.flatMap { throw RuntimeException(it) }
                }
                .bodyToFlux(Item::class.java)
    }
}
