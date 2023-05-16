package com.ermilovgrad.ogorodmanager.data.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.Immutable
import java.sql.Timestamp

@Entity
@Immutable
@Table(name = "order_plan_usr")
data class OrderPlanCalc(
    @Id
    @Column(name="order_plan_id", nullable = false, unique = true)
    val id: Long? = null,
    @Column(name="client_fio", nullable = false, unique = false)
    val clientFio: String,
    @Column(name="client_address", nullable = false, unique = false)
    val clientAddress: String? = null,
    @Column(name="client_tel", nullable = false, unique = false)
    val clientTel: String? = null,
    @Column(name="product_name", nullable = false, unique = false)
    val productName: String,
    @Column(name="order_plan_product_count", nullable = false, unique = false)
    val productCount: Int,
    @Column(name="measure_unit_name", nullable = false, unique = false)
    val measureUnitName: String,
    @Column(name="price", nullable = false, unique = false)
    val price: Double,
    @Column(name="sum", nullable = false, unique = false)
    val sum: Double,
    @Column(name="order_plan_dt_beg", nullable = false, unique = false)
    val dtBeg: Timestamp,
    @Column(name="order_plan_dt_end", nullable = false, unique = false)
    val dtEnd: Timestamp,
    @Column(name="order_plan_note", nullable = false, unique = false)
    val note: String? = null,
    @Column(name="status", nullable = false, unique = false)
    val status: String,
    @Column(name="order_plan_status", nullable = false, unique = false)
    val statusId: Byte,
    @Column(name="order_plan_ts", nullable = false, unique = false)
    val ts: Timestamp,
    @Column(name="summary", nullable = false, unique = false)
    val summary: String
)

fun OrderPlanCalc.formSummary(orderPlanCalc: OrderPlanCalc): String {
    return "Заказ_${orderPlanCalc.clientFio}_${orderPlanCalc.clientAddress ?: "0"}_${orderPlanCalc.clientTel}_${orderPlanCalc.productName}_${orderPlanCalc.productCount}_${orderPlanCalc.measureUnitName}_${orderPlanCalc.dtBeg}_${orderPlanCalc.dtEnd}_${orderPlanCalc.ts}"
}
