package org.jtester.module.database;

import org.jtester.module.core.ICoreInitial;
import org.jtester.module.database.dbop.IDBOperator;

public interface IDatabase {
    final IDBOperator db = ICoreInitial.initDBOperator();
}
