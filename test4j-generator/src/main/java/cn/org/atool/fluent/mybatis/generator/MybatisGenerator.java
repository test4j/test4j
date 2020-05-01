//package cn.org.atool.fluent.mybatis.generator;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
//import org.test4j.generator.mybatis.config.InjectionConfig;
//import org.test4j.generator.mybatis.rule.DateType;
//import org.test4j.generator.mybatis.rule.DbType;
//import org.test4j.generator.mybatis.rule.Naming;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.experimental.Accessors;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.test4j.generator.mybatis.config.*;
//import org.test4j.generator.mybatis.template.AbstractTableTemplate;
//import org.test4j.module.core.utility.MessageHelper;
//import org.test4j.tools.commons.StringHelper;
//
//import java.util.*;
//
//@Getter
//@Setter
//@Accessors(chain = true)
//@Slf4j
//public class MybatisGenerator {
//
//
//
//    /**
//     * 代码package前缀
//     */
//    private String basePackage;
//    /**
//     * 项目dao类的基础package
//     */
//    private String daoBasePackage;
//
//
//    private IdType idType;
//
//    public MybatisGenerator(String basePackage) {
//        this.basePackage = basePackage;
//        this.daoBasePackage = basePackage;
//    }
//
//    public MybatisGenerator(String basePackage, String daoBasePackage) {
//        this.basePackage = basePackage;
//        this.daoBasePackage = daoBasePackage;
//    }
//
//
////    public void generate(TableConvert... convertors) {
////        List<GenerateObj> generateObjs = new ArrayList<>();
////        for (TableConvert tableConvert : convertors) {
////            tableConvert.setMybatisGenerator(this);
////            List<Table> list = new ArrayList<>();
////            list.addAll(tableConvert.getTables().values());
////            Collections.sort(list);
////
////            for (Table table : list) {
////                currTable.set(table);
////                MessageHelper.info("begin to generate table:" + table.getTableName());
////                this.generate(tableConvert, new String[]{table.getTableName()}, table.getVersionColumn());
////                generateObjs.add(GenerateObj.init(table));
////                log.info("generate table {} successful.", table.getTableName());
////            }
////        }
////        currTable.remove();
////
////    }
//
//
////    /**
////     * 生成mybatis模板
////     * <p/>
////     * 如果多张表的策略不一致， 可以把表分开重复调用此方法
////     *
////     * @param convertor
////     * @param tableNames 生成表列表
////     * @param verField   乐观锁字段
////     */
////    private void generate(TableConvert convertor, String[] tableNames, String verField) {
////        new AutoGenerator()
////            .setGlobalConfig(this.initGlobalConfig(convertor.getEntitySuffix()))
////            .setDataSource(convertor.getDataSourceConfig())
////            .setPackageInfo(this.initPackageConfig())
////            .setTemplate(this.initTemplate())
////            .setStrategy(this.initStrategy(convertor.getPrefix(), tableNames, verField))
////            .setInjectionConfig(this.initInjectConfig(convertor))
////            .execute();
////    }
//
//    public String getPackage(TemplateFile.TemplateType type) {
//        if (TemplateFile.TemplateType.Dao.equals(type) && this.daoBasePackage != null) {
//            return this.daoBasePackage;
//        } else {
//            return this.basePackage;
//        }
//    }
//
//    /**
//     * 初始化全局配置
//     *
//     * @param entitySuffix 数据库模型实体类后缀
//     * @return
//     */
////    private GlobalConfig initGlobalConfig(String entitySuffix) {
////        GlobalConfig config = new GlobalConfig()
////            .setAuthor(this.author)
////            .setOutputDir(this.outputDir)
////            .setFileOverride(true)
////            .setActiveRecord(false)
////            .setEnableCache(false)
////            .setBaseResultMap(true)
////            .setBaseColumnList(true)
////            .setDateType(DateType.ONLY_DATE)
////            .setOpen(false)
////            .setEntityName("%s" + entitySuffix);
////        if (idType != null) {
////            config.setIdType(idType);
////        }
////        return config;
////    }
//
////    private InjectionConfig initInjectConfig(TableConvert convertor) {
////        Map<String, Object> config = new HashMap<>();
////        {
////            config.put("chainSet", this.isEntitySetChain);
////            config.putAll(currTable.get().findFieldConfig());
////
////        }
////        if (CollectionUtils.isNotEmpty(convertor.getModelInterface())) {
////            config.put("interface", true);
////            config.put("interfacePack", convertor.getInterfacePacks());
////            config.put("interfaceName", convertor.getInterfaceNames());
////        }
////        if (!StringHelper.isBlank(currTable().getMapperPrefix())) {
////            config.put("mapperPrefix", currTable().getMapperPrefix().trim());
////        }
////        InjectionConfig cfg = new InjectionConfig() {
////            @Override
////            public void initMap() {
////                this.setMap(config);
////            }
////        };
////        cfg.setFileOutConfigList(TemplateFile.parseConfigList(this, config));
////        GenerateObj.setCurrConfig(config);
////        return cfg;
////    }
//
////    private StrategyConfig initStrategy(String[] tablePrefix, String[] tables, String verField) {
////        StrategyConfig sc = new StrategyConfig();
////        sc.setCapitalMode(true)
////            .setTableNaming(Naming.underline_to_camel)
////            .setEntityLombokModel(true)
////            .setEntityTableFieldAnnotationEnable(true);
////        if (StringUtils.isNotBlank(verField)) {
////            sc.setVersionFieldName(verField);
////        }
////        if (tables != null && tables.length > 0) {
////            sc.setInclude(tables);
////        }
////        if (tablePrefix != null) {
////            sc.setTablePrefix(tablePrefix);
////        }
////        return sc;
////    }
//
//    /**
//     * 初始化模板路径，允许覆盖，可以拷贝源码 resources/templates下面文件修改
//     * <p/>
//     * 如果任何一个模板设置为空或者null， 则不生成模板
//     *
//     * @return
//     */
////    private AbstractTableTemplate initTemplate() {
////        AbstractTableTemplate tc = new AbstractTableTemplate();
////        {
////            tc.setEntity("/templates/entity/Entity.java.vm");
////            tc.setMapper("/templates/mapper/Mapper.java.vm");
////            tc.setXml(null);
////            tc.setController(null);
////            tc.setService(null);
////            tc.setServiceImpl(null);
////        }
////        return tc;
////    }
//
//    private PackageConfig initPackageConfig() {
//        return new PackageConfig()
//            .setParent(this.basePackage)
//            .setEntity("entity")
//            .setService("dao")
//            .setServiceImpl("dao.impl");
//    }
//}