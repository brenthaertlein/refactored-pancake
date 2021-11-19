package com.demo.pagination

import javax.annotation.PostConstruct
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.repository.support.PageableExecutionUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PaginationController(
    private val mongoOperations: MongoOperations
) {

    @PostConstruct
    fun postConstruct() {
        mongoOperations.insertAll((1..100).map { Foo() })
    }

    /**
     * This method will incorrectly produce a `totalElements` value matching the size of the results produced by `MongoOperations.find`
     */
    @GetMapping("/same")
    fun sameQuery(pageable: Pageable): Page<Foo> =
        Query()
            .let {
                PageableExecutionUtils.getPage(mongoOperations.find(it.with(pageable), Foo::class.java), pageable) {
                    mongoOperations.count(it, Foo::class.java)
                }
            }

    /**
     * This method produces the correct `Page.totalElements` value because it uses `Query.of` to copy the attributes of the source `Query`
     * to a new instance
     */
    @GetMapping("/copy")
    fun copyQuery(pageable: Pageable): Page<Foo> =
        Query()
            .let {
                PageableExecutionUtils.getPage(mongoOperations.find(Query.of(it).with(pageable), Foo::class.java), pageable) {
                    mongoOperations.count(it, Foo::class.java)
                }
            }

    /**
     * This method produces the correct `Page.totalElements` value because it uses `Query.query(CriteriaDefinition)` (or the `Query`
     * constructor) to create distinct `Query` instances for each `MongoOperations` call
     */
    @GetMapping("/criteria")
    fun criteria(pageable: Pageable): Page<Foo> =
        Criteria()
            .let {
                PageableExecutionUtils.getPage(mongoOperations.find(Query.query(it).with(pageable), Foo::class.java), pageable) {
                    mongoOperations.count(Query(it), Foo::class.java)
                }
            }
}
