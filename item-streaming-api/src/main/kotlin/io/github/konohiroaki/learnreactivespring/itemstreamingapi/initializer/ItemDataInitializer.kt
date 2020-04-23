package io.github.konohiroaki.learnreactivespring.itemstreamingapi.initializer

import io.github.konohiroaki.learnreactivespring.itemstreamingapi.document.CappedItem
import io.github.konohiroaki.learnreactivespring.itemstreamingapi.repository.ItemReactiveCappedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.CollectionOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.Duration

@Component
@Profile("!test")
class ItemDataInitializer : CommandLineRunner {

    @Autowired
    lateinit var mongo: MongoOperations

    @Autowired
    lateinit var repository: ItemReactiveCappedRepository

    override fun run(vararg args: String?) {
        createCappedCollection()
        dataSetup()
    }

    private fun createCappedCollection() {
        mongo.dropCollection(CappedItem::class.java)
        mongo.createCollection(CappedItem::class.java, CollectionOptions.empty().maxDocuments(20).size(50000).capped())
    }

    private fun dataSetup() {
        val flux = Flux.interval(Duration.ofSeconds(1))
                .map { CappedItem(null, "Random Item $it", 100.00 + it) }
        repository.insert(flux)
                .subscribe { println("Inserted: $it") }
    }
}
