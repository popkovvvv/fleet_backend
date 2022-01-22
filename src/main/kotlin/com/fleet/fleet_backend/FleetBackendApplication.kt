package com.fleet.fleet_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.fleet.fleet_backend"])
class FleetBackendApplication

fun main(args: Array<String>) {
    runApplication<FleetBackendApplication>(*args)
}
