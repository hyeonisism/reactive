package org.hyeonisism.reactive.router

import org.hyeonisism.reactive.model.Product
import org.hyeonisism.reactive.repository.ProductRepository
import org.hyeonisism.reactive.router.RouterProperty.Companion.ID
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono


@Router
class ProductRouter {

    @Bean
    fun routes(handler: ProductHandler): RouterFunction<*> {
        return RouterFunctions
            .route(GET("/products"), handler::getProducts)
            .andRoute(GET("/products/{id}"), handler::getProduct)
    }
}

@Component
class ProductHandler(val productRepository: ProductRepository) {

    fun getProduct(request: ServerRequest): Mono<ServerResponse> {
        val product = Mono.just(request.pathVariable(ID))
            .flatMap { id -> productRepository.findById(id.toLong()) }

        return ServerResponse.ok().body(product, Product::class.java)
    }

    fun getProducts(request: ServerRequest): Mono<ServerResponse> {
        val products = productRepository.findAll()

        return ServerResponse.ok().body(products, Product::class.java)
    }
}