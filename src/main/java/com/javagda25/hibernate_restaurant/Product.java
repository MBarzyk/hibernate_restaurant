package com.javagda25.hibernate_restaurant;

import com.javagda25.hibernate_restaurant.utils.IEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private double price;
    private double tax;
    private int stock;

    @ToString.Exclude
    @ManyToOne()
    private Invoice invoice;
}
