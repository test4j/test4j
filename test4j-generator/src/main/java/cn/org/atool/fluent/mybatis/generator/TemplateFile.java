//package cn.org.atool.fluent.mybatis.generator;

//@Accessors(chain = true)
//public class TemplateFile {
//    public static final List<TemplateFile> TEMPLATE_FILE_LIST = new ArrayList<TemplateFile>() {
//        {
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