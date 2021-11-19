package com.demo.pagination

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class PaginationControllerTest(
    @Autowired val mockMvc: MockMvc
) {

    @Test
    fun sameQuery() {
        mockMvc.get("/same") { param("size", "10") }
            .andExpect { jsonPath("totalElements") { value("10") } }
    }

    @Test
    fun copyQuery() {
        mockMvc.get("/copy") { param("size", "10") }
            .andExpect { jsonPath("totalElements") { value("100") } }
    }

    @Test
    fun criteria() {
        mockMvc.get("/criteria") { param("size", "10") }
            .andExpect { jsonPath("totalElements") { value("100") } }
    }
}
