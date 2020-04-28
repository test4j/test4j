package org.test4j.module.database.utility;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.module.core.utility.MessageHelper;

class DBHelperTest extends Test4J {

    @Test
    void buildH2Unique() {
        String sql = DBHelper.buildH2Unique("teble2", "field1","field2");
        MessageHelper.info(sql);
        want.string(sql.trim()).eq("ALTER TABLE teble2 ADD CONSTRAINT UNIQ_TEBLE2_FIELD1_FIELD2 UNIQUE(field1,field2);");
    }

    @Test
    void buildH2Index() {
        String sql = DBHelper.buildH2Index("teble1", "field1","field2");
        MessageHelper.info(sql);
        want.string(sql.trim()).eq("CREATE INDEX IDX_TEBLE1_FIELD1_FIELD2 ON teble1(field1,field2);");
    }
}