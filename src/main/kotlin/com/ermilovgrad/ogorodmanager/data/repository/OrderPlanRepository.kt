package com.ermilovgrad.ogorodmanager.data.repository

import com.ermilovgrad.ogorodmanager.data.model.OrderPlan
import org.springframework.data.jpa.repository.JpaRepository

interface OrderPlanRepository : JpaRepository<OrderPlan, Long> {

}
