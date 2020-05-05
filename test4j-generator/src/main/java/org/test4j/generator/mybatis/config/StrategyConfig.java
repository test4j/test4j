package org.test4j.generator.mybatis.config;

import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.test4j.generator.mybatis.rule.Naming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 策略配置项
 *
 * @author darui.wu
 */
@Data
@Accessors(chain = true)
public class StrategyConfig {
    /**
     * 是否大写命名
     */
    private boolean isCapitalMode = false;
    /**
     * 名称转换
     */
    private INameConvert nameConvert;
    /**
     * 数据库表映射到实体的命名策略
     */
    private Naming tableNaming = Naming.underline_to_camel;
    /**
     * 数据库表字段映射到实体的命名策略
     * <p>未指定按照 naming 执行</p>
     */
    private Naming columnNaming = Naming.underline_to_camel;
    /**
     * 表前缀
     */
    @Setter(AccessLevel.NONE)
    private String[] tablePrefix;
    /**
     * 字段前缀
     */
    @Setter(AccessLevel.NONE)
    private String[] fieldPrefix;
    /**
     * 自定义继承的Entity类全称，带包名
     */
    @Setter(AccessLevel.NONE)
    private String superEntityClass;
    /**
     * 自定义基础的Entity类，公共字段
     */
    @Setter(AccessLevel.NONE)
    private String[] superEntityColumns;
    /**
     * 需要包含的表名，允许正则表达式（与exclude二选一配置）
     */
    @Setter(AccessLevel.NONE)
    private String[] include = null;

    /**
     * 实体是否生成 serialVersionUID
     */
    private boolean entitySerialVersionUID = true;

    /**
     * Boolean类型字段是否移除is前缀（默认 false）<br>
     * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
     */
    private boolean entityBooleanColumnRemoveIsPrefix = false;
    
    public Naming getColumnNaming() {
        return columnNaming == null ? tableNaming : columnNaming;
    }

}
