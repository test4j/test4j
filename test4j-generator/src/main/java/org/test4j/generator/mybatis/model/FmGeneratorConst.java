package org.test4j.generator.mybatis.model;

import com.baomidou.mybatisplus.core.toolkit.StringPool;

import java.nio.charset.StandardCharsets;

/**
 * FluentMybatis文件生成常量定义
 *
 */
public interface FmGeneratorConst {

    String MODULE_NAME = "ModuleName";


    String SERVICE = "Service";
    String SERVICE_IMPL = "ServiceImpl";

    String MAPPER = "Mapper";
    String XML = "Xml";
    String CONTROLLER = "Controller";

    String ENTITY_PATH = "entity_path";
    String SERVICE_PATH = "service_path";
    String SERVICE_IMPL_PATH = "service_impl_path";
    String MAPPER_PATH = "mapper_path";
    String XML_PATH = "xml_path";
    String CONTROLLER_PATH = "controller_path";

    String JAVA_TMPDIR = "java.io.tmpdir";
    String UTF8 = StandardCharsets.UTF_8.name();
    String UNDERLINE = "_";

    String JAVA_SUFFIX = StringPool.DOT_JAVA;
    String KT_SUFFIX = ".kt";
    String XML_SUFFIX = ".xml";

//    String TEMPLATE_ENTITY_JAVA = "/templates/entity.java";
//    String TEMPLATE_ENTITY_KT = "/templates/entity.kt";
//    String TEMPLATE_MAPPER = "/templates/mapper.java";
//    String TEMPLATE_XML = "/templates/mapper.xml";
//    String TEMPLATE_SERVICE = "/templates/service.java";
//    String TEMPLATE_SERVICE_IMPL = "/templates/serviceImpl.java";
//    String TEMPLATE_CONTROLLER = "/templates/controller.java";

    String VM_LOAD_PATH_KEY = "file.resource.loader.class";
    String VM_LOAD_PATH_VALUE = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

    String SUPER_MAPPER_CLASS = "com.baomidou.mybatisplus.core.mapper.BaseMapper";
    String SUPER_SERVICE_CLASS = "com.baomidou.mybatisplus.extension.service.IService";
    String SUPER_SERVICE_IMPL_CLASS = "com.baomidou.mybatisplus.extension.service.impl.ServiceImpl";
}
