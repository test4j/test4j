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

import cn.org.atool.fluent.mybatis.generator.demo.helper.UserEntityHelper;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.UserMP;
import cn.org.atool.fluent.mybatis.generator.demo.mapping.UserMP.Column;

import java.util.Date;

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
@ScriptTable(UserMP.Table_Name)
public class UserEntity implements IEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = Column.id, type = IdType.AUTO)
    private Long id;
    /**
     * 
     */
    @TableField(value = Column.gmt_created, insert = "now()")
    private Date gmtCreated;
    /**
     * 
     */
    @TableField(value = Column.gmt_modified, update = "now()", insert = "now()")
    private Date gmtModified;
    /**
     * 
     */
    @TableField(value = Column.is_deleted, update = "0", insert = "0")
    private Boolean isDeleted;
    /**
     * 
     */
    @TableField(value = Column.address_id)
    private Long addressId;
    /**
     * 
     */
    @TableField(value = Column.age)
    private Integer age;
    /**
     * 
     */
    @TableField(value = Column.user_name)
    private String userName;
    /**
     * 
     */
    @TableField(value = Column.version)
    private String version;

    @Override
    public Serializable findPk() {
        return this.id;
    }

    /**
     * 将实体对象转换为map
     */
    @Override
    public Map<String, Object> toMap() {
        return UserEntityHelper.map(this);
    }
}