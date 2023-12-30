package ks.msx.web_page.entity.product_type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Type {
    TYPE1("TYPE1"),
    TYPE2("TYPE2");

    @Getter
    public final String getType;
}
