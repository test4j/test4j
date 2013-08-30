package org.jtester.hamcrest.iassert.common.intf;

import java.util.Calendar;

import org.jtester.hamcrest.iassert.object.intf.IArrayAssert;
import org.jtester.hamcrest.iassert.object.intf.IBooleanAssert;
import org.jtester.hamcrest.iassert.object.intf.IByteAssert;
import org.jtester.hamcrest.iassert.object.intf.IDateAssert;

@SuppressWarnings("rawtypes")
public interface ITypeAssert<T, E extends IAssert> extends IAssert<T, E> {
	/**
	 * 断言对象是数字类型，并将断言器转换成数组断言器
	 * 
	 * @param claz
	 * @return
	 */
	IArrayAssert typeIsArray();

	IBooleanAssert typeIsBool();

	IByteAssert typeIsByte();

	IDateAssert<Calendar> typeIsCalendar();

	// TODO ...
}
