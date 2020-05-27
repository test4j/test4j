package org.test4j.tools.commons;

import ext.test4j.apache.commons.io.IOUtils;
import org.test4j.exception.Test4JException;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.tools.cpdetector.CodepageDetectorProxy;
import org.test4j.tools.cpdetector.JChardetFacade;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static ext.test4j.apache.commons.io.IOUtils.closeQuietly;

@SuppressWarnings("rawtypes")
public class ResourceHelper {

    /**
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     *
     * @param is
     * @param encoding 编码格式
     * @return
     */
    public static String readFromStream(InputStream is, String encoding) {
        String line = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, encoding))) {
            StringBuilder buffer = new StringBuilder();
            boolean isFirst = true;
            while ((line = reader.readLine()) != null) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    buffer.append("\n");
                }
                buffer.append(line);
            }
            return buffer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(is);
        }
    }

    /**
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     *
     * @param is
     * @return
     */
    public static String readFromStream(InputStream is) {
        String encoding = getFileEncodingCharset(is);
        String context = readFromStream(is, encoding);
        return context;
    }

    /**
     * 从文件流中读取文本行
     *
     * @param is
     * @param encoding 编码格式
     * @return 返回文本行列表
     */
    public static String[] readLinesFromStream(InputStream is, String encoding) {
        String line = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, encoding))) {
            List<String> list = new ArrayList<String>();
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
            return list.toArray(new String[0]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(is);
        }
    }

    /**
     * 从文件流中读取文本行
     *
     * @param is
     * @return 返回文本行列表
     */
    public static String[] readLinesFromStream(InputStream is) {
        String encoding = getFileEncodingCharset(is);
        String[] lines = readLinesFromStream(is, encoding);
        return lines;
    }

    /**
     * 写字符串到文件中
     *
     * @param file
     * @param content
     */
    public static void writeStringToFile(File file, String content) {
        mkFileParentDir(file);
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把classpath中的文件内容复制到指定的外部文件中
     *
     * @param classPathResourceName  classpath下资源文件
     * @param fileSystemResourceName 拷贝目标的系统文件名称
     */
    public static void copyClassPathResource(String classPathResourceName, String fileSystemResourceName) {
        copyClassPathResource(classPathResourceName, new File(fileSystemResourceName));
    }

    /**
     * 把classpath中的文件内容复制到指定的外部文件中
     *
     * @param classPathResourceName  classpath下资源文件
     * @param fileSystemResourceFile 拷贝目标的系统文件
     */
    public static void copyClassPathResource(String classPathResourceName, File fileSystemResourceFile) {
        mkFileParentDir(fileSystemResourceFile);
        ClassLoader loader = ResourceHelper.class.getClassLoader();
        try (InputStream resourceInputStream = loader.getResourceAsStream(classPathResourceName);
             OutputStream fileOutputStream = new FileOutputStream(fileSystemResourceFile)) {
            IOUtils.copy(resourceInputStream, fileOutputStream);
        } catch (IOException e) {
            throw new Test4JException(e);
        }
    }

    /**
     * 将classpath下的文件拷贝到fileSystemDirectoryName目录下
     *
     * @param classPathResourceName   classpath下的资源文件
     * @param fileSystemDirectoryName 拷贝目标的系统文件目录名
     */
    public static void copyClassPathResourceToDir(String classPathResourceName, String fileSystemDirectoryName) {
        String fileName = StringHelper.substringAfterLast(classPathResourceName, "/");
        String fileSystemResourceName = fileSystemDirectoryName + "/" + fileName;
        copyClassPathResource(classPathResourceName, fileSystemResourceName);
    }

    /**
     * Creates an URL that points to the given file.
     *
     * @param file The file, not null
     * @return The URL to the file, not null
     */
    public static URL getUrl(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new Test4JException("Unable to create URL for file " + file.getName(), e);
        }
    }

    /**
     * 查找文件
     *
     * @param claz
     * @param url
     * @return
     * @throws Exception
     */
    public static File findFile(final Class claz, final String url) throws Exception {
        if (url.startsWith("file:")) {
            return new File(url.replace("file:", ""));
        } else {
            URL file = ClassLoader.getSystemResource(url);
            String newUrl = url;
            if (file == null && claz != null) {
                newUrl = ClazzHelper.getPathFromPath(claz) + File.separatorChar + url;
                file = ClassLoader.getSystemResource(newUrl);
            }
            if (file == null) {
                throw new RuntimeException(String.format("can't find resource in classpath:%s", newUrl));
            } else {
                return new File(file.toURI());
            }
        }
    }

    /**
     * 文件缺省的编码格式
     *
     * @return
     */
    public static String defaultFileEncoding() {
        String encoding = System.getProperty("file.encoding", "utf-8");
        return encoding;
    }

    /**
     * 获得的文件的编码格式
     *
     * @param file
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getFileEncodingCharset(File file) {
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        detector.add(JChardetFacade.getInstance());

        try {
            Charset charset;
            try {
                charset = detector.detectCodepage(file.toURL());
            } catch (IllegalArgumentException e) {
                charset = Charset.forName(ResourceHelper.defaultFileEncoding());
                MessageHelper.warn("get file encoding error:" + e.getMessage() + ", use default encoding:"
                    + ResourceHelper.defaultFileEncoding());
            }
            String fileCharacterEnding = charset.name();
            return fileCharacterEnding;
        } catch (FileNotFoundException ue) {
            throw new RuntimeException(ue.getMessage(), ue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultFileEncoding();
        }
    }

    /**
     * 获得的文件流的编码格式
     *
     * @param is
     * @return
     */
    public static String getFileEncodingCharset(InputStream is) {
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        detector.add(JChardetFacade.getInstance());

        try {
            Charset charset = Charset.forName(ResourceHelper.defaultFileEncoding());
            try {
                charset = detector.detectCodepage(is, 2147483647);
            } catch (IllegalArgumentException e) {
                charset = Charset.forName(ResourceHelper.defaultFileEncoding());
                MessageHelper.warn("get file encoding error:" + e.getMessage() + ", use default encoding:"
                    + ResourceHelper.defaultFileEncoding());
            }
            String fileCharacterEnding = charset.name();
            return fileCharacterEnding;
        } catch (Throwable e) {
            e.printStackTrace();
            return defaultFileEncoding();
        }
    }

    /**
     * 根据文件名称返回文件输入流<br>
     * <br>
     * o file:开头读取文件系统 <br>
     * o classpath:开头读取classpath下的文件<br>
     * o 否则读取 classpath下文件
     *
     * @param klassLoader
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream getResourceAsStream(ClassLoader klassLoader, final String file) throws FileNotFoundException {
        String _file = file;
        if (file == null) {
            throw new RuntimeException("execute file name can't be null!");
        }
        if (file.startsWith("file:")) {
            _file = file.replaceFirst("file:", "");
            return new FileInputStream(new File(_file));
        } else {
            if (file.startsWith("classpath:")) {
                _file = file.replaceFirst("classpath:", "");
            }
            return klassLoader.getResourceAsStream(_file);
        }
    }

    /**
     * 返回文件的URL<br>
     * <br>
     * o file:开头读取文件系统 <br>
     * o classpath:开头读取classpath下的文件<br>
     * o 否则读取 classpath下文件
     *
     * @param filename
     * @return
     */
    @SuppressWarnings("deprecation")
    public static URL getResourceUrl(final String filename) {
        if (filename == null) {
            throw new RuntimeException("execute file name can't be null!");
        }
        if (filename.startsWith("file:")) {
            String _filename = filename.replaceFirst("file:", "");
            File file = new File(_filename);
            URL url = null;
            try {
                url = file.toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            return url;
        } else {
            String resource = filename;
            if (filename.startsWith("classpath:")) {
                resource = filename.replaceFirst("classpath:", "");
            }
            URL url = ResourceHelper.class.getClassLoader().getResource(resource);
            return url;
        }
    }

    public static String readFromClasspath(String file) {
        try (InputStream is = ResourceHelper.class.getClassLoader().getResourceAsStream(file)) {
            return readFromStream(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 先读取claz所在package下的url资源<br>
     * 如果没有找到，在读取跟classpath下的url资源
     *
     * @param clazz
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream getResourceAsStream(Class clazz, String fileName) throws FileNotFoundException {
        String packPath = ClazzHelper.getPathFromPath(clazz);
        String url = StringHelper.isBlank(packPath) ? fileName : packPath + "/" + fileName;
        InputStream is = ResourceHelper.class.getClassLoader().getResourceAsStream(url);

        if (is == null && StringHelper.isBlank(packPath) == false) {
            is = clazz.getClassLoader().getResourceAsStream(fileName);
        }

        if (is == null) {
            if (StringHelper.isBlank(packPath)) {
                throw new FileNotFoundException(String.format("can't find class path resource in in classpath: [%s].",
                    fileName));
            } else {
                throw new FileNotFoundException(String.format(
                    "can't find class path resource in in classpaths: [%s] and [%s]", url, fileName));
            }
        } else {
            return is;
        }
    }

    /**
     * 从文本文件中读取内容 <br>
     * <br>
     * o file:开头读取文件系统 <br>
     * o classpath:开头读取classpath下的文件<br>
     * o 否则读取跟 classpath下filePath文件
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static String readFromFile(ClassLoader klassLoader, String file) throws FileNotFoundException {
        InputStream stream = getResourceAsStream(klassLoader, file);
        return readFromStream(stream);
    }

    /**
     * 从文本文件中读取内容 <br>
     * <br>
     * o file:开头读取文件系统 <br>
     * o classpath:开头读取classpath下的文件<br>
     * o 否则读取跟 classpath下filePath文件
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static String readFromFile(String file) throws FileNotFoundException {
        InputStream stream = getResourceAsStream(ResourceHelper.class.getClassLoader(), file);
        return readFromStream(stream);
    }

    /**
     * 从文本文件中读取内容
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static String readFromFile(File file) throws FileNotFoundException {
        if (file == null) {
            throw new RuntimeException("the file can't be null.");
        } else if (file.exists() == false) {
            throw new RuntimeException(String.format("the file[%s] isn't exists.", file.getName()));
        }

        String encoding = ResourceHelper.getFileEncodingCharset(file);
        String content = readFromStream(new FileInputStream(file), encoding);
        return content;
    }

    /**
     * 读取文件内容<br>
     * * <br>
     * o file:开头读取文件系统 <br>
     * o classpath:开头读取classpath下的文件<br>
     * o 否则, 先读取clazz path下的fileName文件,再读取跟路径下的fileName文件
     *
     * @param clazz    文件所在的class package
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    public static String readFromFile(Class clazz, String fileName) throws FileNotFoundException {
        if (StringHelper.isBlank(fileName)) {
            throw new RuntimeException("file name can't be null.");
        }
        if (fileName.startsWith("file:") || fileName.startsWith("classpath:")) {
            String content = readFromFile(clazz.getClassLoader(), fileName);
            return content;
        } else {
            InputStream is = getResourceAsStream(clazz, fileName);

            String content = readFromStream(is);
            return content;
        }
    }

    /**
     * 按行读取文件
     *
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static String[] readLinesFromFile(ClassLoader klassLoader, String filePath) throws FileNotFoundException {
        InputStream stream = getResourceAsStream(klassLoader, filePath);
        String[] lines = readLinesFromStream(stream);
        return lines;
    }

    public static String[] readLinesFromFile(ClassLoader klassLoader, String filePath, String encoding) throws FileNotFoundException {
        InputStream stream = getResourceAsStream(klassLoader, filePath);
        String[] lines = readLinesFromStream(stream, encoding);
        return lines;
    }

    /**
     * 从文件中价值配置项<br>
     * <br>
     * o file:开头读取文件系统 <br>
     * o classpath:开头读取classpath下的文件<br>
     * o 否则读取 classpath下文件
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static Properties loadPropertiesFrom(ClassLoader klassLoader, final String file) {
        try (InputStream in = ResourceHelper.getResourceAsStream(klassLoader, file)) {
            Properties props = new Properties();
            props.load(in);
            return props;
        } catch (IOException e) {
            throw new RuntimeException(String.format("load properties from file[%s] error.", file), e);
        }
    }

    /**
     * 判断在claz对应的package下面是否存在名称为file的文件
     *
     * @param claz
     * @param file
     * @return
     */
    public static boolean isResourceExists(Class claz, String file) {
        String pack = ClazzHelper.getPathFromPath(claz);
        String path = StringHelper.isBlank(pack) ? file : pack + "/" + file;
        try {
            URL url = ClassLoader.getSystemResource(path);
            return url != null;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 过滤file文件中的注释代码
     *
     * @param is
     * @return
     */
    public static String convertStreamToSQL(InputStream is) {
        String line = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder buffer = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#") && !line.startsWith("--")) {
                    buffer.append(line + " ");
                }
            }
            return buffer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(is);
        }
    }

    /**
     * 获得javaFile相对于basePath的子路径<br>
     * 例如<br>
     * filePath = src/java/org/test4j/reflector/helper/ClazzHelper.java<br>
     * basepath = src/java/org/test4j<br>
     * 那么返回值就为 reflector/helper/ClazzHelper.java
     *
     * @param basePath 跟路径
     * @param filePath 文件绝对路径名称
     * @return
     */
    public static String getSuffixPath(File basePath, String filePath) {
        String prefixPath = basePath.getAbsolutePath().replaceAll("[\\/\\\\]+", "/");
        String fullPath = filePath.replaceAll("[\\/\\\\]+", "/");

        String suffixPath = filePath;
        if (fullPath.startsWith(prefixPath)) {
            suffixPath = fullPath.replace(prefixPath, "");
        }
        if (suffixPath.startsWith("/")) {
            suffixPath = suffixPath.substring(1);
        }
        return suffixPath;
    }

    /**
     * 删除文件或目录
     *
     * @param path
     * @return
     */
    public static boolean deleteFileOrDir(String path) {
        try {
            return deleteFileOrDir(new File(path));
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 删除文件或目录
     *
     * @param path
     * @return
     */
    public static boolean deleteFileOrDir(File path) {
        if (path.exists() == false) {
            return true;
        }

        if (path.isFile()) {
            path.delete();
            return true;
        }
        File[] files = path.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                deleteFileOrDir(file);
            } else {
                file.delete();
            }
        }
        return (path.delete());
    }

    /**
     * 创建文件file所在的父目录
     *
     * @param file
     */
    public static void mkFileParentDir(String file) {
        String _file = file.replaceAll("[\\/\\\\]+", "/");
        File path = new File(_file).getParentFile();
        if (path.exists() == false) {
            path.mkdirs();
        }
    }

    /**
     * 创建文件file所在的父目录
     *
     * @param file
     */
    public static void mkFileParentDir(File file) {
        File path = file.getParentFile();
        if (path.exists() == false) {
            path.mkdirs();
        }
    }

    /**
     * 是否是jar文件
     *
     * @param classRoot
     * @return
     */
    public static boolean isJarFile(File classRoot) {
        String fileName = classRoot.getName();
        if (fileName.length() < 4) {
            return false;
        } else {
            String surfix = fileName.substring(fileName.length() - 4);
            return surfix.equalsIgnoreCase(".jar");
        }
    }
}
