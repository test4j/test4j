package org.test4j.hamcrest.diff;

import lombok.Data;

import java.util.*;

@Data
public class MatchItem {
    private final int index;
    /**
     * 匹配项
     */
    private Integer expected;
    /**
     * 未匹配项，及不匹配信息
     */
    private final Map<Integer, DiffMap> diffItem;

    public MatchItem(int index) {
        this.index = index;
        this.diffItem = new HashMap<>();
    }

    public MatchItem add(int expected, DiffMap itemDiff) {
        if (itemDiff.diff == 0) {
            this.expected = expected;
            this.diffItem.clear();
        } else {
            this.diffItem.put(expected, itemDiff);
        }
        return this;
    }

    /**
     * 移除已经被匹配的项
     *
     * @param matchedSet
     */
    public void remove(Set<Integer> matchedSet) {
        for (Integer matched : matchedSet) {
            this.diffItem.remove(matched);
        }
    }
}
