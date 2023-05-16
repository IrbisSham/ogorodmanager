package com.ermilovgrad.ogorodmanager.data.repository

import com.ermilovgrad.ogorodmanager.data.model.Discount
import org.springframework.data.jpa.repository.JpaRepository

interface DiscountRepository : JpaRepository<Discount, Long> {

}
