# Specifications

JPA’s Criteria API lets you build queries programmatically. Spring Data JPA Specification provides a small, focused API to express predicates over entities and reuse them across repositories. Based on the concept of a specification from Eric Evans' book “Domain Driven Design”, specifications follow the same semantics providing an API to define criteria using JPA. To support specifications, you can extend your repository interface with the JpaSpecificationExecutor interface, as follows:

JPA ning

```java
public interface CustomerRepository extends CrudRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
}
```