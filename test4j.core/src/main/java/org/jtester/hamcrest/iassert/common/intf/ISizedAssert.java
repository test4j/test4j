package org.jtester.hamcrest.iassert.common.intf;

@SuppressWarnings("rawtypes")
public interface ISizedAssert<E extends IAssert> {
	/**
	 * want: the size of map(collection/array) item is equal to the number size.<br>
	 * same as {@link #sizeEq(int)} <br>
	 * <br>
	 * 数组长度或collection中元素个数等于期望值
	 * 
	 * @param size
	 *            期望值
	 * @return
	 */
	E sizeIs(int size);

	/**
	 * want: the size of map(collection/array) item is equal to the number size.<br>
	 * same as {@link #sizeIs(int)}<br>
	 * <br>
	 * 数组长度或collection中元素个数等于期望值
	 * 
	 * @param size
	 *            期望值
	 * @return
	 */
	E sizeEq(int size);

	/**
	 * want: the size of map(collection/array) item is greater than the number
	 * size.<br>
	 * <br>
	 * 数组长度或collection中元素个数大于期望值
	 * 
	 * @param size
	 *            期望值
	 * @return
	 */
	E sizeGt(int size);

	/**
	 * want: the size of map(collection/array) item is greater than or equal to
	 * the number size.<br>
	 * <br>
	 * 数组长度或collection中元素个数大于等于期望值
	 * 
	 * @param size
	 *            期望值
	 * @return
	 */
	E sizeGe(int size);

	/**
	 * want: the size of map(collection/array) item is less than the number
	 * size.<br>
	 * <br>
	 * 数组长度或collection中元素个数小于期望值
	 * 
	 * @param size
	 *            期望值
	 * @return
	 */
	E sizeLt(int size);

	/**
	 * want: the size of map(collection/array) item is less than or equal to the
	 * number size.<br>
	 * <br>
	 * 数组长度或collection中元素个数小于等于期望值
	 * 
	 * @param size
	 *            期望值
	 * @return
	 */
	E sizeLe(int size);

	/**
	 * want: the size of map(collection/array) item is greater than or equal to
	 * the number min, and is less than or equal to the number max.<br>
	 * <br>
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	E sizeBetween(int min, int max);

	/**
	 * want: the size of map(collection/array) item isn't equal to the number
	 * size.<br>
	 * <br>
	 * 数组长度或collection中元素个数不等于期望值
	 * 
	 * @param size
	 *            期望值
	 * @return
	 */
	E sizeNe(int size);
}
