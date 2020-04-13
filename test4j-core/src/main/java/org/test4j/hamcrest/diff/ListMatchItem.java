package org.test4j.hamcrest.diff;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * List匹配项
 *
 * @author darui.wu
 */
@Data
public class ListMatchItem {
    /**
     * 实际数据项
     */
    private final int actualIndex;
    /**
     * 匹配的期望项
     */
    private Integer expectIndex;
    /**
     * 未匹配项，及不匹配信息
     */
    private final Map<Integer, DiffMap> diffItem;
    /**
     * 已匹配项
     */
    private Set<Integer> hasMatchedIndex;

    public ListMatchItem(int actualIndex, Set<Integer> hasMatchedIndex) {
        this.actualIndex = actualIndex;
        this.hasMatchedIndex = hasMatchedIndex;
        this.diffItem = new HashMap<>();
    }

    /**
     * 添加匹配项
     *
     * @param expected
     * @param itemDiff
     * @return true：添加的是匹配项
     */
    public boolean addMatched(int expected, DiffMap itemDiff) {
        if (itemDiff.diff == 0) {
            this.expectIndex = expected;
            this.diffItem.clear();
            this.hasMatchedIndex.add(expected);
            return true;
        } else {
            this.diffItem.put(expected, itemDiff);
            return false;
        }
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

    /**
     * 是否是已匹配项
     *
     * @param index
     * @return
     */
    public boolean hasMatched(int index) {
        return this.hasMatchedIndex.contains(index);
    }
}