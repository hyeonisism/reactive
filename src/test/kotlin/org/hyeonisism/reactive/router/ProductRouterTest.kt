package org.hyeonisism.reactive.router

import org.hyeonisism.reactive.model.Product
import org.hyeonisism.reactive.repository.ProductRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal


@ExtendWith(SpringExtension::class)
@WebFluxTest(value = [ProductRouter::class, ProductHandler::class])
internal class ProductRouterTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var repository: ProductRepository

    @Test
    internal fun `when get product by id then return cake`() {
        // given
        val cake = Product(1L, "cake", BigDecimal(13000))

        given(repository.findById(anyLong())).willReturn(Mono.just(cake))

        // expect
        webTestClient.get().uri("/products/1").accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBodyList(Product::class.java)
            .hasSize(1)
            .contains(cake)
    }

    @Test
    internal fun `when get products then return cake and pizza`() {
        // given
        val cake = Product(1L, "cake", BigDecimal(13000))
        val pizza = Product(2L, "pizza", BigDecimal(20000))

        given(repository.findAll()).willReturn(Flux.just(cake, pizza))

        // expect
        webTestClient.get().uri("/products").accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBodyList(Product::class.java)
            .hasSize(2)
            .contains(cake, pizza)
    }
}
