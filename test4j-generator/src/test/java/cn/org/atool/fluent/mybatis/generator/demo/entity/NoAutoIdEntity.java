package cn.org.atool.fluent.mybatis.generator.demo.entity;

import cn.org.atool.fluent.mybatis.annotation.IdType;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.condition.interfaces.IEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.test4j.module.database.annotations.ScriptTable;
import java.util.Map;
import java.io.Serializable;

import cn.org.atool.fluent.mybatis.generator.demo.helper.NoAutoIdEntityHelper;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.NoAutoIdMP;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.NoAutoIdMP.Column;



/**
 * <p>
 * 
 * </p>
 *
 * @author generate code
 */
@Getter
@Setter
@Accessors(chain = true)
@ScriptTable(NoAutoIdMP.Table_Name)
public class NoAutoIdEntity implements IEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = Column.id)
    private String id;
    /**
     * 
     */
    @TableField(value = Column.column_1)
    private String column1;

    @Override
    public Serializable findPk() {
        return this.id;
    }

    /**
     * 将实体对象转换为map
     */
    @Override
    public Map<String, Object> toMap() {
        return NoAutoIdEntityHelper.map(this);
    }
}