package cq.httpclients5.ext.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieAttributeHandler;
import org.apache.hc.client5.http.cookie.CookieOrigin;
import org.apache.hc.client5.http.cookie.CookieSpec;
import org.apache.hc.client5.http.cookie.CookieSpecFactory;
import org.apache.hc.client5.http.cookie.MalformedCookieException;
 
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.impl.cookie.CookieSpecBase;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicHeaderElement;
import org.apache.hc.core5.http.message.BasicHeaderValueFormatter;
import org.apache.hc.core5.http.message.BufferedHeader;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.Args;
import org.apache.hc.core5.util.CharArrayBuffer;

 

public class QtCookieSpecProvider implements CookieSpecFactory{

	@Override
	public CookieSpec create(HttpContext context) {

		return new CookieSpecBase() {
			
			
			
			
			@Override
			protected List<Cookie> parse(HeaderElement[] elems, CookieOrigin origin) throws MalformedCookieException {
				return super.parse(elems, origin);
			}

			@Override
			public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
				// TODO Auto-generated method stub
				super.validate(cookie, origin);
			}

			@Override
			public boolean match(Cookie cookie, CookieOrigin origin) {
				// TODO Auto-generated method stub
				return super.match(cookie, origin);
			}

			@Override
			public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<Header> formatCookies(List<Cookie> cookies) {
				// TODO Auto-generated method stub
				return null;
			}
			
		}; 
	
	}

}
