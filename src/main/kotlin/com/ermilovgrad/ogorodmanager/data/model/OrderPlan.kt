package com.ermilovgrad.ogorodmanager.data.model

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "order_plan")
data class OrderPlan(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_plan_id", nullable = false, unique = true)
    val id: Long? = null,
    @Column(name="order_plan_product_count", nullable = false, unique = false)
    val count: Int,
    @Column(name="order_plan_dt_beg", nullable = false, unique = false)
    val dtBeg: Timestamp,
    @Column(name="order_plan_dt_end", nullable = false, unique = false)
    val dtEnd: Timestamp,
    @Column(name="order_plan_note", nullable = true, unique = false)
    val note: String? = null,
    @Column(name="order_plan_status", nullable = false, unique = false)
    val status: Byte,
    @Column(name="order_plan_ts", nullable = false, unique = false)
    val ts: Timestamp,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_plan_client_id", nullable=false)
    val client: Client,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_plan_price_history_id", nullable=false)
    val priceHistory: PriceHistory
)
