package com.ermilovgrad.ogorodmanager.data.model

import jakarta.persistence.*
import org.hibernate.annotations.GenerationTime

@Entity
@Table(name = "discount")
data class Discount(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="discount_id", nullable = false, unique = true)
    val id: Long? = null,
    @Column(name="discount_cd", nullable = false, unique = true)
    @org.hibernate.annotations.Generated(value = GenerationTime.INSERT)
    val cd: String? = null,
    @Column(name="discount_name", nullable = false, unique = false)
    val name: String,
    @Column(name="discount_percent", nullable = false, unique = false)
    val percent: Double = 0.0,
    @Column(name="discount_fixed", nullable = false, unique = false)
    val fixed: Double = 0.0,
    @OneToMany(fetch = FetchType.LAZY, mappedBy="discount")
    val clientDiscounts: Set<ClientDiscount>? = null
)
