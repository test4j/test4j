package org.jtester.hamcrest.iassert.object.intf;

import org.jtester.hamcrest.iassert.common.intf.IBaseAssert;
import org.jtester.hamcrest.iassert.common.intf.IComparableAssert;
import org.jtester.hamcrest.matcher.string.StringMode;

/**
 * 字符串对象断言接口
 * 
 * @author darui.wudr
 * 
 */
public interface IStringAssert extends IBaseAssert<String, IStringAssert>, IComparableAssert<String, IStringAssert> {
	/**
	 * 期望2个字符串在modes模式下是相等的<br>
	 * same as "isEqualTo(...)"
	 * 
	 * @param expected
	 * @param modes
	 * @return
	 */
	IStringAssert eq(String expected, StringMode... modes);

	/**
	 * 期望2个字符串在modes模式下是相等的<br>
	 * same as "eq(...)"
	 * 
	 * @param expected
	 * @param modes
	 * @return
	 */
	IStringAssert isEqualTo(String expected, StringMode... modes);

	/**
	 * 断言字符串包含期望的字串${expected}
	 * 
	 * @param expected
	 *            期望的字串
	 * @param modes
	 *            字符串预处理模式
	 * @return
	 */
	IStringAssert contains(String expected, StringMode... modes);

	/**
	 * 断言字符串包含指定的若干个子字符串
	 * 
	 * @param expecteds
	 *            期望的字符串组
	 * @param modes
	 *            字符串处理模式
	 * @return
	 */
	IStringAssert contains(String[] expecteds, StringMode... modes);

	/**
	 * 断言字符串中依次包含字串列表expecteds
	 * 
	 * @param expected
	 * @return
	 */
	IStringAssert containsInOrder(String... expecteds);

	/**
	 * 断言字符串中依次包含若干特定模式的子字符串
	 * 
	 * @param expecteds
	 * @param modes
	 * @return
	 */
	IStringAssert containsInOrder(String[] expecteds, StringMode... modes);

	/**
	 * 断言字符串以${expected}子串结尾
	 * 
	 * @param expected
	 *            期望的字串
	 * @param modes
	 *            字符串预处理模式
	 * @return
	 */
	IStringAssert end(String expected, StringMode... modes);

	/**
	 * 断言字符串以${expected}子串开头
	 * 
	 * @param expected
	 *            期望的字串
	 * @param modes
	 *            字符串预处理模式
	 * @return
	 */
	IStringAssert start(String expected, StringMode... modes);

	/**
	 * 断言字符串不包含指定的子字符串
	 * 
	 * @param sub
	 * @param modes
	 *            字符串预处理模式
	 * @return
	 */
	IStringAssert notContain(String sub, StringMode... modes);

	/**
	 * 断言字符串不包含指定的子字符串
	 * 
	 * @param subs
	 * @param modes
	 * @return
	 */
	IStringAssert notContain(String[] subs, StringMode... modes);

	/**
	 * 断言字符串符合正则表达式${regex}
	 * 
	 * @param regex
	 *            期望的正则表达式
	 * @return
	 */
	IStringAssert regular(String regex);

	/**
	 * 断言字符串在忽略大小写的情况下等于期望值
	 * 
	 * @param string
	 *            期望值
	 * @return
	 */
	IStringAssert eqIgnoreCase(String string);

	/**
	 * 忽略字符串中所有的空白符情况下相等
	 * 
	 * @param string
	 * @return
	 */
	IStringAssert eqIgnoreSpace(String string);

	/**
	 * 字符串中忽略连续的空白串情况下相等<br>
	 * 即空白符或连续的空白符当作一个空格符处理
	 * 
	 * @param string
	 *            期望值
	 * @return
	 */
	IStringAssert eqWithStripSpace(String string);

	/**
	 * 断言字符串不是空白串
	 * 
	 * @return
	 */
	IStringAssert notBlank();
}
