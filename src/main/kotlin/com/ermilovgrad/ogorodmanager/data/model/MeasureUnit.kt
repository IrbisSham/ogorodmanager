package com.ermilovgrad.ogorodmanager.data.model

import jakarta.persistence.*
import org.hibernate.annotations.GenerationTime

@Entity
@Table(name = "measure_unit")
data class MeasureUnit(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="measure_unit_id", nullable = false, unique = true)
    val id: Long? = null,
    @Column(name="measure_unit_cd", nullable = false, unique = true)
    @org.hibernate.annotations.Generated(value = GenerationTime.INSERT)
    val cd: String? = null,
    @Column(name="measure_unit_name", nullable = false, unique = false)
    val name: String,
    @OneToMany(fetch = FetchType.LAZY, mappedBy="measureUnit")
    val priceHistories: Set<PriceHistory>? = null
)
