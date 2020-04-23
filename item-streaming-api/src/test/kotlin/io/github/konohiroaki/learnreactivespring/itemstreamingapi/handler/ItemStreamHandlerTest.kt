package io.github.konohiroaki.learnreactivespring.itemstreamingapi.handler

import io.github.konohiroaki.learnreactivespring.itemstreamingapi.document.CappedItem
import io.github.konohiroaki.learnreactivespring.itemstreamingapi.repository.ItemReactiveCappedRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.CollectionOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration


@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
@ActiveProfiles("test")
class ItemStreamHandlerTest {

    @Autowired
    lateinit var repository: ItemReactiveCappedRepository

    @Autowired
    lateinit var mongo: MongoOperations

    @Autowired
    lateinit var client: WebTestClient

    @BeforeEach
    fun setUp() {
        mongo.dropCollection(CappedItem::class.java)
        mongo.createCollection(CappedItem::class.java, CollectionOptions.empty().maxDocuments(20).size(50000).capped())
        val flux = Flux.interval(Duration.ofMillis(100))
                .map { CappedItem(null, "Random Item $it", 100.00 + it) }
                .take(5)
        repository.insert(flux)
                .doOnNext { println("Inserted: $it") }
                .blockLast()
    }

    @Test
    fun getItemsStream() {
        val flux = client.get().uri("/v2/items")
                .exchange()
                .expectStatus().isOk
                .returnResult(CappedItem::class.java)
                .responseBody
                .take(5)

        StepVerifier.create(flux.log())
                .expectNextCount(5)
                .thenCancel()
                .verify()
    }
}
