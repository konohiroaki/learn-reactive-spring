package io.github.konohiroaki.learnreactivespring.itemapi.initialize

import io.github.konohiroaki.learnreactivespring.itemapi.document.Item
import io.github.konohiroaki.learnreactivespring.itemapi.repository.ItemReactiveRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
@Profile("!test") // not run command line runner when test profile active
class ItemDataInitializer : CommandLineRunner {

    @Autowired
    lateinit var repository: ItemReactiveRepository

    override fun run(vararg args: String?) {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap { repository.save(it) }
                .thenMany(repository.findAll())
                .subscribe { println("Inserted: $it") }
    }

    private fun data(): List<Item> {
        return listOf(
                Item(null, "Samsung TV", 399.99),
                Item(null, "LG TV", 329.99),
                Item(null, "Apple Watch", 349.99),
                Item("ABC", "Beats HeadPhones", 149.99)
        )
    }
}
