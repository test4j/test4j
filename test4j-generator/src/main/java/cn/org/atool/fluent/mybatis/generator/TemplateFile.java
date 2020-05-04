//package cn.org.atool.fluent.mybatis.generator;
//
//import org.test4j.generator.mybatis.config.FileOutConfig;
//import org.test4j.generator.mybatis.model.TableInfo;
//import lombok.Setter;
//import lombok.experimental.Accessors;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import static java.util.stream.Collectors.joining;
//
//@Accessors(chain = true)
//public class TemplateFile {
//    public static final List<TemplateFile> TEMPLATE_FILE_LIST = new ArrayList<TemplateFile>() {
//        {
//            this.add(new TemplateFile("", ""));
//            this.add(new TemplateFile("entity/EntityHelper.java.vm", "helper/*EntityHelper.java"));
//            this.add(new TemplateFile("query/Query.java.vm", "query/*EntityQuery.java"));
//            this.add(new TemplateFile("query/Update.java.vm", "query/*EntityUpdate.java"));
//            this.add(new TemplateFile("query/WrapperHelper.java.vm", "query/*EntityWrapperHelper.java"));
//
//            this.add(new TemplateFile("dao/BaseDao.java.vm", "dao/base/*BaseDao.java")
//                    .setBaseDao(true)
//            );
//            this.add(new TemplateFile("mapper/Partition.java.vm", "mapper/*PartitionMapper.java")
//                    .setPartition(true)
//            );
//            //test
//            this.add(new TemplateFile("dao/DaoIntf.java.vm", "dao/intf/*Dao.java", TemplateType.Dao));
//            this.add(new TemplateFile("dao/DaoImpl.java.vm", "dao/impl/*DaoImpl.java", TemplateType.Dao));
//            this.add(new TemplateFile("datamap/TableMap.java.vm", "datamap/table/*TableMap.java", TemplateType.Test));
//            this.add(new TemplateFile("datamap/EntityMap.java.vm", "datamap/entity/*EntityMap.java", TemplateType.Test));
//            this.add(new TemplateFile("mix/TableMix.java.vm", "mix/*TableMix.java", TemplateType.Test));
//        }
//    };
//
//    private String template;
//
//    private String fileNameReg;
//
//    private TemplateType templateType = TemplateType.Base;
//    /**
//     * 是否分库
//     */
//    @Setter
//    private boolean isPartition = false;
//    /**
//     * 是否base dao
//     */
//    @Setter
//    private boolean isBaseDao = false;
//    /**
//     * 文件子路径
//     */
//    private String fileSubPath;
//    /**
//     * 文件后缀
//     */
//    private String fileSuffix;
//    /**
//     * 文件名称(不包含路径和后缀)
//     */
//    private String fileName;
//    /**
//     * 模板标识
//     */
//    private String templateId;
//
//    public TemplateFile(String template, String fileNameReg) {
//        this.template = template;
//        this.fileNameReg = fileNameReg;
//    }
//
//    public TemplateFile(String template, String fileNameReg, TemplateType templateType) {
//        this.template = template;
//        this.fileNameReg = fileNameReg;
//        this.templateType = templateType;
//    }
//
//    public static List<FileOutConfig> parseConfigList(MybatisGenerator generator, Map<String, Object> config) {
//        return TEMPLATE_FILE_LIST.stream()
//                .filter(template -> !template.isPartition || currTable().isPartition())
//                .map(template -> template.parse(generator, config))
//                .collect(Collectors.toList());
//    }
//
//    private FileOutConfig parse(MybatisGenerator generator, Map<String, Object> config) {
//        return new FileOutConfig("/templates/" + template) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                parse(currTable().getWithoutSuffixEntity());
//                config.put("withoutSuffixEntity", currTable().getWithoutSuffixEntity());
//                config.put("withSuffixEntity", currTable().getWithSuffixEntity());
//                String _package = getFilePack(generator.getPackage(templateType));
//
//                config.put("pack" + templateId, _package);
//                config.put("file" + templateId, fileName);
//                setBaseDaoImports(config);
//                return getFullFileName(generator);
//            }
//        };
//    }
//
//    private void setBaseDaoImports(Map<String, Object> config) {
//        if (!this.isBaseDao || currTable().getBaseDaoInterfaces().size() == 0) {
//            return;
//        }
//        String imports = currTable().getBaseDaoInterfaces().values().stream().map(item -> "import " + item + ";").collect(joining("\n"));
//        config.put("baseDaoInterfaceImports", imports);
//
//        String implement = currTable().getBaseDaoInterfaces().keySet().stream().map(item -> {
//            item = item.replaceAll("\\$\\{entity\\}", (String) config.get("withSuffixEntity"));
//            item = item.replaceAll("\\$\\{update\\}", (String) config.get("fileEntityUpdate"));
//            item = item.replaceAll("\\$\\{query\\}", (String) config.get("fileEntityQuery"));
//            return item;
//        }).collect(joining(", "));
//        config.put("baseDaoInterfaceImplement", implement);
//    }
//
//    private void parse(String entityName) {
//        int start = this.fileNameReg.lastIndexOf('/');
//        start = start < 0 ? 0 : start;
//        int end = this.fileNameReg.indexOf('.');
//        end = end < 0 ? this.fileNameReg.length() : end;
//
//        this.fileSubPath = this.fileNameReg.substring(0, start);
//        String fileRegName = this.fileNameReg.substring(start + 1, end);
//        this.fileSuffix = this.fileNameReg.substring(end + 1);
//
//        this.fileName = fileRegName.replace("*", entityName);
//        this.templateId = fileRegName.replace("*", "");
//    }
//
//    private String getFilePack(String basePackage) {
//        return basePackage + "." + fileSubPath.replace('/', '.');
//    }
//
//    private String getFullFileName(MybatisGenerator generator) {
//        String output = generator.getOutputDir();
//        if (this.templateType == TemplateType.Test) {
//            output = generator.getTestOutputDir();
//        } else if (this.templateType == TemplateType.Dao) {
//            output = generator.getDaoOutputDir();
//        }
//        String parentPath = generator.getPackage(templateType).replaceAll("\\.", "/");
//        return String.format("%s/%s/%s/%s.%s", output, parentPath, fileSubPath, fileName, fileSuffix);
//    }
//
//    public enum TemplateType {
//        Base,
//        Test,
//        Dao;
//    }
//}
