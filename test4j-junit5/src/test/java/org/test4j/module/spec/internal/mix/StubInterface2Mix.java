package org.test4j.module.spec.internal.mix;

import org.test4j.mock.Mock;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;
import org.test4j.module.spec.internal.stub.StubInterface2Stub;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @author darui.wu
 * @create 2019/11/15
 */
public class StubInterface2Mix implements IMix {

    @Step("你的方法描述, 输入{1}, 输入{2}")
    public void demo(int input1, int input2) {
        new StubInterface2Stub() {
            @Mock
            @Override
            public <E> Set<? super BigDecimal> function3(Class<E> arg1) {
                //具体mock实现
                return null;
            }
        };
    }
}