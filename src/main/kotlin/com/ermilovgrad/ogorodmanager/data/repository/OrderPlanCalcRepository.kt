package com.ermilovgrad.ogorodmanager.data.repository

import com.ermilovgrad.ogorodmanager.data.model.OrderPlanCalc

interface OrderPlanCalcRepository : ReadOnlyRepository<OrderPlanCalc, Long> {
    fun findAllByOrderByTsDescIdDesc(): List<OrderPlanCalc>
//    {
//        val orders: List<Order> = mutableListOf()
//        orders.add (1, Order(Sort.Direction.DESC, "Ts"))
//        orders.add (2, Order(Sort.Direction.DESC, "Id"))
//        return findAll(orders)
//    }
}
