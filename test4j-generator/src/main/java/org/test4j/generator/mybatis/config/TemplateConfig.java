package org.test4j.generator.mybatis.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.test4j.generator.mybatis.model.FmGeneratorConst;

/**
 * 模板路径配置项
 *
 * @author tzg hubin
 * @since 2017-06-17
 */
@Data
@Accessors(chain = true)
public class TemplateConfig {

    @Getter(AccessLevel.NONE)
    private String entity = FmGeneratorConst.TEMPLATE_ENTITY_JAVA;

    private String entityKt = FmGeneratorConst.TEMPLATE_ENTITY_KT;

    private String service = FmGeneratorConst.TEMPLATE_SERVICE;

    private String serviceImpl = FmGeneratorConst.TEMPLATE_SERVICE_IMPL;

    private String mapper = FmGeneratorConst.TEMPLATE_MAPPER;

    private String xml = FmGeneratorConst.TEMPLATE_XML;

    private String controller = FmGeneratorConst.TEMPLATE_CONTROLLER;

    public String getEntity(boolean kotlin) {
        return kotlin ? entityKt : entity;
    }
}
