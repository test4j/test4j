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

import cn.org.atool.fluent.mybatis.generator.demo.helper.NoPrimaryEntityHelper;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.NoPrimaryMP;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.NoPrimaryMP.Column;



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
@ScriptTable(NoPrimaryMP.Table_Name)
public class NoPrimaryEntity implements IEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableField(value = Column.column_1)
    private Integer column1;
    /**
     * 
     */
    @TableField(value = Column.column_2)
    private String column2;

    @Override
    public Serializable findPk() {
        return null;
    }

    /**
     * 将实体对象转换为map
     */
    @Override
    public Map<String, Object> toMap() {
        return NoPrimaryEntityHelper.map(this);
    }
}