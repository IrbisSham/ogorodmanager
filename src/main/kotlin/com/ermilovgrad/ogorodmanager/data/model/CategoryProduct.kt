package com.ermilovgrad.ogorodmanager.data.model

import jakarta.persistence.*
import org.hibernate.annotations.GenerationTime

@Entity
@Table(name = "category_product")
data class CategoryProduct (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_product_id", nullable = false, unique = true)
    val id: Long? = null,
    @Column(name="category_product_cd", nullable = true, unique = true)
    @org.hibernate.annotations.Generated(value = GenerationTime.INSERT)
    val cd: String? = null,
    @Column(name="category_product_name", nullable = false, unique = false)
    val name: String,
    @OneToMany(fetch = FetchType.LAZY, mappedBy="categoryProduct")
    val products: Set<Product>? = null
)
