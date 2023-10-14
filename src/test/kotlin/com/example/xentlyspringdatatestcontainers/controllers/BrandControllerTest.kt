package com.example.xentlyspringdatatestcontainers.controllers

import com.example.xentlyspringdatatestcontainers.BrandService
import com.example.xentlyspringdatatestcontainers.exceptions.BrandNotFoundException
import com.example.xentlyspringdatatestcontainers.models.Brand
import org.hamcrest.Matchers
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@RunWith(SpringRunner::class)
@WebMvcTest(BrandController::class)
class BrandControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var service: BrandService

    @MockBean
    lateinit var assembler: BrandAssembler

    @Nested
    inner class Save {
        @Test
        fun `single item`() {
            val brands = listOf(
                Brand.View(name = "Example", slug = "example"),
            )
            `when`(assembler.toModel(any()))
                .thenCallRealMethod()
            `when`(service.save(anyList()))
                .thenReturn(brands)

            mvc.perform(
                post("/api/v1/brands")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                          {
                             "name": "Example"
                          }
                        """.trimIndent(),
                    ),
            ).andExpectAll(
                status().isCreated,
                header().string(HttpHeaders.LOCATION, Matchers.endsWith("/brands/example")),
                jsonPath("$.name", Matchers.equalTo("Example")),
                jsonPath("$.slug", Matchers.equalTo("example")),
                jsonPath(
                    "$._links.brands.href",
                    Matchers.allOf(Matchers.startsWith("http"), Matchers.containsString("/brands"))
                ),
                jsonPath(
                    "$._links.self.href",
                    Matchers.allOf(Matchers.startsWith("http"), Matchers.endsWith("/brands/example"))
                ),
            )
        }

        @Test
        fun `in bulk`() {
            val brands = listOf(
                Brand.View(name = "Asus", slug = "asus"),
                Brand.View(name = "Nivea", slug = "nivea"),
            )
            `when`(assembler.toModel(any()))
                .thenCallRealMethod()
            `when`(assembler.toCollectionModel(anyIterable()))
                .thenCallRealMethod()
            `when`(service.save(anyList()))
                .thenReturn(brands)

            mvc.perform(
                post("/api/v1/brands/bulk")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                          [
                            {
                              "name": "Asus"
                            },
                            {
                              "name": "Nivea"
                            }
                          ]
                        """.trimIndent(),
                    ),
            ).andExpectAll(
                status().isCreated,
                header().doesNotExist(HttpHeaders.LOCATION),
                jsonPath("$._embedded.views.size()", Matchers.equalTo(2)),
            )
        }
    }

    @Nested
    @DisplayName("get by slug")
    inner class GetSingle {
        @Test
        fun `if item is found`() {
            val brand = Brand.View(name = "Example", slug = "example")
            `when`(assembler.toModel(brand))
                .thenCallRealMethod()
            `when`(service.get("example"))
                .thenReturn(brand)

            mvc.get("/api/v1/brands/example")
                .andExpect {
                    status {
                        isOk()
                    }
                }
                .andExpect {
                    jsonPath("$.name", Matchers.equalTo("Example"))
                    jsonPath("$.slug", Matchers.equalTo("example"))
                    jsonPath(
                        "$._links.brands.href",
                        Matchers.allOf(Matchers.startsWith("http"), Matchers.containsString("/brands"))
                    )
                    jsonPath(
                        "$._links.self.href",
                        Matchers.allOf(Matchers.startsWith("http"), Matchers.endsWith("/brands/example"))
                    )
                }
        }

        @Test
        fun `if item is not found`() {
            val brand = Brand.View(name = "Example", slug = "example")
            `when`(assembler.toModel(brand))
                .thenCallRealMethod()
            `when`(service.get("example"))
                .thenThrow(BrandNotFoundException::class.java)

            mvc.get("/api/v1/brands/example")
                .andExpect {
                    status {
                        isNotFound()
                    }
                }
        }
    }

    @Nested
    @DisplayName("get filtered by an optional query")
    inner class GetMultiple {
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = ["example", "        "])
        fun `if item is found`(query: String?) {
            val brand = Brand.View(name = "Example", slug = "example")
            val page: Page<Brand.View> = PageImpl(listOf(brand))
            `when`(service.get(any(), any()))
                .thenReturn(page)
            `when`(assembler.toModel(any()))
                .thenCallRealMethod()
            `when`(assembler.setQuery(anyString()))
                .thenCallRealMethod()
            `when`(assembler.resetQuery())
                .thenCallRealMethod()

            mvc.get("/api/v1/brands?q=$query")
                .andExpect {
                    status {
                        isOk()
                    }
                }
                .andExpect {
                    jsonPath("$._embedded.views.[0].name", Matchers.equalTo("Example"))
                    jsonPath("$._embedded.views.[0].slug", Matchers.equalTo("example"))
                    jsonPath(
                        "$._embedded.views.[0]._links.brands.href",
                        Matchers.allOf(Matchers.startsWith("http"), Matchers.containsString("/brands?q="))
                    )
                    jsonPath(
                        "$._embedded.views.[0]._links.self.href",
                        Matchers.allOf(Matchers.startsWith("http"), Matchers.endsWith("/brands/example"))
                    )
                    jsonPath(
                        "$._links.self.href",
                        Matchers.allOf(Matchers.startsWith("http"), Matchers.containsString("/brands?q=")),
                    )
                    jsonPath(
                        "$.page",
                        Matchers.notNullValue(),
                    )
                }
        }
    }
}