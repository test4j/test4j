package org.jtester.hamcrest.iassert.object.intf;

import java.io.File;

import org.jtester.hamcrest.iassert.common.intf.IBaseAssert;

/**
 * 文件类型对象断言接口
 * 
 * @author darui.wudr
 * 
 */
public interface IFileAssert extends IBaseAssert<File, IFileAssert> {
	/**
	 * 断言指定的文件已经存在
	 * 
	 * @return
	 */
	IFileAssert isExists();

	/**
	 * 断言指定的文件不存在
	 * 
	 * @return
	 */
	IFileAssert unExists();

	/**
	 * 断言文件名称等于期望值
	 * 
	 * @param expected
	 * @return
	 */
	IFileAssert nameEq(String expected);

	/**
	 * 断言文件名称包含期望值
	 * 
	 * @param expected
	 * @return
	 */
	IFileAssert nameContain(String expected);
}
