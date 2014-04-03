/**
 *  Created on 2006-7-14 0:32:35
 */
package org.dutir.util.net;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.HeaderGroup;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.cyberneko.html.parsers.DOMParser;
import org.dutir.util.LogFormatter;
import org.dutir.util.dom.DomNodeUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The http utility functions for returning document.
 * 
 * @author Joe
 * @version 1.0
 * 
 */
public class HttpDocument implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3668507850703993669L;

	private static final Logger log = LogFormatter
			.getLogger(HttpDocument.class);

	private transient HttpClient httpClient;

	private HeaderGroup requestHeaderGroup;

	private boolean manualCookie;

	private boolean followRedirects;

	private static String Default_encoding = "GBK";// default

	private String requestCharSet;

	private String encoding;

	/**
	 * 
	 */
	public HttpDocument(final HttpClient httpClient) {
		this.httpClient = httpClient;
		this.httpClient.setHttpConnectionFactoryTimeout(10000);
	}

	/**
	 * 
	 * @param httpClient
	 * @param requestHeaderGroup
	 */
	public HttpDocument(final HttpClient httpClient,
			final HeaderGroup requestHeaderGroup) {
		this(httpClient);
		this.requestHeaderGroup = requestHeaderGroup;
	}

	/**
	 * 
	 * @param httpClient
	 * @param requestHeaderGroup
	 * @param encoding
	 */
	public HttpDocument(final HttpClient httpClient,
			final HeaderGroup requestHeaderGroup, String encoding) {
		this(httpClient);
		this.requestHeaderGroup = requestHeaderGroup;
		this.encoding = encoding;
	}

	/**
	 * 
	 * @param httpClient
	 * @param manualCookie
	 */
	public HttpDocument(final HttpClient httpClient, final boolean manualCookie) {
		this(httpClient);
		this.manualCookie = manualCookie;
	}

	/**
	 * 
	 * @param httpClient
	 * @param encoding
	 */
	public HttpDocument(final HttpClient httpClient, final String encoding) {
		this(httpClient);
		this.encoding = encoding;
	}

	/**
	 * 
	 * @param httpClient
	 * @param manualCookie
	 * @param encoding
	 */
	public HttpDocument(final HttpClient httpClient,
			final boolean manualCookie, final String encoding) {
		this(httpClient);
		this.encoding = encoding;
		this.manualCookie = manualCookie;
	}

	/**
	 * 
	 * @param httpClient
	 * @param requestHeaderGroup
	 * @param manualCookie
	 */
	public HttpDocument(final HttpClient httpClient,
			final HeaderGroup requestHeaderGroup, final boolean manualCookie) {
		this(httpClient, requestHeaderGroup);
		this.manualCookie = manualCookie;
	}

	/**
	 * 
	 * @param httpClient
	 * @param requestHeaderGroup
	 * @param manualCookie
	 * @param encoding
	 */
	public HttpDocument(final HttpClient httpClient,
			final HeaderGroup requestHeaderGroup, final boolean manualCookie,
			final String encoding) {
		this(httpClient, requestHeaderGroup, manualCookie);
		this.encoding = encoding;
	}

	/**
	 * @return the httpClient
	 */
	public HttpClient getHttpClient() {
		return httpClient;
	}

	/**
	 * @return Returns the encoding.
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * <br>
	 * ����˵����ͨ��httpͷ�õ���ҳ������Ϣ <br>
	 * �������contentheade rhttpͷ <br>
	 * �������ͣ���ҳ����
	 */
	public static String getContentCharSet(Header contentheader) {
		String charset = null;
		if (contentheader != null) {
			HeaderElement values[] = contentheader.getElements();
			if (values.length == 1) {
				NameValuePair param = values[0].getParameterByName("charset");
				if (param != null) {
					charset = param.getValue();
				}
			}
		}
		return charset;
	}

	/**
	 * @return Returns the manualCookie.
	 */
	public boolean isManualCookie() {
		return manualCookie;
	}

	/**
	 * @return Returns the requestHeaderGroup.
	 */
	public HeaderGroup getRequestHeaderGroup() {
		return requestHeaderGroup;
	}

	/**
	 * @return the followRedirects
	 */
	public boolean isFollowRedirects() {
		return followRedirects;
	}

	/**
	 * @param followRedirects
	 *            the followRedirects to set
	 */
	public void setFollowRedirects(boolean followRedirects) {
		this.followRedirects = followRedirects;
	}

	/**
	 * @return requestCharSet
	 */
	public String getRequestCharSet() {
		return requestCharSet;
	}

	/**
	 * @param requestCharSet
	 *            Ҫ���õ� requestCharSet
	 * @see CharSetPostMethod
	 */
	public void setRequestCharSet(String requestCharSet) {
		this.requestCharSet = requestCharSet;
	}

	/**
	 * Execute `get' method.
	 * 
	 * @param url
	 * @return
	 */
	public Document get(String url) {
		GetMethod getMethod = new GetMethod(url);
		getMethod.setFollowRedirects(followRedirects);
		addRequestHeaderGroup(getMethod, requestHeaderGroup);
		if (manualCookie) {
			addCookies(getMethod, httpClient.getState().getCookies());
		}
		executeMethod(httpClient, getMethod);
		return getDocument(getMethod);
	}

	/**
	 * Execute `post' method.
	 * 
	 * @param action
	 * @param parameters
	 * @return
	 */
	public Document post(String action, NameValuePair[] parameters) {
		return post(action, parameters, null);
	}

	/**
	 * Execute `post' method.
	 * 
	 * @param action
	 * @param parameters
	 * @param requestHeaderGroup
	 * @return
	 */
	public Document post(String action, NameValuePair[] parameters,
			HeaderGroup requestHeaderGroup) {
		PostMethod postMethod;
		if (this.requestCharSet == null) {
			postMethod = new PostMethod(action);
		} else {
			postMethod = new CharSetPostMethod(action, this.requestCharSet);
		}
		addRequestHeaderGroup(postMethod, this.requestHeaderGroup);
		addRequestHeaderGroup(postMethod, requestHeaderGroup);
		if (manualCookie) {
			addCookies(postMethod, httpClient.getState().getCookies());
		}
		if (parameters != null) {
			postMethod.addParameters(parameters);
		}
		executeMethod(httpClient, postMethod);

		return getDocument(postMethod);
	}

	/**
	 * Execute `post' method.
	 * 
	 * @param action
	 * @param parameters
	 * @return
	 */
	public Document post(String action, List<NameValuePair> parameters) {
		return post(action, parameters, null);
	}

	/**
	 * Execute `post' method.
	 * 
	 * @param action
	 * @param parameters
	 * @param requestHeaderGroup
	 * @return
	 */
	public Document post(String action, List<NameValuePair> parameters,
			HeaderGroup requestHeaderGroup) {
		NameValuePair[] nvps = null;
		if (parameters != null) {
			nvps = new NameValuePair[parameters.size()];
			parameters.toArray(nvps);
		}
		return post(action, nvps, requestHeaderGroup);
	}

	/**
	 * Get response document from method.
	 * 
	 * @param method
	 * @return
	 */
	public Document getDocument(HttpMethod method) {
		try {
			return getDocumentInternal(method);
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * Get response document from a method.
	 * 
	 * @param method
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	private Document getDocumentInternal(HttpMethod method) throws Exception {
		Document document;
		// ����Ƿ��ض���
		int statuscode = method.getStatusCode();
		if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statuscode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statuscode == HttpStatus.SC_SEE_OTHER)
				|| (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			// ��ȡ�µ�URL��ַ
			Header header = method.getResponseHeader("location");
			if (header != null) {
				String newuri = header.getValue();
				if ((newuri == null) || (newuri.equals(""))) {
					newuri = "/";
				}
				org.apache.commons.httpclient.URI parentUri = method.getURI();
				if (!newuri.startsWith(parentUri.getScheme())) {
					String s = newuri.startsWith("/") ? newuri : parentUri
							.getAboveHierPath()
							+ newuri;
					// Really need to decode?
					s = java.net.URLDecoder.decode(s, "UTF-8");
					newuri = new org.apache.commons.httpclient.URI(parentUri
							.getScheme(), parentUri.getHost(), (s), "")
							.toString();
				}
				method.releaseConnection();
				document = get(newuri);
			} else {
				throw new Exception("Invalid redirect");
			}
		} else {
			System.out.println("URI: " + method.getURI().toString()
					+ "\ngetResponseBodyAsString: ");
			InputStream inputStream = method.getResponseBodyAsStream();

			try {
				DOMParser parser = new DOMParser();
				InputSource inputSource = new InputSource();
				String encoding = getContentCharSet(method
						.getResponseHeader("Content-Type"));
				if (encoding == null) {
					encoding = Default_encoding;
				}
				inputSource.setEncoding(encoding);
				inputSource.setByteStream(inputStream);
				parser.parse(inputSource);
				document = parser.getDocument();
			} finally {
				inputStream.close();
				method.releaseConnection();
			}
		}
		return document;
	}

	public String getRedirectURL(String url) throws Exception {

		GetMethod method = new GetMethod(url);
		method.setFollowRedirects(followRedirects);
		addRequestHeaderGroup(method, requestHeaderGroup);
		if (manualCookie) {
			addCookies(method, httpClient.getState().getCookies());
		}
		executeMethod(httpClient, method);

		// ����Ƿ��ض���
		int statuscode = method.getStatusCode();
		if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statuscode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statuscode == HttpStatus.SC_SEE_OTHER)
				|| (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			// ��ȡ�µ�URL��ַ
			Header header = method.getResponseHeader("location");
			if (header != null) {
				String newuri = header.getValue();
				if ((newuri == null) || (newuri.equals(""))) {
					newuri = "/";
				}
				org.apache.commons.httpclient.URI parentUri = method.getURI();
				if (!newuri.startsWith(parentUri.getScheme())) {
					String s = newuri.startsWith("/") ? newuri : parentUri
							.getAboveHierPath()
							+ newuri;
					// Really need to decode?
					s = java.net.URLDecoder.decode(s, "UTF-8");
					newuri = new org.apache.commons.httpclient.URI(parentUri
							.getScheme(), parentUri.getHost(), (s), "")
							.toString();
					System.out.println("new url:" + newuri);
				}
				method.releaseConnection();
				return newuri;
			} else {
//				throw new Exception("Invalid redirect");
				return null;
			}
		}
		return null;
	}
	

	
	public byte[] getDocumentInterna(String url) throws Exception {

		GetMethod method = new GetMethod(url);
		method.setFollowRedirects(followRedirects);
		addRequestHeaderGroup(method, requestHeaderGroup);
		if (manualCookie) {
			addCookies(method, httpClient.getState().getCookies());
		}
		executeMethod(httpClient, method);

		// ����Ƿ��ض���
		int statuscode = method.getStatusCode();
		if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statuscode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statuscode == HttpStatus.SC_SEE_OTHER)
				|| (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			// ��ȡ�µ�URL��ַ
			Header header = method.getResponseHeader("location");
			if (header != null) {
				String newuri = header.getValue();
				if ((newuri == null) || (newuri.equals(""))) {
					newuri = "/";
				}
				org.apache.commons.httpclient.URI parentUri = method.getURI();
				if (!newuri.startsWith(parentUri.getScheme())) {
					String s = newuri.startsWith("/") ? newuri : parentUri
							.getAboveHierPath()
							+ newuri;
					// Really need to decode?
					s = java.net.URLDecoder.decode(s, "UTF-8");
					newuri = new org.apache.commons.httpclient.URI(parentUri
							.getScheme(), parentUri.getHost(), (s), "")
							.toString();
					System.out.println("new url:" + newuri);
				}
				method.releaseConnection();
				return getDocumentInterna(newuri);
			} else {
				throw new Exception("Invalid redirect");
			}
		} else {
//			System.out.println("URI: " + method.getURI().toString()
//					+ "\ngetResponseBodyAsString: ");
			InputStream inputStream = method.getResponseBodyAsStream();
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte buf[] = new byte[1024];
				int len;
				while((len = inputStream.read(buf)) != -1){
					baos.write(buf, 0, len);
				}
				return baos.toByteArray();
			} finally {
				inputStream.close();
				method.releaseConnection();
			}
		}
	}

	public byte[] getDocumentInterna(String url, int rediTimes) throws Exception {

		GetMethod method = new GetMethod(url);
		method.setFollowRedirects(followRedirects);
		addRequestHeaderGroup(method, requestHeaderGroup);
		if (manualCookie) {
			addCookies(method, httpClient.getState().getCookies());
		}
		executeMethod(httpClient, method);

		// ����Ƿ��ض���
		int statuscode = method.getStatusCode();
		if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statuscode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statuscode == HttpStatus.SC_SEE_OTHER)
				|| (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			// ��ȡ�µ�URL��ַ
			Header header = method.getResponseHeader("location");
			if (header != null) {
				String newuri = header.getValue();
				if ((newuri == null) || (newuri.equals(""))) {
					newuri = "/";
				}
				org.apache.commons.httpclient.URI parentUri = method.getURI();
				if (!newuri.startsWith(parentUri.getScheme())) {
					String s = newuri.startsWith("/") ? newuri : parentUri
							.getAboveHierPath()
							+ newuri;
					// Really need to decode?
					s = java.net.URLDecoder.decode(s, "UTF-8");
					newuri = new org.apache.commons.httpclient.URI(parentUri
							.getScheme(), parentUri.getHost(), (s), "")
							.toString();
					System.out.println("new url:" + newuri);
				}
				method.releaseConnection();
				if(rediTimes < 3){
					return getDocumentInterna(newuri, rediTimes +1);
				}else{
					return new byte[0];
				}
			} else {
				throw new Exception("Invalid redirect");
			}
		} else {
			InputStream inputStream = method.getResponseBodyAsStream();
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte buf[] = new byte[1024];
				int len;
				while((len = inputStream.read(buf)) != -1){
					baos.write(buf, 0, len);
				}
				return baos.toByteArray();
			} finally {
				inputStream.close();
				method.releaseConnection();
			}
		}
	}

	/**
	 * Convert the cookies to header format.
	 * 
	 * @param cookies
	 * @return
	 */
	private static String cookieToHeaderString(Cookie[] cookies) {
		StringBuffer ret = new StringBuffer();
		for (Cookie cookie : cookies) {
			ret.append(cookie.getName()).append("=").append(cookie.getValue())
					.append("; ");
		}
		String s = ret.toString();
		return s;
	}

	/**
	 * Add headers to http method.
	 * 
	 * @param method
	 * @param requestHeaderGroup
	 */
	public static void addRequestHeaderGroup(HttpMethod method,
			HeaderGroup requestHeaderGroup) {
		if (requestHeaderGroup != null) {
			for (Header header : requestHeaderGroup.getAllHeaders()) {
				method.addRequestHeader(header);
			}
		}
	}

	/**
	 * Execute http method.
	 * 
	 * @param httpClient
	 * @param method
	 */
	public static void executeMethod(HttpClient httpClient, HttpMethod method) {
		try {
			httpClient.executeMethod(method);
		} catch (HttpException e) {
			String url = null;
			try {
				url = method.getURI().toString();
			} catch (URIException e1) {
			}
			// throw new BlogMoverRuntimeException("HttpException: url=" + url,
			// e);
		} catch (IOException e) {
			String url = null;
			try {
				url = method.getURI().toString();
			} catch (URIException e1) {
			}
			// throw new BlogMoverRuntimeException("HttpException: url=" + url,
			// e);
		}
	}

	/**
	 * Add cookies to http method.
	 * 
	 * @param method
	 * @param cookies
	 */
	public static void addCookies(HttpMethod method, Cookie[] cookies) {
		if (cookies != null) {
			Header header = method.getRequestHeader("Cookie");
			if (header == null) {
				header = new Header();
			}
			String value = header.getValue();
			if (value == null) {
				value = "";
			}
			if (value.trim().endsWith(";") == false) {
				value += "; ";
			}
			method.addRequestHeader("Cookie", value
					+ cookieToHeaderString(cookies));
		}
	}

	public static void main(String args[]) throws Exception {
		HttpDocument httpdoc = new HttpDocument(new HttpClient());

		String url = "http://blacktree.cocoaforge.com/forums/viewtopic.php?t=7243";
//		url = URLEncoder.encode(url, "utf8");
		
		
		 org.w3c.dom.Document domdoc = httpdoc.get(url);
		 byte[] content = httpdoc.getDocumentInterna(url);
		 String scontext =  new String(content, "utf8");
//		 String scontext = DomNodeUtils.getText(domdoc);
		 System.out.println(scontext);

		 System.exit(1);

		FileInputStream inputStream = new FileInputStream(url);
		// org.w3c.dom.Document domdoc =
		// domdoc.getTextContent();
		DOMParser parser = new DOMParser();
		InputSource inputSource = new InputSource();
		// inputSource.setEncoding("");
		inputSource.setByteStream(inputStream);
		parser.parse(inputSource);
		org.w3c.dom.Document domdoc1 = parser.getDocument();
		String text = null;
		try {
			StringBuffer buf = new StringBuffer();
			DomNodeUtils.getText(buf, domdoc1, Integer.MAX_VALUE);
			System.out.println(buf.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
