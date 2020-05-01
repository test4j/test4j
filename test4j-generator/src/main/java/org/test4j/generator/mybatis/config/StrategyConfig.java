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
    private Naming tableNaming = Naming.no_change;
    /**
     * 数据库表字段映射到实体的命名策略
     * <p>未指定按照 naming 执行</p>
     */
    private Naming columnNaming = null;
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
//    /**
//     * 自定义继承的Mapper类全称，带包名
//     */
//    private String superMapperClass = FmGeneratorConst.SUPER_MAPPER_CLASS;
//    /**
//     * 自定义继承的Service类全称，带包名
//     */
//    private String superServiceClass = FmGeneratorConst.SUPER_SERVICE_CLASS;
//    /**
//     * 自定义继承的ServiceImpl类全称，带包名
//     */
//    private String superServiceImplClass = FmGeneratorConst.SUPER_SERVICE_IMPL_CLASS;
//    /**
//     * 自定义继承的Controller类全称，带包名
//     */
//    private String superControllerClass;
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
     * 【实体】是否生成字段常量（默认 false）<br>
     * -----------------------------------<br>
     * public static final String ID = "test_id";
     */
    private boolean entityColumnConstant = false;
    /**
     * 【实体】是否为构建者模型（默认 false）<br>
     * -----------------------------------<br>
     * public User setName(String name) { this.name = name; return this; }
     */
    private boolean entityBuilderModel = false;
    /**
     * 【实体】是否为lombok模型（默认 false）<br>
     * <a href="https://projectlombok.org/">document</a>
     */
    private boolean entityLombokModel = false;
    /**
     * Boolean类型字段是否移除is前缀（默认 false）<br>
     * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
     */
    private boolean entityBooleanColumnRemoveIsPrefix = false;

    /**
     * 是否生成实体时，生成字段注解
     */
    private boolean entityTableFieldAnnotationEnable = false;

    /**
     * 大写命名、字段符合大写字母数字下划线命名
     *
     * @param word 待判断字符串
     */
    public boolean isCapitalModeNaming(String word) {
        return isCapitalMode && StringUtils.isCapitalMode(word);
    }


    public Naming getColumnNaming() {
        if (null == columnNaming) {
            // 未指定以 naming 策略为准
            return tableNaming;
        }
        return columnNaming;
    }


    public boolean includeSuperEntityColumns(String fieldName) {
        if (null != superEntityColumns) {
            for (String column : superEntityColumns) {
                if (column.equals(fieldName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public StrategyConfig setSuperEntityColumns(String... superEntityColumns) {
        this.superEntityColumns = superEntityColumns;
        return this;
    }

    public StrategyConfig setInclude(String... include) {
        this.include = include;
        return this;
    }

    public StrategyConfig setFieldPrefix(String... fieldPrefixs) {
        this.fieldPrefix = fieldPrefixs;
        return this;
    }

    public StrategyConfig setSuperEntityClass(String superEntityClass) {
        this.superEntityClass = superEntityClass;
        return this;
    }


    /**
     * <p>
     * 设置实体父类，该设置自动识别公共字段<br/>
     * 属性 superEntityColumns 改配置无需再次配置
     * </p>
     * <p>
     * 注意！！字段策略要在设置实体父类之前有效
     * </p>
     *
     * @param clazz 实体父类 Class
     * @return
     */
    public StrategyConfig setSuperEntityClass(Class<?> clazz) {
        return setSuperEntityClass(clazz, null);
    }

    /**
     * <p>
     * 设置实体父类，该设置自动识别公共字段<br/>
     * 属性 superEntityColumns 改配置无需再次配置
     * </p>
     *
     * @param clazz        实体父类 Class
     * @param columnNaming 字段命名策略
     * @return
     */
    public StrategyConfig setSuperEntityClass(Class<?> clazz, Naming columnNaming) {
        if (null != columnNaming) {
            this.columnNaming = columnNaming;
        }
        String pkg = ClassUtils.getPackageName(clazz);
        if (StringUtils.isNotEmpty(pkg)) {
            pkg += "." + clazz.getSimpleName();
        } else {
            pkg = clazz.getSimpleName();
        }
        this.superEntityClass = pkg;
        convertSuperEntityColumns(clazz);
        return this;
    }

    /**
     * <p>
     * 父类 Class 反射属性转换为公共字段
     * </p>
     *
     * @param clazz 实体父类 Class
     */
    protected void convertSuperEntityColumns(Class<?> clazz) {
        List<Field> fields = TableInfoHelper.getAllFields(clazz);
        if (CollectionUtils.isNotEmpty(fields)) {
            this.superEntityColumns = fields.stream().map(field -> {
                if (null == columnNaming || columnNaming == Naming.no_change) {
                    return field.getName();
                }
                return StringUtils.camelToUnderline(field.getName());
            }).collect(Collectors.toSet()).stream().toArray(String[]::new);
        }
    }

    /**
     * @deprecated please use `setEntityTableFieldAnnotationEnable`
     */
    @Deprecated
    public StrategyConfig entityTableFieldAnnotationEnable(boolean isEnableAnnotation) {
        entityTableFieldAnnotationEnable = isEnableAnnotation;
        return this;
    }
}
