package org.test4j.module.database.sql;

import lombok.Data;
import lombok.experimental.Accessors;
import org.test4j.tools.commons.StringHelper;

import java.util.List;

@Data
@Accessors(chain = true)
public class SqlContext {
    private String sql;

    private Object[] parameters = {};

    public SqlContext() {
    }

    public SqlContext(String sql, Object[] parameters) {
        this.sql = StringHelper.trim(sql);
        if (parameters != null) {
            this.parameters = parameters;
        }
    }

    public SqlContext(String sql, List parameters) {
        this.sql = StringHelper.trim(sql);
        if (parameters != null && !parameters.isEmpty()) {
            this.parameters = parameters.toArray();
        }
    }
}
