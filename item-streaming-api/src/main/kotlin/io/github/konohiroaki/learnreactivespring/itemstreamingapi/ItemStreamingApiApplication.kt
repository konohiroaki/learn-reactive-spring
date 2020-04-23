package io.github.konohiroaki.learnreactivespring.itemstreamingapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ItemStreamingApiApplication

fun main(args: Array<String>) {
    runApplication<ItemStreamingApiApplication>(*args)
}
