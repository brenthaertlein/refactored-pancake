# Spring Boot 2.3 Pagination Demo

This demo application showcases a weird behavior when using `Pageable` and `Query` in Spring Data Mongo present in
Spring Boot 2.3 but not in Spring Boot 1.5

Ultimately, the behavior of `MongoTemplate.count` (the default implementation of `MongoOperations.count` used by Spring
Data Mongo) has changed and now respects the `limit` and `skip` on the `Query` provided.

Since `Query.with(Pageable)`updates fields _on the existing instance_ of `Query`, calling `query.with(pageable)` to
append the `limit` and `offset` from the `Pageable` for `MongoOperations.find`, using the same `Query` instance
for `MongoOperations.count` will now respect the `limit` on the `Pageable` and no longer give an accurate count of the
entire collection (for the given _filter_ part of the `Query`).

Fortunately, a new static method `Query.of` allows duplicating a `Query` as a new instance, which wasn't easily done in
Spring Boot 1. Alternatively, it is still acceptable to construct a `Query` from a `CriteriaDefinition` where
appropriate and use the static method `Query.query(CriteriaDefinition)`  (or the `Query` constructor) to construct
separate instances of a `Query`when executing a `MongoOperations.count` with the same filter used for
a `MongoOperations.find`, which is required for pagination.
