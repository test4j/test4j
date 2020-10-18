package cn.org.atool.fluent.mybatis.generator.demo.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import java.io.Serializable;
import java.lang.Override;
import java.lang.String;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * NoAutoIdEntity
 *
 * @author darui.wu@163.com
 */
@Data
@Accessors(
    chain = true
)
@FluentMybatis(
    table = "no_auto_id",
    mapperBeanPrefix = "new"
)
public class NoAutoIdEntity implements IEntity {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      auto = false
  )
  private String id;

  @TableField("column_1")
  private String column1;

  @Override
  public Serializable findPk() {
    return this.id;
  }
}
