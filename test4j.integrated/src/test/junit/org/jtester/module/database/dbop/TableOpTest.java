package org.jtester.module.database.dbop;

import java.sql.SQLException;
import java.util.Date;

import mockit.Mock;

import org.jtester.database.table.ITable;
import org.jtester.database.table.TddUserTable;
import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.hamcrest.matcher.string.StringMode;
import org.jtester.junit.JTester;
import org.jtester.module.database.annotations.Transactional;
import org.jtester.module.database.annotations.Transactional.TransactionMode;
import org.jtester.module.database.environment.TableMeta.ColumnMeta;
import org.jtester.tools.commons.ExceptionWrapper;
import org.jtester.tools.commons.ListHelper;
import org.jtester.tools.datagen.DataSet;
import org.junit.Test;

@SuppressWarnings({ "serial", "unchecked" })
@Transactional(TransactionMode.COMMIT)
public class TableOpTest implements JTester {

    @Test
    public void testInsertData() throws SQLException {
        db.table(ITable.t_tdd_user).clean().insert(new TddUserTable() {
            {
                this.put(IColumn.f_id, 1);
                this.put(IColumn.f_first_name, "darui.wu");
                this.put(IColumn.f_gmt_created, new Date());
            }
        });
        db.table(ITable.t_tdd_user).query().reflectionEqMap(1, new TddUserTable() {
            {
                this.put(IColumn.f_id, 1);
                this.put(IColumn.f_first_name, "darui.wu");
            }
        });
    }

    @Test
    public void testInsert_ErrorColumnName() {
        try {
            db.table(ITable.t_tdd_user).insert(new DataSet() {
                {
                    this.data("{'id':1,'my_name':'darui.wu'}");
                }
            });
            want.fail();
        } catch (Exception e) {
            String message = ExceptionWrapper.toString(e);
            want.string(message).contains("can't find column[my_name] field in table");
        }
    }

    @Test
    public void testAdd() {
        db.table(ITable.t_tdd_user).clean().insert(new DataSet() {
            {
                data("{'id':1, 'first_name':'name1'}");
                data(2, new TddUserTable() {
                    {
                        this.put(IColumn.f_id, new int[] { 2, 3 });
                        this.put(IColumn.f_first_name, new String[] { "darui.wu", "jobs.he" });
                    }
                });
            }
        });
        db.queryAsPoJo("select count(*) from tdd_user", Integer.class).isEqualTo(3L);
    }

    @Test
    public void testInsert_NoSuchColumn() throws SQLException {
        try {
            new TableOp(ITable.t_tdd_user).insert(new TddUserTable() {
                {
                    this.put("no_column", "darui.wu");
                    this.put(IColumn.f_gmt_created, new Date());
                }
            });
            want.fail();
        } catch (Exception e) {
            String message = e.getMessage();
            want.string(message).contains("no_column");
        }
    }

    @Test
    public void testInsert_BadType() throws SQLException {
        try {
            new TableOp(ITable.t_tdd_user).insert(new TddUserTable() {
                {
                    this.put(IColumn.f_first_name, "darui.wu");
                    this.put(IColumn.f_gmt_created, "2011-08-19ss");
                }
            });
            want.fail();
        } catch (Exception e) {
            String message = e.getMessage();
            want.string(message).contains("2011-08-19ss");
        }
    }

    @Test
    public void testInsert_DataIterator() {
        new TableOp(ITable.t_tdd_user).clean().insert(2, new TddUserTable() {
            {
                this.put(IColumn.f_id, new Integer[] { 1, 2 });
                this.put(IColumn.f_first_name, new Object[] { "darui.wu", "data.iterator" });
            }
        });
        db.query("select * from tdd_user").sizeEq(2)
                .propertyEq(TddUserTable.IColumn.f_first_name, new String[] { "darui.wu", "data.iterator" });
    }

    @Test
    public void testInsert_DataIterator_1() {
        db.table(ITable.t_tdd_user).clean().insert(new TddUserTable() {
            {
                this.put(IColumn.f_id, 1);
                this.put(IColumn.f_first_name, "darui.wu");
            }
        }, new TddUserTable() {
            {
                this.put(IColumn.f_id, 2);
                this.put(IColumn.f_first_name, "data.iterator");
            }
        });
        db.query("select * from tdd_user").sizeEq(2)
                .propertyEq(TddUserTable.IColumn.f_first_name, new String[] { "darui.wu", "data.iterator" });
    }

    @Test
    public void testInsert_DataIterator_2() {
        db.table(ITable.t_tdd_user).clean().insert(new TddUserTable() {
            {
                this.put(IColumn.f_id, 1);
                this.put(IColumn.f_first_name, "darui.wu");
            }
        });
        db.table(ITable.t_tdd_user).insert(new TddUserTable() {
            {
                this.put(IColumn.f_id, 2);
                this.put(IColumn.f_first_name, "data.iterator");
            }
        });
        db.query("select * from tdd_user").sizeEq(2)
                .propertyEq(TddUserTable.IColumn.f_first_name, new String[] { "darui.wu", "data.iterator" });
    }

    @Test
    public void testInsert_DuplicateKey() {
        try {
            db.table(ITable.t_tdd_user).clean().insert(new DataSet() {
                {
                    this.data("{id:1,first_name:darui.wu}");
                    this.data("{id:1,first_name:data.iterator}");
                }
            });
            want.fail();
        } catch (Exception e) {
            String message = ExceptionWrapper.toString(e);
            want.string(message).contains("duplicate entry", StringMode.IgnoreCase);
        }
    }

