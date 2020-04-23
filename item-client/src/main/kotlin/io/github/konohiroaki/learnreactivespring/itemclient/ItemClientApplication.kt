package io.github.konohiroaki.learnreactivespring.itemclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ItemClientApplication

fun main(args: Array<String>) {
    runApplication<ItemClientApplication>(*args)
}
