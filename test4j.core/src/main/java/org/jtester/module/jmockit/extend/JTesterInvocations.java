package org.jtester.module.jmockit.extend;


public interface JTesterInvocations {
    /**
     * mock返回对象值
     * 
     * @param value
     */
    void thenReturn(Object value);

    /**
     * mock依次返回的对象值
     * 
     * @param value
     */
    void thenReturn(Object firstValue, Object... remainingValues);

    //    /**
    //     * @param o
    //     * @return
    //     */
    //    <T> ExpectationsResult when(T obj);
    //
    //    /**
    //     * mock抛出的异常
    //     * 
    //     * @param e
    //     */
    //    void thenThrow(Throwable e);
    //
    //    /**
    //     * mock对象执行操作
    //     * 
    //     * @param delegate
    //     */
    //    void thenDo(Delegate delegate);
    //
    //    /**
    //     * mock方法的参数可以是任意值
    //     * 
    //     * @param claz
    //     * @return
    //     */
    //    <T> T any(Class<T> claz);
    //
    //    /**
    //     * mock方法的参数必须是指定值
    //     * 
    //     * @param value
    //     * @return
    //     */
    //    <T> T is(T value);
}
