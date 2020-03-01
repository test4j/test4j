package org.test4j.module.spec.internal;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.test4j.tools.commons.ResourceHelper;

import java.io.File;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static org.test4j.tools.commons.ResourceHelper.writeStringToFile;

/**
 * 代理桩MockUp生成
 *
 * @author darui.wu
 * @create 2019/11/15 3:58 下午
 */
public class StubGenerator {
    public static void generate(String srcDir, String _package, Class... interfaces) {
        List<String> mixes = Arrays.stream(interfaces)
                .map(klass -> new KlassGenerator(srcDir, _package, klass).generate())
                .collect(Collectors.toList());
        buildMixesFileContext(srcDir, _package, mixes);
    }

    static final String PACKAGE_REG = "\\$\\{package}";

    static final String KLASS_REG = "\\$\\{klass}";

    static void buildMixesFileContext(String srcDir, String _package, List<String> mixes) {
        mixes.sort((String s1, String s2) -> s1.compareTo(s2));
        String mixImports = mixes.stream()
                .map(mix -> "import " + _package + ".mix." + mix + ";")
                .collect(joining("\n"));
        String mixFields = mixes.stream()
                .map(mix -> {
                    String field = mix.substring(0, 1).toLowerCase() + mix.substring(1);
                    return new StringBuilder()
                            .append("\n\t@Mix")
                            .append(String.format("\n\tpublic %s %s;", mix, field))
                            .toString();
                })
                .collect(joining("\n"));

        String template = ResourceHelper.readFromClasspath("templates/stub/Mixes.java.vm");
        String context = template.replaceAll(PACKAGE_REG, _package)
                .replace("${mixImports}", mixImports)
                .replace("${mixFields}", mixFields);

        File stubFile = new File(String.format("%s/%s/StubMixes.java",
                srcDir, _package.replace('.', '/')
        ));
        writeStringToFile(stubFile, context);
    }

    static void buildStubFileContext(KlassGenerator generator) {
        generator.imports.sort(Comparator.naturalOrder());
        String imports = generator.imports.stream()
                .map(item -> "import " + item + ";")
                .collect(joining("\n"));
        String methods = generator.methods.stream()
                .collect(joining("\n"));

        String template = ResourceHelper.readFromClasspath("templates/stub/Stub.java.vm");
        String context = template.replaceAll(PACKAGE_REG, generator._package)
                .replaceAll(KLASS_REG, generator.klass.getSimpleName())
                .replace("${imports}", imports)
                .replace("${methods}", methods);
        File stubFile = new File(String.format("%s/%s/stub/%sStub.java",
                generator.srcDir,
                generator._package.replace('.', '/'),
                generator.klass.getSimpleName())
        );
        writeStringToFile(stubFile, context);
    }

    static void buildMixFileContext(KlassGenerator generator) {
        File mixFile = new File(String.format("%s/%s/mix/%sMix.java",
                generator.srcDir,
                generator._package.replace('.', '/'),
                generator.klass.getSimpleName())
        );
        if (mixFile.exists()) {
            return;
        }

        String template = ResourceHelper.readFromClasspath("templates/stub/Mix.java.vm");
        String context = template.replaceAll(PACKAGE_REG, generator._package)
                .replaceAll(KLASS_REG, generator.klass.getSimpleName());
        writeStringToFile(mixFile, context);
    }

    public static class KlassGenerator {
        final String srcDir;

        final String _package;

        final Class klass;

        final List<String> imports = new ArrayList<String>();

        final List<String> methods = new ArrayList<>();

        private ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

        public KlassGenerator(String srcDir, String _package, Class klass) {
            this.srcDir = srcDir;
            this._package = _package;
            this.klass = klass;
        }

        /**
         * 生成stub文件和mix文件<br/>
         * 如果mix文件已经存在，则跳过mix文件生成
         *
         * @return 返回mix文件名
         */
        public String generate() {
            this.init();
            buildStubFileContext(this);
            buildMixFileContext(this);

            return this.klass.getSimpleName() + "Mix";
        }


