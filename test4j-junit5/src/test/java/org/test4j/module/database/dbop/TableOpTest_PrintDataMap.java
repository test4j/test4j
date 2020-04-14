package org.test4j.module.database.dbop;

import org.junit.jupiter.api.Test;
import org.test4j.db.datamap.table.UserTableMap;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.junit5.Test4J;
import org.test4j.tools.datagen.TableData;

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
        String text = db.table(t_user).printAsDataMap(null, UserTableMap.class.getSimpleName());
        want.string(text).eq(
                "UserTableMap.create(4)" +
                        "   .user_name.values('name1', 'name2', 'name3', 'name4')" +
                        "   .id.values('1', '2', '3', '4')" +
                        "   .age.values(null, '3', '5', null)"
                , StringMode.IgnoreSpace, StringMode.SameAsQuato);

        text = db.table(t_user).printAsMulMap(null, UserTableMap.class.getSimpleName());
        want.string(text).eq(
                "UserTableMap.create(1)" +
                        "    .user_name.values('name1').id.values('1')," +
                        "UserTableMap.create(1)" +
                        "    .user_name.values('name2').id.values('2').age.values('3')," +
                        "UserTableMap.create(1)" +
                        "    .user_name.values('name3').id.values('3').age.values('5')," +
                        "UserTableMap.create(1)" +
                        "    .user_name.values('name4').id.values('4')"
                , StringMode.SameAsQuato, StringMode.IgnoreSpace
        );
    }

    @Test
    void printAsJson() {
        DataMap datas = UserTableMap.create(4)
                .id.autoIncrease()
                .user_name.values("name1", "name2", "name3", "name4")
                .age.values(null, 3, 5, null)
                .first_name.values("first1", "first2")
                .last_name.values(null, "last2", "last3", "last4");
        db.table(t_user).clean().insert(datas);
        String text = db.table(t_user).printAsJson(null, "first_name", "last_name", "user_name");
        want.string(text).eq(
                "{'t_user': [" +
                        "    {'id': '1', 'first_name': 'first1', 'user_name': 'name1'}," +
                        "    {'id': '2', 'first_name': 'first2', 'last_name': 'last2','user_name': 'name2', 'age': '3'}," +
                        "    {'id': '3', 'first_name': 'first2', 'last_name': 'last3', 'user_name': 'name3', 'age': '5'}," +
                        "    {'id': '4', 'first_name': 'first2', 'last_name': 'last4', 'user_name': 'name4'}" +
                        "]}"
                , StringMode.SameAsQuato, StringMode.IgnoreSpace
        );
        db.insert(TableData.fromText(text), true);
        db.table(t_user).query().eqReflect(datas);
    }

    @Test
    void insert_column_is_json() {
        db.insert(TableData.fromFile(this.getClass(), "TableOpTest_PrintDataMap-column_is_json.json"), true);
        db.table(t_user).printAndAssert(null);
        db.table(t_user).query().eqReflect(DataMap.create(4)
                .kv("user_name", "{\"name1\":\"ttt\"}")
        );
    }
}