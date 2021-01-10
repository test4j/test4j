package org.test4j.module.database.dbop;

import org.junit.jupiter.api.Test;
import org.test4j.Test4J;
import org.test4j.db.dm.UserDataMap;
import org.test4j.asserts.matcher.string.StringMode;

import org.test4j.tools.commons.ResourceHelper;
import org.test4j.module.database.datagen.TableMap;

import java.io.FileNotFoundException;

import static org.test4j.db.ITable.t_user;

/**
 * @Descriotion:
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2020/4/9.
 */
class TableOpTest_PrintDataMap implements Test4J {

    @Test
    void printAsDataMap() {
        db.table(t_user).clean().insert(UserDataMap.table(4)
            .id.autoIncrease()
            .userName.values("name1", "name2", "name3", "name4")
            .age.values(null, 3, 5, null)
        );
        String text = db.printAsDataMap(t_user, UserDataMap.class.getSimpleName());
        want.string(text).eq(
            "UserDataMap.create(4)" +
                "   .user_name.values('name1', 'name2', 'name3', 'name4')" +
                "   .id.values('1', '2', '3', '4')" +
                "   .age.values(null, '3', '5', null)"
            , StringMode.IgnoreSpace, StringMode.SameAsQuato);

        text = db.printAsDataMap("t_user where 1=1", UserDataMap.class.getSimpleName());
        want.string(text).eq(
            "UserDataMap.create(4)" +
                "   .user_name.values('name1', 'name2', 'name3', 'name4')" +
                "   .id.values('1', '2', '3', '4')" +
                "   .age.values(null, '3', '5', null)"
            , StringMode.IgnoreSpace, StringMode.SameAsQuato);
    }

    @Test
    void printAsJson() throws FileNotFoundException {
        DataMap datas = UserDataMap.table(4)
            .id.autoIncrease()
            .userName.values("name1", "name2", "name3", "name4")
            .age.values(null, 3, 5, null)
            .firstName.values("first1", "first2")
            .lastName.values(null, "last2", "last3", "last4");
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