    @Test
    public void testInsert_CheckFillData() {
        new MockUp<ColumnMeta>() {
            @Mock
            public boolean isNullable() {
                return false;
            }
        };
        db.table(ITable.t_tdd_user).clean().insert(new DataSet() {
            {
                this.data("{id:1,first_name:darui.wu}");
                this.data("{id:2,first_name:data.iterator}");
            }
        }).commit();
        db.table(ITable.t_tdd_user).query().reflectionEqMap(ListHelper.toList(new TddUserTable() {
            {
                this.put(IColumn.f_id, 1);
                this.put(IColumn.f_first_name, "darui.wu");
                this.put(IColumn.f_post_code, "jteste");
                this.put(IColumn.f_address_id, 0);
                this.put(IColumn.f_last_name, "jtester");
                this.put(IColumn.f_sarary, 0.0);
            }
        }, new TddUserTable() {
            private static final long serialVersionUID = 1L;

            {
                this.put(IColumn.f_id, 2);
                this.put(IColumn.f_first_name, "data.iterator");
                this.put(IColumn.f_post_code, "jteste");
                this.put(IColumn.f_address_id, 0);
                this.put(IColumn.f_last_name, "jtester");
                this.put(IColumn.f_sarary, 0.0);
            }
        }));
    }

    @Test
    public void testInsert_CheckFillData2() {
        new MockUp<ColumnMeta>() {
            @Mock
            public boolean isNullable() {
                return false;
            }
        };
        db.table(ITable.t_tdd_user).clean().insert(new DataSet() {
            {
                this.data("{id:1,first_name:darui.wu}");
                this.data("{id:2,first_name:data.iterator}");
            }
        }).commit();
        db.table(ITable.t_tdd_user).query().reflectionEqMap(2, new TddUserTable() {
            {
                this.put(IColumn.f_id, 1, 2);
                this.put(IColumn.f_first_name, "darui.wu", "data.iterator");
                this.put(IColumn.f_post_code, "jteste");
                this.put(IColumn.f_address_id, 0);
                this.put(IColumn.f_last_name, "jtester");
                this.put(IColumn.f_sarary, 0.0);
            }
        }, EqMode.IGNORE_ORDER);
    }

    @Test
    public void testInsert_MapDataIterator() {
        new MockUp<ColumnMeta>() {
            @Mock
            public boolean isNullable() {
                return false;
            }
        };
        db.table(ITable.t_tdd_user).clean().insert(new DataSet() {
            {
                this.data(new TddUserTable() {
                    {
                        put(IColumn.f_id, "1");
                        put(IColumn.f_first_name, "darui.wu");
                    }
                });
                this.data(new TddUserTable() {
                    {
                        put(IColumn.f_id, "2");
                        put(IColumn.f_first_name, "data.iterator");
                    }
                });
            }
        });
        db.commit().table(ITable.t_tdd_user).count().isEqualTo(2);
    }

    @Test
    public void testInsert_MapDataAndJSON() {
        new MockUp<ColumnMeta>() {
            @Mock
            public boolean isNullable() {
                return false;
            }
        };
        db.table(ITable.t_tdd_user).clean().insert(new DataSet() {
            {
                this.data(new TddUserTable() {
                    {
                        put(IColumn.f_id, "1");
                        put(IColumn.f_first_name, "darui.wu");
                    }
                });
                this.data("{'id':2, 'first_name':'data.iterator'}");
            }
        }).commit();
        db.table(ITable.t_tdd_user).count().isEqualTo(2);
    }

    @Test
    public void testInsert_JSON() {
        db.table(ITable.t_tdd_user).clean().insert("{'id':1,'first_name':'wang','last_name':'json'}").commit();
        db.table(ITable.t_tdd_user).query().reflectionEqMap(new TddUserTable() {
            {
                this.put(IColumn.f_id, 1);
                this.put(IColumn.f_first_name, "wang");
                this.put(IColumn.f_last_name, "json");
            }
        });
    }

    @Test
    public void testCount_MySQL() {
        db.table(ITable.t_tdd_user).count().notNull();
    }

    @Test
    public void testQueryWhere_DataMap() {
        db.table(ITable.t_tdd_user).clean().insert(3, new TddUserTable() {
            {
                this.put(IColumn.f_id, new int[] { 100, 101, 102 });
                this.put(IColumn.f_first_name, "name1", "name2", "name3");
                this.put(IColumn.f_post_code, "310012", "310000");
            }
        });
        db.table(ITable.t_tdd_user).count().eq(3);
        db.table(ITable.t_tdd_user).queryWhere(new TddUserTable() {
            {
                this.put(IColumn.f_post_code, "310000");
            }
        }).propertyEq(TddUserTable.IColumn.f_id, new int[] { 101, 102 });
    }

    @Test
    public void testQueryWhere_String() {
        db.table(ITable.t_tdd_user).clean().insert(3, new TddUserTable() {
            {
                this.put(IColumn.f_id, new int[] { 100, 101, 102 });
                this.put(IColumn.f_first_name, "name1", "name2", "name3");
                this.put(IColumn.f_post_code, "310012", "310000");
            }
        });
        db.table(ITable.t_tdd_user).count().eq(3);
        db.table(ITable.t_tdd_user).queryWhere("post_code=310000").propertyEq("id", new int[] { 101, 102 });
    }
}
