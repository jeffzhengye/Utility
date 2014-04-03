/**
 * 
 */
package org.dutir.util.net;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderGroup;

/**
 * @author shutrazh
 * 
 */
public class HttpClientUtils {
	public static HeaderGroup buildInternetExplorerHeader(String acceptLanguage) {
		return buildInternetExplorerHeader(acceptLanguage, true);
	}

	public static HeaderGroup buildInternetExplorerHeader(
			String acceptLanguage, boolean acceptEncoding) {
		HeaderGroup hg = new HeaderGroup();
		hg
				.addHeader(new Header(
						"Accept",
						"image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/x-shockwave-flash, */*"));
		hg.addHeader(new Header("Accept-Language", acceptLanguage));
		hg.addHeader(new Header("UA-CPU", "x86"));
		hg
				.addHeader(new Header("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322)"));
		if (acceptEncoding) {
			hg.addHeader(new Header("Accept-Encoding", "gzip, deflate"));
		}
		hg.addHeader(new Header("Connection", "Keep-Alive"));

		return hg;
	}
}
