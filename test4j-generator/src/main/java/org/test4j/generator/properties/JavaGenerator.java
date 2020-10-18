//package org.test4j.generator.properties;
//
//import org.test4j.tools.commons.TextBuilder;
//
//import java.util.Arrays;
//import java.util.Map;
//import java.util.TreeMap;
//
///**
// * JavaGenerator
// *
// * @author darui.wu
// * @create 2020/4/24 6:04 下午
// */
//final class JavaGenerator {
//    private final static String Properties_Klass_Surfix = "Properties";
//
//    private String klassName;
//    /**
//     * key: fullKey的最后部分；value：fullKey
//     */
//    private Map<String, Object> children = new TreeMap<>();
//
//    JavaGenerator(String klassName) {
//        this.klassName = klassName;
//    }
//
//    final void addKeys(String[] subKeys, String fullKey, String value) {
//        String subKey = subKeys[0];
//        if (subKeys.length == 1) {
//            this.children.put(subKey, new LeafNode(fullKey, value));
//        } else {
//            if (!children.containsKey(subKey)) {
//                children.put(subKey, new JavaGenerator(this.klassName(subKey)));
//            }
//            ((JavaGenerator) children.get(subKey)).addKeys(Arrays.copyOfRange(subKeys, 1, subKeys.length), fullKey, value);
//        }
//    }
//
//    /**
//     * 首字母大写
//     *
//     * @param key
//     * @return
//     */
//    private String klassName(String key) {
//        return key.substring(0, 1).toUpperCase() + key.substring(1) + Properties_Klass_Surfix;
//    }
//
//    /**
//     * 生成配置对应层级的class文件
//     *
//     * @param root 是否根目录
//     * @param tab  缩进
//     * @return
//     */
//    final String toJavaStr(boolean root, String tab) {
//        TextBuilder klass = new TextBuilder().append("\n%spublic %sclass %s {", tab, root ? "" : "static ", this.klassName);
//        TextBuilder construct = new TextBuilder().append("\n\n%s\t%s(Environment env) {", tab, this.klassName);
//
//        if (root) {
//            klass.append("\n\tpublic static %s config;\n", this.klassName);
//        }
//        for (Map.Entry<String, Object> entry : this.children.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            if (value instanceof LeafNode) {
//                LeafNode node = (LeafNode) value;
//                String keyType = node.getKeyType();
//                klass.append("\n%s\tpublic final %s %s;", tab, keyType, key);
//                construct.append("\n%s\t\tthis.%s = new %s(env, \"%s\");", tab, key, keyType, node.getFullKey());
//            } else {
//                String klassName = ((JavaGenerator) value).klassName;
//                klass.append("\n%s\tpublic final %s %s;", tab, klassName, key);
//                construct.append("\n%s\t\tthis.%s = new %s(env);", tab, key, klassName);
//            }
//        }
//
//        if (root) {
//            construct.append("\n\t\t%s.config = this;", this.klassName);
//        }
//        construct.append("\n\t%s}", tab);
//        klass.append(construct);
//        for (Map.Entry<String, Object> entry : this.children.entrySet()) {
//            Object value = entry.getValue();
//            if (value instanceof JavaGenerator) {
//                String java = ((JavaGenerator) value).toJavaStr(false, tab + "\t");
//                klass.append("\n" + java);
//            }
//        }
//        klass.append("\n%s}", tab);
//        return klass.toString();
//    }
//}