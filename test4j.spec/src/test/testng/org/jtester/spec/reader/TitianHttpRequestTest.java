package org.jtester.spec.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

import mockit.Mock;

import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SuppressWarnings("unused")
public class TitianHttpRequestTest extends JTester {
	@Test
	public void testSendGetRequest_NotConnect() {
		new MockUp<TitianHttpRequest>() {
			@Mock
			public BufferedReader createHttpReader(String url) throws IOException {
				throw new ConnectException();
			}
		};
		try {
			new TitianHttpRequest("http://localhost:8080/titian", "CRM", "1").sendGetRequest("xxx");
			want.fail();
		} catch (Exception e) {
			String msg = e.getMessage();
			want.string(msg).contains("can't connect titian web system, url:http://");
		}
	}

	@Test(groups = "debug")
	public void testSendGetRequest() throws Exception {
		String xml = new TitianHttpRequest("http://localhost:8080/titian", "CRM", "1").sendGetRequest("xxx");
		String message = URLDecoder.decode(xml.substring(8), "UTF-8");
		want.string(message).notNull();
	}
}
