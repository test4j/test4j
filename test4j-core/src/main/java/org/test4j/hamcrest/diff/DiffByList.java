package org.test4j.hamcrest.diff;

import lombok.Getter;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.tools.commons.ArrayHelper;
import org.test4j.tools.commons.ListHelper;
import org.test4j.tools.datagen.IDataMap;

import java.util.*;

/**
 * 按List比较
 *
 * @author darui.wu
 * @create 2020/4/13 10:53 上午
 */
@Getter
public class DiffByList extends BaseDiff {

    public DiffByList(BaseDiff diff) {
        super(diff);
    }

    public DiffByList(EqMode... modes) {
        super(modes);
    }

    /**
     * 队列比较
     *
     * @param parentKey
     * @param actual
     * @param expect
     */
    @Override
    public DiffMap compare(Object parentKey, Object actual, Object expect) {
        if (validateNull(parentKey, actual, expect)) {
            return this.diffMap;
        }
        if (!ArrayHelper.isCollOrArray(actual)) {
            this.diffMap.add(parentKey, "actual should be a List", "expect should be a Map List");
            return this.diffMap;
        }
        List actualList = ListHelper.toList(actual, false);
        List expectList = null;
        if (expect instanceof IDataMap) {
            expectList = ((IDataMap) expect).rows();
        } else if (ArrayHelper.isCollOrArray(expect)) {
            expectList = ListHelper.toList(expect, false);
        } else {
            this.diffMap.add(parentKey, "actual is a List", "expect should be a Map List");
            return this.diffMap;
        }
        int size = expectList.size();
        if (actualList.size() != size) {
            diffMap.add(parentKey, "the size is " + actualList.size(), "the size is " + size);
            return this.diffMap;
        }
        if (ignoreOrder) {
            this.compareIgnoreOrder(parentKey, actualList, expectList);
        } else {
            this.compareWithOrder(parentKey, actualList, expectList);
        }
        return this.diffMap;
    }

    /**
     * 按顺序比较
     *
     * @param parentKey
     * @param actualList
     * @param expectList
     */
    private void compareWithOrder(Object parentKey, List actualList, List expectList) {
        for (int index = 0; index < expectList.size(); index++) {
            String key = parentKey + "[" + index + "]";
            new DiffFactory(this).compare(key, actualList.get(index), expectList.get(index));
        }
    }

    /**
     * 忽略顺序比较
     *
     * @param parentKey
     * @param actualList
     * @param expectList
     * @return
     */
    private void compareIgnoreOrder(Object parentKey, List actualList, List<Map> expectList) {
        Map<Integer, ListMatchItem> all = new HashMap<>();
        int size = expectList.size();
        // 已经被匹配的比较对象
        Set<Integer> matchedExpected = new HashSet<>(size);
        for (int index = 0; index < size; index++) {
            Object actual = actualList.get(index);
            ListMatchItem child = new ListMatchItem(index, matchedExpected);
            this.compareWithExpect(parentKey, actual, expectList, child);
            all.put(index, child);
        }
        Set<Integer> existedDiffIndex = new HashSet<>(size);
        for (Map.Entry<Integer, ListMatchItem> entry : all.entrySet()) {
            ListMatchItem item = entry.getValue();
            if (item.getExpectIndex() != null) {
                continue;
            }
            item.remove(matchedExpected);
            DiffMap minDiff = this.findMinDiff(existedDiffIndex, item);
            diffMap.add(minDiff);
        }
    }

    /**
     * 查找最小差异项
     *
     * @param existedDiffIndex
     * @param item
     * @return
     */
    private DiffMap findMinDiff(Set<Integer> existedDiffIndex, ListMatchItem item) {
        Map.Entry<Integer, DiffMap> minDiff = null;
        Map.Entry<Integer, DiffMap> _default = null;
        for (Map.Entry<Integer, DiffMap> child : item.getDiffItem().entrySet()) {
            if (_default == null) {
                _default = child;
            }
            if (existedDiffIndex.contains(child.getKey())) {
                // 已经被比较的跳过
                continue;
            }
            if (minDiff == null || minDiff.getValue().diffCount() > child.getValue().diffCount()) {
                minDiff = child;
            }
        }
        if (minDiff == null) {
            minDiff = _default;
        }
        existedDiffIndex.add(minDiff.getKey());
        return minDiff.getValue();
    }

    private ListMatchItem compareWithExpect(Object parentKey, Object actual, List<Map> expectList, ListMatchItem child) {
        /**
         * 所有匹配项
         */
        Map<Integer, DiffMap> matched = new HashMap<>(expectList.size());
        for (int loop = 0; loop < expectList.size(); loop++) {
            // 跳过已经被匹配项
            if (child.hasMatched(loop)) {
                continue;
            }
            String key = parentKey + "[" + (loop + 1) + "]~[" + (child.getActualIndex() + 1) + "]";
            // 因为比较信息不能确定, 所以这里需要重新创建比较对象
            DiffMap diff = new DiffFactory(this, new DiffMap())
                    .compare(key, actual, expectList.get(loop));
            if (diff.hasDiff()) {
                child.addMatched(loop, diff);
            } else {
                matched.put(loop, diff);
            }
        }
        // 取忽略项最少的一组匹配值
        DiffMap maxMatched = null;
        Integer maxIndex = null;
        for (Map.Entry<Integer, DiffMap> entry : matched.entrySet()) {
            if (maxMatched == null || maxMatched.getIgnoreCount() > entry.getValue().getIgnoreCount()) {
                maxMatched = entry.getValue();
                maxIndex = entry.getKey();
            }
        }
        if (maxMatched != null) {
            child.addMatched(maxIndex, maxMatched);
        }
        return child;
    }
}