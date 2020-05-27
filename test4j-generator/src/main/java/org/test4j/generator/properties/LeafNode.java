package org.test4j.generator.properties;


import lombok.Getter;

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
        return "Property";//TODO
    }
}