package org.jtester.hamcrest.iassert.common.impl;

import org.jtester.hamcrest.iassert.common.intf.IAssert;

/**
 * all assert statements have been implemented by this class <br/>
 * (or it't super classes)
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings("rawtypes")
public class AllAssert<T, E extends IAssert> extends ListAssert<T, E> implements IAssert<T, E> {

	public AllAssert(Class<? extends IAssert> clazE) {
		super(clazE);
	}

	public AllAssert(T value, Class<? extends IAssert> clazE) {
		super(value, clazE);
	}
}
