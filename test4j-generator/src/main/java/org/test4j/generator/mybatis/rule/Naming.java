package org.test4j.generator.mybatis.rule;

import java.util.Arrays;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.test4j.generator.mybatis.model.FmGeneratorConst;
import org.test4j.tools.commons.StringConst;

/**
 * 数据库表到文件命名转换策略
 *
 * @author wudarui
 */
public enum Naming {
    /**
     * 不做任何改变，原样输出
     */
    no_change,
    /**
     * 下划线转驼峰命名
     */
    underline_to_camel;

    public static String underlineToCamel(String name) {
        if (StringUtils.isEmpty(name)) {
            return StringConst.EMPTY;
        }
        String tempName = name;
        // 大写数字下划线组成转为小写 , 允许混合模式转为小写
        if (StringUtils.isCapitalMode(name) || StringUtils.isMixedMode(name)) {
            tempName = name.toLowerCase();
        }
        StringBuilder result = new StringBuilder();
        String[] camels = tempName.split(StringConst.UNDERLINE);
        for(String word:camels){

        }
        // 跳过原始字符串中开头、结尾的下换线或双重下划线
        // 处理真正的驼峰片段
        Arrays.stream(camels).filter(camel -> !StringUtils.isEmpty(camel)).forEach(camel -> {
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel);
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(capitalFirst(camel));
            }
        });
        return result.toString();
    }

    /**
     * 去掉指定的前缀
     *
     * @param name   字段名称
     * @param prefix 前缀
     * @return
     */
    public static String removePrefix(String name, String... prefix) {
        if (StringUtils.isEmpty(name)) {
            return StringPool.EMPTY;
        }
        if (prefix == null) {
            return name;
        }
        String lowerCase = name.toLowerCase();
        for (String pf : prefix) {
            if (lowerCase.startsWith(pf.toLowerCase())) {
                return name.substring(pf.length());
            }
        }
        return name;
    }

    /**
     * 去掉下划线前缀且将后半部分转成驼峰格式
     *
     * @param name        ignore
     * @param tablePrefix ignore
     * @return ignore
     */
    public static String removePrefixAndCamel(String name, String[] tablePrefix) {
        return underlineToCamel(removePrefix(name, tablePrefix));
    }

    /**
     * 实体首字母大写
     *
     * @param name 待转换的字符串
     * @return 转换后的字符串
     */
    public static String capitalFirst(String name) {
        if (StringUtils.isNotEmpty(name)) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        } else {
            return StringPool.EMPTY;
        }
    }
}
