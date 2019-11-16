package org.test4j.module.spec.internal;

import org.junit.jupiter.api.Test;
import org.test4j.example.stub.StubInterface1;
import org.test4j.example.stub.StubInterface2;
import org.test4j.module.ICore;
import org.test4j.module.spec.IMix;

/**
 * @Descriotion:
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2019/11/15.
 */
class StubGeneratorTest implements ICore {

    @Test
    void generate() {
        String src = System.getProperty("user.dir") + "/src/test/java/";
        IMix.stub(src,
                this.getClass().getPackage().getName(),
                StubInterface1.class, StubInterface2.class
        );
        String path = src + this.getClass().getPackage().getName().replace('.', '/');
        want.file(path + "/stub/StubInterface1Stub.java").isExists();
        want.file(path + "/stub/StubInterface2Stub.java").isExists();
        want.file(path + "/mix/StubInterface1Mix.java").isExists();
        want.file(path + "/mix/StubInterface1Mix.java").isExists();
        want.file(path + "/StubMixes.java").isExists();
    }
}