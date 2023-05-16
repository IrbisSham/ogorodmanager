package com.ermilovgrad.ogorodmanager.data.model

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "price_history")
data class PriceHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="price_history_id", nullable = false, unique = true)
    val id: Long? = null,
    @Column(name="price_history_sum", nullable = false, unique = false)
    val sum: Double,
    @Column(name="price_history_dt_beg", nullable = false, unique = false)
    val dtBeg: Timestamp,
    @Column(name="price_history_dt_end", nullable = false, unique = false)
    val dtEnd: Timestamp,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="price_history_measure_unit_id", nullable=false)
    val measureUnit: MeasureUnit,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="price_history_product_id", nullable=false)
    val product: Product,
    @OneToMany(fetch = FetchType.LAZY, mappedBy="priceHistory")
    val orderPlans: Set<OrderPlan>? = null
)
