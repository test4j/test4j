package org.test4j.generator.mybatis.config;

import lombok.Getter;
import org.test4j.generator.mybatis.rule.Naming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 策略配置项
 *
 * @author darui.wu
 */
@Data
@Accessors(chain = true)
public class StrategyConfig {
    /**
     * 数据库表映射到实体的命名策略
     */
    private Naming tableNaming = Naming.underline_to_camel;
    /**
     * 数据库表字段映射到实体的命名策略
     * <p>未指定按照 naming 执行</p>
     */
    @Getter(AccessLevel.NONE)
    private Naming columnNaming;

    /**
     * 实体是否生成 serialVersionUID
     */
    private boolean entitySerialVersionUID = true;

    /**
     * Boolean类型字段是否移除is前缀（默认 false）<br>
     * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
     */
    private boolean booleanColumnRemoveIsPrefix = false;

    public Naming getColumnNaming() {
        return columnNaming == null ? tableNaming : columnNaming;
    }
}
