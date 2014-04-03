/**
 * Created on 2007-11-11 下午08:41:59
 */
package org.dutir.util.net;

import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author sutra
 * @see <a
 *      href="http://thinkbase.net/w/main/Wiki?HttpClient+POST+%E7%9A%84+UTF-8+%E7%BC%96%E7%A0%81%E9%97%AE%E9%A2%98">
 *      HttpClient POST 的 UTF-8 编码问题</a>
 */
public class CharSetPostMethod extends PostMethod {
	private String requestCharSet;

	public CharSetPostMethod(String uri, String requestCharSet) {
		super(uri);
		this.requestCharSet = requestCharSet;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.apache.commons.httpclient.methods.EntityEnclosingMethod#getRequestCharSet()
	 */
	@Override
	public String getRequestCharSet() {
		return this.requestCharSet;
	}
}
