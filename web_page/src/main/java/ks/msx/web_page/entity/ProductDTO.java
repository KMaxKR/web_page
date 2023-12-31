package ks.msx.web_page.entity;

import ks.msx.web_page.entity.product_type.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private String name;
    private String description;
    private double price;
    private String img = "";
    private String type;
}
