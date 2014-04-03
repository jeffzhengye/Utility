package org.dutir.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.net.URL;

import org.apache.http.HttpEntity;

import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;

import org.apache.http.entity.StringEntity;

import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class HttpUtil {
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlName = url + "?" + param;
			URL realUrl = new URL(urlName);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 建立实际的连接
			conn.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = conn.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "-->" + map.get(key));
			}
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += "\n" + line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定URL发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += "\n" + line;
			}
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// 提供主方法，测试发送GET请求和POST请求
	public static void main(String args[]) {
		// 发送GET请求
//		String s = HttpUtil.sendGet("http://localhost:8080/abc/login.jsp",
//				"user=test your web site&pass=abc");
//		s = HttpUtil.sendPost("http://localhost:8080/abc/login.jsp",
//				"pass=abc&pass=bcd&user=test your web sitet");
//		System.out.println(s);
		
		
//		Datas dts = new Gson().fromJson(s, Datas.class);
//		System.out.println(dts);
		
		JSonRequestor jSonRequestor = new JSonRequestor();

		jSonRequestor.doRequest();
		// 发送POST请求
		// String s1 = HttpUtil.sendPost("http://localhost:8000/abc/a.jsp",
		// "user=test&pass=abc");
		// System.out.println(s1);
	}
}

class Datas {
	private ArrayList<String> user;
	private ArrayList<String> pass;

	public ArrayList<String> getUser() {
		return user;
	}

	public ArrayList<String> getPass() {
		return pass;
	}

	public String toString() {
		return String.format("user:%s, pass:%s", user.get(0), pass.get(0));
	}
}

class Invoice {

	private String username;

	private String po_number;

	private int bill;

	public void setBill(int bill) {
		this.bill = bill;
	}

	public void setPo_number(String po_number) {
		this.po_number = po_number;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

class JSonRequestor {

	public JSonRequestor() {

	}

	public void doRequest() {

		URL serverURL = null;

		try {

			Invoice invoice = new Invoice();

			invoice.setBill(2000);

			invoice.setPo_number("2000");

			invoice.setUsername("edw");

			Gson g = new Gson();

			String json = g.toJson(invoice);

			System.out.println(json);

			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(
					"http://localhost:8080/EPay/CreateInvoice");

			StringEntity stringEntity = new StringEntity(json);

//			stringEntity.setContentType("application/json");

			httppost.setEntity(stringEntity);

			System.out
					.println("executing request " + httppost.getRequestLine());

			HttpResponse response = httpclient.execute(httppost);

			HttpEntity resEntity = response.getEntity();

			System.out.println("----------------------------------------");

			System.out.println(response.getStatusLine());

			if (resEntity != null) {

				System.out.println("Response content length: "
						+ resEntity.getContentLength());

				System.out.println("Chunked?: " + resEntity.isChunked());

			}

			EntityUtils.consume(resEntity);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public static void main(String[] args) {

		JSonRequestor jSonRequestor = new JSonRequestor();

		jSonRequestor.doRequest();

	}

}
