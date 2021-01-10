package org.test4j.module.database.sql;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

import static org.test4j.tools.commons.StringHelper.trim;
import static org.test4j.tools.database.SQLUtility.filterSpace;

@Data
@Accessors(chain = true)
public class SqlContext {
    private String sql;

    private Object[] parameters = {};

    public SqlContext() {
    }

    public SqlContext(String sql, Object[] parameters) {
        this.sql = filterSpace(trim(sql));
        if (parameters != null) {
            this.parameters = parameters;
        }
    }

    public SqlContext(String sql, List parameters) {
        this.sql = filterSpace(trim(sql));
        if (parameters != null && !parameters.isEmpty()) {
            this.parameters = parameters.toArray();
        }
    }
}