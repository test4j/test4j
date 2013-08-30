package org.jtester.junit;

import java.util.Iterator;

import org.test4j.module.ICore.DataIterator;

public class DataCase {
	public static Iterator<Object[]> dataWithParameter() {
		return new DataIterator() {
			{
				data("darui.wu");
				data("jobs.he");
			}
		};
	}
}
