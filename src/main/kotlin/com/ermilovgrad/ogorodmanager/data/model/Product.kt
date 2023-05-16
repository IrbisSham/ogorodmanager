package com.ermilovgrad.ogorodmanager.data.model

import jakarta.persistence.*
import org.hibernate.annotations.GenerationTime

@Entity
@Table(name = "product")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id", nullable = false, unique = true)
    val id: Long? = null,
    @Column(name="product_cd", nullable = false, unique = true)
    @org.hibernate.annotations.Generated(value = GenerationTime.INSERT)
    val cd: String? = null,
    @Column(name="product_name", nullable = false, unique = false)
    val name: String,
    @OneToMany(fetch = FetchType.LAZY, mappedBy="product")
    val priceHistories: Set<PriceHistory>? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_category_id", nullable=false)
    val categoryProduct: CategoryProduct
)
