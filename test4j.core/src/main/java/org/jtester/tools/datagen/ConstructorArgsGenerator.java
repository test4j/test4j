package org.jtester.tools.datagen;

import java.lang.reflect.Constructor;

/**
 * 构造函数参数值生成器
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings("rawtypes")
public interface ConstructorArgsGenerator {
	public Object[] generate(Constructor constructor);
}
