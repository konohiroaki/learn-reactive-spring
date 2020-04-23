package io.github.konohiroaki.learnreactivespring.itemapi.controller

import io.github.konohiroaki.learnreactivespring.itemapi.document.Item
import io.github.konohiroaki.learnreactivespring.itemapi.repository.ItemReactiveRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class ItemController {

    @Autowired
    lateinit var repository: ItemReactiveRepository

    @GetMapping("/v1/items")
    fun getAllItems(): Flux<Item> {
        return repository.findAll()
    }

    @GetMapping("/v1/items/{id}")
    fun getItemByID(@PathVariable id: String): Mono<ResponseEntity<Item>> {
        return repository.findById(id)
                .map { ResponseEntity(it, HttpStatus.OK) }
                .defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @PostMapping("/v1/items")
    @ResponseStatus(HttpStatus.CREATED)
    fun createItem(@RequestBody item: Item): Mono<Item> {
        return repository.save(item)
    }

    @DeleteMapping("/v1/items/{id}")
    fun deleteItemByID(@PathVariable id: String): Mono<Void> /* we need to return something for non-blocking */ {
        return repository.deleteById(id)
    }

    @PutMapping("/v1/items/{id}")
    fun updateItem(@PathVariable id: String, @RequestBody item: Item): Mono<ResponseEntity<Item>> {
        return repository.findById(id)
                .flatMap {
                    val newItem = Item(it.id, item.desc, item.price)
                    repository.save(newItem)
                }
                .map { ResponseEntity(it, HttpStatus.OK) }
                .defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @GetMapping("/v1/error") // test error handling
    fun error(): Flux<Item> {
        return repository.findAll()
                .concatWith(Mono.error(RuntimeException("Exception occurred.")))
    }

    // by default it returns timestamp, path, status, error, message in json format
    // put this method in class annotated with @ControllerAdvice in order to apply globally (not only for this class's endpoint)
    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
    }
}
