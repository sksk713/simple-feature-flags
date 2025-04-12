package org.innercircle.simplefeatureflags

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SimpleFeatureFlagsApplication

fun main(args: Array<String>) {
    runApplication<SimpleFeatureFlagsApplication>(*args)
}
