package org.test4j.module.database;

import org.test4j.module.core.ICoreInitial;
import org.test4j.module.database.dbop.IDBOperator;

public interface IDatabase {
    final IDBOperator db = ICoreInitial.initDBOperator();
}
