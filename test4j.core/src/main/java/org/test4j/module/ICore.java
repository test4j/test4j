package org.test4j.module;

import org.test4j.hamcrest.TheStyleAssertion;
import org.test4j.hamcrest.WantStyleAssertion;
import org.test4j.module.jmockit.IMockict;
import org.test4j.tools.commons.Reflector;
import org.test4j.tools.datagen.AbastractDataGenerator;
import org.test4j.tools.datagen.AbstractDataMap;
import org.test4j.tools.datagen.DataProviderIterator;

@SuppressWarnings("serial")
public interface ICore extends IMockict {
    final WantStyleAssertion want      = new WantStyleAssertion();

    final TheStyleAssertion  the       = new TheStyleAssertion();

    final Reflector          reflector = Reflector.instance;

    /**
     * 数据生成器<br>
     * index计数从0开始
     * 
     * @author darui.wudr
     */
    public static abstract class DataGenerator extends AbastractDataGenerator {
    }

    public static class DataMap extends AbstractDataMap {
    }

    public static class DataIterator extends DataProviderIterator<Object> {
    }
}
