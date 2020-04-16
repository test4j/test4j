package org.test4j.module.database.dbop;

import org.junit.jupiter.api.Test;
import org.test4j.db.datamap.table.UserTableMap;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.junit5.Test4J;
import org.test4j.tools.commons.ResourceHelper;
import org.test4j.tools.datagen.TableMap;

import java.io.FileNotFoundException;

import static org.test4j.db.ITable.t_user;

/**
 * @Descriotion:
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2020/4/9.
 */
class TableOpTest_PrintDataMap extends Test4J {

    @Test
    void printAsDataMap() {
        db.table(t_user).clean().insert(UserTableMap.create(4)
                .id.autoIncrease()
                .user_name.values("name1", "name2", "name3", "name4")
                .age.values(null, 3, 5, null)
        );
        String text = db.printAsDataMap(t_user, UserTableMap.class.getSimpleName());
        want.string(text).eq(
                "UserTableMap.create(4)" +
                        "   .user_name.values('name1', 'name2', 'name3', 'name4')" +
                        "   .id.values('1', '2', '3', '4')" +
                        "   .age.values(null, '3', '5', null)"
                , StringMode.IgnoreSpace, StringMode.SameAsQuato);

        text = db.printAsDataMap("t_user where 1=1", UserTableMap.class.getSimpleName());
        want.string(text).eq(
                "UserTableMap.create(4)" +
                        "   .user_name.values('name1', 'name2', 'name3', 'name4')" +
                        "   .id.values('1', '2', '3', '4')" +
                        "   .age.values(null, '3', '5', null)"
                , StringMode.IgnoreSpace, StringMode.SameAsQuato);
    }

    @Test
    void printAsJson() throws FileNotFoundException {
        DataMap datas = UserTableMap.create(4)
                .id.autoIncrease()
                .user_name.values("name1", "name2", "name3", "name4")
                .age.values(null, 3, 5, null)
                .first_name.values("first1", "first2")
                .last_name.values(null, "last2", "last3", "last4");
        db.table(t_user).clean().insert(datas);
        String text = db.printAsJson(new String[]{t_user}, new String[]{"first_name", "last_name", "user_name"});
        String json = ResourceHelper.readFromFile(this.getClass(), "print_as_json.json");
        want.string(text).eq(json, StringMode.SameAsQuato, StringMode.IgnoreSpace);
        db.insert(TableMap.fromText(text), true);
        db.table(t_user).query().eqReflect(datas);
    }

    @Test
    void insert_column_is_json() {
        db.insert(TableMap.fromFile(this.getClass(), "TableOpTest_PrintDataMap-column_is_json.json"), true);
        db.table(t_user).printAndAssert(null);
        db.table(t_user).query().eqReflect(DataMap.create(4)
                .kv("user_name", "{\"name1\":\"ttt\"}")
        );
    }
}