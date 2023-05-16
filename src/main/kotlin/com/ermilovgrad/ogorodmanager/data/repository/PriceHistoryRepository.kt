package com.ermilovgrad.ogorodmanager.data.repository

import com.ermilovgrad.ogorodmanager.data.model.PriceHistory
import org.springframework.data.jpa.repository.JpaRepository

interface PriceHistoryRepository : JpaRepository<PriceHistory, Long> {

}
