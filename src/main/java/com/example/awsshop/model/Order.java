package com.example.awsshop.model;

//import jakarta.persistence.*;
import lombok.*;
//import org.hibernate.Hibernate;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

//@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Order {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long totalAmount;
    private String shippingAddress;
    private Date orderDate;
    private Date orderFulfillmentDate;
    private OrderStatus orderStatus;
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
//    @ToString.Exclude
//    private Set<Product> price;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
//        Order order = (Order) o;
//        return id != null && Objects.equals(id, order.id);
//    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
