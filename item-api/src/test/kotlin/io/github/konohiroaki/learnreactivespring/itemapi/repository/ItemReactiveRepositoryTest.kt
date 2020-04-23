package io.github.konohiroaki.learnreactivespring.itemapi.repository

import io.github.konohiroaki.learnreactivespring.itemapi.document.Item
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.annotation.DirtiesContext
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

@DataMongoTest
@DirtiesContext
class ItemReactiveRepositoryTest {

    @Autowired
    lateinit var repository: ItemReactiveRepository

    val items = listOf(
            Item(null, "Samsung TV", 400.0),
            Item(null, "LG TV", 420.0),
            Item(null, "Apple TV", 250.0),
            Item("ABC", "Kindle", 90.0)
    )

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap { repository.save(it) }
                .doOnNext { println("Inserted: $it") }
                .blockLast() // wait until all are inserted so tests will have the data
    }

    @Test
    fun getAllItems() {
        StepVerifier.create(repository.findAll())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete()
    }

    @Test
    fun getItemByID() {
        StepVerifier.create(repository.findById("ABC"))
                .expectSubscription()
                .expectNextMatches { it.desc == "Kindle" }
                .verifyComplete()
    }

    @Test
    fun findItemByDesc() {
        StepVerifier.create(repository.findByDesc("Kindle").log("findByDesc"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete()
    }

    @Test
    fun saveItem() {
        val item = Item(null, "Google Home", 30.00)
        val savedItem = repository.save(item)

        StepVerifier.create(savedItem.log())
                .expectSubscription()
                .expectNextMatches { it.id != null && it.desc == "Google Home" }
                .verifyComplete()
    }

    @Test
    fun updateItem() {
        val newPrice = 520.00
        val updatedItem = repository.findByDesc("LG TV")
                .map { Item(it.id, it.desc, newPrice) }
                .flatMap { repository.save(it) }

        StepVerifier.create(updatedItem.log())
                .expectSubscription()
                .expectNextMatches { it.price == 520.00 }
                .verifyComplete()
    }

    @Test
    fun deleteItem() {
        val deletedItem = repository.findByDesc("Kindle")
                .flatMap { repository.delete(it) }

        StepVerifier.create(deletedItem.log())
                .verifyComplete()
        StepVerifier.create(repository.findAll())
                .expectNextCount(3)
                .verifyComplete()
    }
}