        public void init() {
            imports.add(this.klass.getName());

            List<Method> methods = Arrays.asList(klass.getDeclaredMethods());
            methods.sort(Comparator.comparing(Method::getName));
            for (Method method : methods) {
                StringBuilder buff = new StringBuilder()
                        .append("\n\t@Override")
                        .append("\n\tpublic ");

                buff.append(this.initMethodType(method))
                        .append(this.initKlass(method.getGenericReturnType()))
                        .append(" ")
                        .append(method.getName())
                        .append("(")
                        .append(this.initMethodArgs(method))
                        .append(") ")
                        .append(this.initMethodExceptions(method))
                        .append("{\n")
                        .append("\t\t").append("throw new AssertionError(\"not mock\");").append("\n")
                        .append("\t}");
                this.methods.add(buff.toString());
            }
        }

        private String initMethodType(Method method) {
            TypeVariable<Method>[] typeParameters = method.getTypeParameters();
            if (typeParameters.length == 0) {
                return "";
            }
            String mType = Arrays.stream(typeParameters).map(var -> {
                String name = var.getTypeName();
                Type[] bounds = var.getBounds();
                if (bounds.length == 1 && bounds[0].equals(Object.class)) {
                    return name;
                }
                String bType = Arrays.stream(bounds)
                        .map(this::initKlass)
                        .collect(joining(" & "));

                return bounds.length == 0 ? name : name + " extends " + bType;
            }).collect(joining(", "));
            return "<" + mType + "> ";
        }

        private String initMethodExceptions(Method method) {
            Type[] eTypes = method.getGenericExceptionTypes();
            if (eTypes.length == 0) {
                return "";
            } else {
                return "throws " + Arrays.stream(eTypes)
                        .map(this::initKlass)
                        .collect(joining(", ")) + " ";
            }
        }

        private String initMethodArgs(Method method) {
            Parameter[] parameters = method.getParameters();
            List<String> args = new ArrayList<>();
            for (int index = 0; index < method.getParameterTypes().length; index++) {
                MethodParameter mp = new MethodParameter(method, index);
                mp.initParameterNameDiscovery(discoverer);

                String type = this.initKlass(mp.getGenericParameterType());
                String name = mp.getParameterName();
                this.imports(mp.getParameterType());
                args.add(type + " " + (name == null ? "arg" + (index + 1) : name));
            }
            return args.stream().collect(joining(", "));
        }


        private void imports(Class klass) {
            String name = klass.getName();
            if (this.imports.contains(name)) {
                return;
            }
            if (!name.contains(".")) {
                return;
            }
            if (name.matches("java\\.lang\\.[\\w]+")) {
                return;
            }
            imports.add(name);
        }

        private String initKlass(Type type) {
            if (type instanceof Class) {
                this.imports((Class) type);
                return ((Class) type).getSimpleName();
            } else if (type instanceof ParameterizedType) {
                ParameterizedType ptype = (ParameterizedType) type;
                this.imports((Class) ptype.getRawType());
                String simpleName = ((Class) ptype.getRawType()).getSimpleName();
                Type[] pTypes = ptype.getActualTypeArguments();
                if (pTypes.length == 0) {
                    return simpleName;
                }
                String args = Arrays.stream(pTypes).map(this::initKlass).collect(joining(", "));
                return simpleName + "<" + args + ">";
            } else if (type instanceof WildcardType) {
                WildcardType wType = (WildcardType) type;
                String name = type.getTypeName();
                for (Type lBounds : wType.getLowerBounds()) {
                    this.initKlass(lBounds);
                }
                for (Type uBounds : wType.getUpperBounds()) {
                    this.initKlass(uBounds);
                }
                while (name.matches(".*\\s\\w+\\..*")) {
                    name = name.replaceAll("\\s\\w+\\.", " ");
                }
                while (name.matches(".*<\\w+\\..*")) {
                    name = name.replaceAll("<\\w+\\.", "<");
                }
                return name;
            } else {
                return type.getTypeName();
            }
        }
    }
}
