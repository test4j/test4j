/*
 * Copyright 2013 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package org.jtester.spec;

/**
 * 几个Steps之间的共享数据接口类
 * 
 * @author darui.wudr 2013-8-27 下午4:12:46
 */
public interface SharedData {
    /**
     * 空的共享数据
     * 
     * @author darui.wudr 2013-8-28 下午4:07:56
     */
    public static class EmptyData implements SharedData {
    }
}
