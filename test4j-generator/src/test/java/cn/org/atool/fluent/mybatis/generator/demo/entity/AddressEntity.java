package cn.org.atool.fluent.mybatis.generator.demo.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * AddressEntity
 *
 * @author darui.wu@163.com
 */
@Data
@Accessors(
    chain = true
)
@FluentMybatis(
    table = "address"
)
public class AddressEntity implements IEntity {
  private static final long serialVersionUID = 1L;

  @TableId("id")
  private Long id;

  @TableField("address")
  private String address;

  @TableField("gmt_created")
  private Date gmtCreated;

  @TableField("gmt_modified")
  private Date gmtModified;

  @TableField("is_deleted")
  private Integer isDeleted;

  @TableField("user_id")
  private Long userId;

  @Override
  public Serializable findPk() {
    return this.id;
  }
}
