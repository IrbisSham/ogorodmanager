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
class OgorodmanagerDBTests {

    val timezoneStr = "+03:00"

    @Autowired
    lateinit var categoryProductRepository: CategoryProductRepository

    @Autowired
    lateinit var clientRepository: ClientRepository

    @Autowired
    lateinit var orderPlanCalcRepository: OrderPlanCalcRepository

    @Autowired
    lateinit var orderPlanRepository: OrderPlanRepository

    @Autowired
    lateinit var measureUnitRepository: MeasureUnitRepository

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var priceHistoryRepository: PriceHistoryRepository

    @Autowired
    lateinit var discountRepository: DiscountRepository

    @Autowired
    lateinit var clientDiscountRepository: ClientDiscountRepository

    @BeforeAll
    @Throws(Exception::class)
    fun setup() {
        val categoryProduct = categoryProductRepository.save(CategoryProduct(name = "Fruits"))
        val measureUnit = measureUnitRepository.save(MeasureUnit(name = "kg"))
        val client = clientRepository.save(Client(fio = "Ivanov Ivan", tel = "1"))
        val product = productRepository.save(Product(name = "Apple", categoryProduct = categoryProduct))
        val priceHistory = priceHistoryRepository.save(PriceHistory(product = product,
            sum = 50.0,
            dtBeg = Timestamp.from(LocalDateTime.now().minus(2, ChronoUnit.MONTHS).toInstant(ZoneOffset.of(timezoneStr))),
            dtEnd = Timestamp.from(LocalDateTime.now().plus(1, ChronoUnit.MONTHS).toInstant(ZoneOffset.of(timezoneStr))),
            measureUnit = measureUnit
        ))
        val orderPlan = orderPlanRepository.save(OrderPlan(
            client = client,
            priceHistory = priceHistory,
            count = 5,
            dtBeg = Timestamp.from(LocalDateTime.now().minus(2, ChronoUnit.MONTHS).toInstant(ZoneOffset.of(timezoneStr))),
            dtEnd = Timestamp.from(LocalDateTime.now().plus(1, ChronoUnit.MONTHS).toInstant(ZoneOffset.of(timezoneStr))),
            status = 0,
            ts = Timestamp.from(Instant.now())
        ))
        val discount = discountRepository.save(Discount(name = "base", percent = 10.0))
        val clientDiscount = clientDiscountRepository.save(ClientDiscount(
            dtBeg = Timestamp.from(LocalDateTime.now().minus(2, ChronoUnit.MONTHS).toInstant(ZoneOffset.of(timezoneStr))),
            dtEnd = Timestamp.from(LocalDateTime.now().plus(1, ChronoUnit.MONTHS).toInstant(ZoneOffset.of(timezoneStr))),
            client = client,
            discount = discount
        ))
    }

    @Test
    fun whenRetreiveEntity_thenSumEquals() {
        val entities: List<OrderPlanCalc> = orderPlanCalcRepository
            .findAllByOrderByTsDescIdDesc()
        println("entities: ${entities}")
        assertNotNull(entities)
        assertEquals(entities[0].sum, 225.0)
    }

}



