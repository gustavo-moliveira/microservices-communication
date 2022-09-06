package com.mscommunication.productapi.modules.produto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "fk_supplier", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "fk_category", nullable = false)
    private Category category;
}
