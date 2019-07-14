package org.test4j.hamcrest.iassert.intf;

import org.test4j.hamcrest.iassert.interal.IBaseAssert;

/**
 * char类型对象断言接口
 *
 * @author darui.wudr
 */
public interface ICharacterAssert
        extends
        IBaseAssert<Character, ICharacterAssert> {
    /**
     * @param ch
     * @return
     */
    ICharacterAssert is(char ch);
}
