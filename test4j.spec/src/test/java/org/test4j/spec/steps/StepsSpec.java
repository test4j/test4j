package org.test4j.spec.steps;

import org.test4j.junit.JSpec;
import org.test4j.spec.SharedData;
import org.test4j.spec.annotations.Mix;

/**
 * @author darui.wudr 2013-6-3 下午7:06:39
 */
@Mix({ StepsDemo1.class, StepsDemo2.class })
public class StepsSpec extends JSpec {

    @Override
    protected void initSharedData() {
        this.shared = new MyName();
    }

    public static class MyName implements SharedData {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
