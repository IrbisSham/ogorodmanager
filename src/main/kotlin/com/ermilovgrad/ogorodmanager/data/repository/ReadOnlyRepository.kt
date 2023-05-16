package com.ermilovgrad.ogorodmanager.data.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository
import java.util.Optional

@NoRepositoryBean
interface ReadOnlyRepository<T, ID> : Repository<T, ID> {

    fun findAll(): List<T>

    fun findAll(sort: Sort): List<T>

    fun findAll(pageable: Pageable): List<T>

    fun findById(id: ID ): Optional<T>

}
