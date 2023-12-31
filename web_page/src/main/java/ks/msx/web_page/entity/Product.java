package ks.msx.web_page.entity;

import jakarta.persistence.*;
import ks.msx.web_page.entity.product_type.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product_menu")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "img")
    private String img;  //link to img

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;
}
