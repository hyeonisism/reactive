package org.hyeonisism.reactive.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("product")
data class Product(
    @Id
    val id: Long? = null,
    val name: String,
    val price: BigDecimal
)