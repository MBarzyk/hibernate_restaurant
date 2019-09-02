package com.javagda25.hibernate_restaurant;

import com.javagda25.hibernate_restaurant.utils.IEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invoice implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    private LocalDateTime dateOfCreation;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false, columnDefinition = " tinyint default 0")
    private boolean ifPaid;

    private LocalDateTime dateOfRelease;
    private LocalDateTime dateOfPayment;

    @Formula(value = "(SELECT SUM(p.price * p.stock + (p.price * p.stock * p.tax)) from product p where p.invoice_id = id)")
    private Double billValue;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER)
    private List<Product> productList;

}
