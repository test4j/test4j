package org.test4j.generator.properties;

import com.qxwz.galaxy.config.Property;
import lombok.Getter;

import static com.qxwz.galaxy.property.resolver.ColumbusPropertyResolver.IS_COLUMN_BUS_KEY;
import static com.qxwz.galaxy.property.resolver.NacosPropertyResolver.IS_NACOS_KEY;

/**
 * LeafNode
 *
 * @author darui.wu
 * @create 2020/4/24 6:05 下午
 */
@Getter
final class LeafNode {
    private String fullKey;

    private String value;

    public LeafNode(String fullKey, String value) {
        this.fullKey = fullKey;
        this.value = value.toLowerCase().trim();
    }

    public String getKeyType() {
        if (IS_NACOS_KEY.equals(value) || IS_COLUMN_BUS_KEY.equals(value)) {
            return Property.class.getSimpleName();
        } else {
            return Property.class.getSimpleName();
        }
    }
}