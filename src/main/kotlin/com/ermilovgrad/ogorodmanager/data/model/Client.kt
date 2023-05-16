package com.ermilovgrad.ogorodmanager.data.model

import jakarta.persistence.*
import org.hibernate.annotations.GenerationTime

@Entity
@Table(name = "client")
data class Client(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="client_id", nullable = false, unique = true)
    val id: Long? = null,
    @Column(name="client_cd", nullable = false, unique = false)
    @org.hibernate.annotations.Generated(value = GenerationTime.INSERT)
    val cd: String? = null,
    @Column(name="client_fio", nullable = false, unique = false)
    val fio: String,
    @Column(name="client_tel", nullable = false, unique = true)
    val tel: String,
    @Column(name="client_telegram", nullable = true, unique = false)
    val telegram: String? = null,
    @Column(name="client_address", nullable = true, unique = false)
    val address: String? = null,
    @Column(name="client_email", nullable = true, unique = false)
    val email: String? = null,
    @OneToMany(fetch = FetchType.LAZY, mappedBy="client")
    val orderPlans: Set<OrderPlan>? = null,
    @OneToMany(fetch = FetchType.LAZY, mappedBy="client")
    val clientDiscounts: Set<ClientDiscount>? = null
)
