package org.test4j.example.stub;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * @Descriotion:
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2019/11/15.
 */
public interface StubInterface2 {

    <E extends Object> Set<? super BigDecimal> function3(Class<E> klass);

    <T extends Serializable & Comparable, F extends Date> F newDate(Class<? super T> obj);
}
