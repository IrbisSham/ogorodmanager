package com.ermilovgrad.ogorodmanager.data.repository

import com.ermilovgrad.ogorodmanager.data.model.CategoryProduct
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryProductRepository : JpaRepository<CategoryProduct, Long> {

}
