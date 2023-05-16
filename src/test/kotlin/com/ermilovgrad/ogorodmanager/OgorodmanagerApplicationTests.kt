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
class OgorodmanagerApplicationTests {

    @Test
    fun contextLoads() {
    }

//    @Test
//    fun whenLoadAndAddEvent_thenLoadData() {
//        val mapper = jacksonObjectMapper()
//        mapper.registerKotlinModule()
//        mapper.registerModule(JavaTimeModule())
//        val jsonString = String(
//            OgorodmanagerApplicationTests::class.java.getResourceAsStream("/calendarSample.json")
//                .readAllBytes()
//        )
//        val items: List<Event> = mapper.readValue(jsonString)
//        val events: Events = Events()
//        events.setItems(items)
//        val calendar = Calendar(Calendar.Builder(httpTransport, MockJsonFactory(), authentication))
//        BDDMockito.given(calendar.events()).willReturn()
//        BDDMockito.given(googleCalendarAPI.getCalendar()).willReturn(mockGoogleClient.)
//        BDDMockito.given(googleCalendarAPI.save(user)).willReturn(user);
//        val items = googleCalendarAPI?.getEvents(googleCalendarAPI.getCalendar())
//        assertNotNull(items)
//        assertTrue(items!!.isNotEmpty())
//    }

}



