package org.hyeonisism.reactive.db

import io.r2dbc.h2.H2ConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.h2.tools.Server
import org.hyeonisism.reactive.model.Product
import org.hyeonisism.reactive.repository.ProductRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import reactor.core.publisher.Flux
import java.math.BigDecimal


@Profile("demo")
@Configuration
class MemoryDatabaseConfiguration(@Value("\${h2.web-server.port}") port: Int) {

    val webServer: Server = Server.createWebServer("-webPort", port.toString())

    @EventListener(ContextRefreshedEvent::class)
    fun start() {
        webServer.start()
    }

    @EventListener(ContextClosedEvent::class)
    fun stop() {
        webServer.stop()
    }
}

@Profile("demo")
@Configuration
class MemoryDatabaseInitializeConfiguration(
    val productRepository: ProductRepository
) {
    @EventListener(ApplicationReadyEvent::class)
    fun initProducts() {
        val pizza = Product(
            name = "Pizza",
            price = BigDecimal(20000)
        )

        val cake = Product(
            name = "Cake",
            price = BigDecimal(13000)
        )

        productRepository.saveAll(Flux.just(pizza, cake)).blockLast()
    }
}

@Profile("demo")
@EnableR2dbcRepositories
@Configuration
class MemoryDatabaseR2dbcConfiguration : AbstractR2dbcConfiguration() {
    override fun connectionFactory(): ConnectionFactory {
        return H2ConnectionFactory.inMemory("testdb")
    }

    @Bean
    fun connectionFactoryInitializer(): ConnectionFactoryInitializer {
        val initializer = ConnectionFactoryInitializer()
        val resourceDatabasePopulator = ResourceDatabasePopulator()
        resourceDatabasePopulator.addScript(ClassPathResource("sql/product-h2.sql"))
        initializer.setConnectionFactory(connectionFactory())
        initializer.setDatabasePopulator(resourceDatabasePopulator)
        return initializer
    }
}