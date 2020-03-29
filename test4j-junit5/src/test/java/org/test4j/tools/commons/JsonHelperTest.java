package org.test4j.tools.commons;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.test4j.exception.Test4JException;
import org.test4j.junit5.Test4J;
import org.test4j.model.Manager;

public class JsonHelperTest extends Test4J {
    private static String tempDir = System.getProperty("java.io.tmpdir");

    @Test
    public void toDat() {
        String filename = tempDir + "/manager.dat";
        JSONHelper.toDatFile(Manager.mock(), filename);
        want.file(filename).isExists();
    }

    @Test
    public void fromDat() {
        String filename = "file:" + System.getProperty("user.dir")
                + "/../test4j-btest/src/main/resources/org/test4j/tools/commons/manager.dat";
        want.file(filename).isExists();

        Manager manager = JSONHelper.fromDatFile(Manager.class, filename);
        want.object(manager).eqByProperties("name", "Tony Tester");
    }

    @Test
    public void fromDat_Classpath() {
        String filename = "org/test4j/tools/commons/manager.dat";
        Manager manager = JSONHelper.fromDatFile(Manager.class, filename);
        want.object(manager).eqByProperties("name", "Tony Tester")
                .eqByProperties("phoneNumber.number", "0571-88886666");
        want.date(manager.getDate()).isYear(2009).isMonth("08").isHour(16);
    }

    @Test
    public void toDat_List() {
        String filename = tempDir + "/managers.dat";
        List<?> list = Arrays.asList(Manager.mock(), Manager.mock());
        JSONHelper.toDatFile(list, filename);
        want.file(filename).isExists();
    }

    @Test
    public void fromDat_List() {
        String filename = "classpath:org/test4j/tools/commons/manager_classnotfound.dat";
        want.exception(() ->
                        JSONHelper.fromDatFile(List.class, filename)
                , Test4JException.class)
                .contains("org.jtester.utility.beans.Manager");
    }

    @Test
    public void toJSON() {
        String filename = tempDir + "/manager.json";
        // want.file(filename).unExists();
        JSONHelper.toJsonFile(Manager.mock(), filename);
        want.file(filename).isExists();
    }

    @Test
    public void fromJSON() throws IOException {
        String filename = "file:" + System.getProperty("user.dir")
                + "/../test4j-btest/src/main/resources/org/test4j/tools/commons/manager.json";
        want.file(filename).isExists();

        Manager manager = JSONHelper.fromJsonFile(Manager.class, filename);
        want.object(manager).eqByProperties("name", "Tony Tester");
    }

    @Test
    public void fromJSON_Classpath() {
        String filename = "classpath:org/test4j/tools/commons/manager.json";
        Manager manager = JSONHelper.fromJsonFile(Manager.class, filename);
        want.object(manager).eqByProperties("name", "Tony Tester")
                .eqByProperties("phoneNumber.number", "0571-88886666");
        want.date(manager.getDate()).isYear(2009).isMonth("08").isHour(16);
    }

    @Test
    public void fromJSON_Classpath2() {
        Manager manager = JSONHelper.fromJsonFile(Manager.class, JsonHelperTest.class, "manager.json");
        want.object(manager).eqByProperties("name", "Tony Tester")
                .eqByProperties("phoneNumber.number", "0571-88886666");
        want.date(manager.getDate()).isYear(2009).isMonth("08").isHour(16);
    }

    @Test
    public void toJSON_List() {
        String filename = tempDir + "/managers.xml";
        List<Manager> list = new ArrayList<Manager>();
        list.add(Manager.mock());
        list.add(Manager.mock());
        JSONHelper.toJsonFile(list, filename);
        want.file(filename).isExists();
    }

    @Test
    public void fromJSON_List() throws Exception {
        String filename = "classpath:org/test4j/tools/commons/managers.json";
        List<?> managers = JSONHelper.fromJsonFile(ArrayList.class, filename);
        want.collection(managers).sizeEq(2)
                .eqByProperties("name", new String[]{"Tony Tester", "Tony Tester"});
    }

    @Test
    public void fromJSON_List2() {
        String filename = "classpath:org/test4j/tools/commons/managers2.json";
        List<?> managers = JSONHelper.fromJsonFile(ArrayList.class, filename);
        want.collection(managers).sizeEq(2)
                .eqByProperties("name", new String[]{"Tony Tester", "Tony Tester"});
    }

    @Test
    public void getPojoToXml_Array() {
        Manager[] managers = new Manager[2];
        managers[0] = Manager.mock();
        managers[1] = Manager.mock();
        JSONHelper.toJsonFile(managers, tempDir + "/managers-array.xml");
    }

    @Test
    public void fromJSON_Array() {
        Manager[] managers = JSONHelper.fromJsonFile(Manager[].class,
                "classpath:org/test4j/tools/commons/managers.json");
        want.array(managers).sizeEq(2)
                .eqByProperties("name", new String[]{"Tony Tester", "Tony Tester"});
    }
}
