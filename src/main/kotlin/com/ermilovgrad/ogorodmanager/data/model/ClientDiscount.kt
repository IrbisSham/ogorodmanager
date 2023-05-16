package com.ermilovgrad.ogorodmanager.data.model

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "client_discount")
data class ClientDiscount(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="client_discount_id", nullable = false, unique = true)
    val id: Long? = null,
    @Column(name="client_discount_dt_beg", nullable = false, unique = false)
    val dtBeg: Timestamp,
    @Column(name="client_discount_dt_end", nullable = false, unique = false)
    val dtEnd: Timestamp,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="client_discount_client_id", nullable=false)
    val client: Client,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="client_discount_discount_id", nullable=false)
    val discount: Discount
)
