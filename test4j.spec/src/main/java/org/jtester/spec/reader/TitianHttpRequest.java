package org.jtester.spec.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;

import org.jtester.spec.ISpec;

/**
 * 梯田远程数据读取器
 * 
 * @author darui.wudr 2012-5-18 下午2:47:33
 */
public class TitianHttpRequest {
    private final String baseURL;

    private final String site;

    private final String projectID;

    public TitianHttpRequest(String baseURL, String site, String projectID) {
        this.baseURL = baseURL;
        this.site = site;
        this.projectID = projectID;
    }

    /**
     * 构造请求连接url
     * 
     * @param java
     * @return
     */
    public String titianURL(String java) {
        StringBuffer url = new StringBuffer(baseURL);
        if (baseURL.endsWith("/")) {
            url.append("jspec.do?");
        } else {
            url.append("/jspec.do?");
        }
        url.append("site=").append(site).append("&projectID=").append(projectID).append("&java=").append(java);
        return url.toString();
    }

    /**
     * 采用get方式远程连接梯田系统，获取用例描述信息
     * 
     * @param clazz
     * @return
     */
    public String sendGetRequest(Class<? extends ISpec> clazz) {
        String xml = sendGetRequest(clazz.getName());
        return xml;
    }

    /**
     * 采用get方式远程连接梯田系统，获取用例描述信息
     * 
     * @param clazz
     * @return
     */
    public String sendGetRequest(String clazz) {
        String url = this.titianURL(clazz);
        BufferedReader reader = null;
        try {
            reader = createHttpReader(url);
            StringBuffer message = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                message.append(line).append("\n");
            }
            return message.toString();
        } catch (ConnectException ce) {
            throw new RuntimeException("can't connect titian web system, url:" + url);
        } catch (Exception e) {
            throw new RuntimeException("remote[" + url + "] reader error.", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 根据远程连接url创建输出流
     * 
     * @param url
     * @return
     * @throws IOException
     */
    private static BufferedReader createHttpReader(String url) throws IOException {
        // Send a GET request to the servlet
        URL remoteURL = new URL(url);
        URLConnection conn = remoteURL.openConnection();
        // Get the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        return reader;
    }
}
