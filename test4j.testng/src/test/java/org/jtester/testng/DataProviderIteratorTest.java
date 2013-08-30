package org.jtester.testng;

import org.jtester.tools.datagen.DataProviderIterator;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class DataProviderIteratorTest extends JTester {

    @Test
    public void testCheckDataLength() {
        try {
            new DataProviderIterator<Integer>() {
                {
                    data(1);
                    data(1, 2);
                }
            };
            want.fail();
        } catch (RuntimeException e) {
            String message = e.getMessage();
            want.string(message).containsInOrder("DataProvider error", "length is 1", "(data index 2)", "length is 2");
        }
    }

    public void testDataEmpty() {
        try {
            new DataProviderIterator<Integer>() {
                {
                    data();
                    data(1, 2);
                }
            };
            want.fail();
        } catch (RuntimeException e) {
            String message = e.getMessage();
            want.string(message).contains("provider data(index 1) error");
        }
    }
}
