package cq.httpclients5.ext.strategy;

 

import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.protocol.HttpContext;

import cq.httpclients5.ext.QtStr;

 

public class QtLinkedRedirectStrategy extends DefaultRedirectStrategy {

	
	
	
//	public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
//
//		HttpUriRequest redirect = super.getRedirect(request, response, context);
//		if (!redirect.headerIterator().hasNext()) {
//			redirect.setHeaders(request.getAllHeaders());
//		}
//		if (request.containsHeader(QtStr.REFERER))
//			redirect.removeHeaders(QtStr.REFERER);
//		redirect.addHeader(QtStr.REFERER, request.getRequestLine().getUri());
//		return redirect;
//	}

}
