package org.test4j.hamcrest.iassert.object.intf;

import org.test4j.hamcrest.iassert.common.intf.IBaseAssert;

/**
 * char类型对象断言接口
 * 
 * @author darui.wudr
 * 
 */
public interface ICharacterAssert extends IBaseAssert<Character, ICharacterAssert> {
	ICharacterAssert is(char ch);
}
