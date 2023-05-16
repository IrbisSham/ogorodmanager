package com.ermilovgrad.ogorodmanager

import com.ermilovgrad.ogorodmanager.data.model.*
import com.ermilovgrad.ogorodmanager.data.repository.*
import com.ermilovgrad.ogorodmanager.service.GoogleTaskAPI
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GoogleTaskTests {

    @Autowired
    lateinit var googleTaskAPI: GoogleTaskAPI

    @Test
    fun whenCheckRealGoogleTasks_thenLoadData() {
        val items = googleTaskAPI?.getTasks(googleTaskAPI.getService(), 1000)
        assertNotNull(items)
        assertTrue(items!!.isNotEmpty())
    }

}



