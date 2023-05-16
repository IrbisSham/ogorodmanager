package com.ermilovgrad.ogorodmanager.data.repository

import com.ermilovgrad.ogorodmanager.data.model.MeasureUnit
import org.springframework.data.jpa.repository.JpaRepository

interface MeasureUnitRepository : JpaRepository<MeasureUnit, Long> {

}
