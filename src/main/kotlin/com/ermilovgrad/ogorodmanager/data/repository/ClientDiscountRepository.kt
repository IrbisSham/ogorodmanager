package com.ermilovgrad.ogorodmanager.data.repository

import com.ermilovgrad.ogorodmanager.data.model.ClientDiscount
import org.springframework.data.jpa.repository.JpaRepository

interface ClientDiscountRepository : JpaRepository<ClientDiscount, Long> {

}
