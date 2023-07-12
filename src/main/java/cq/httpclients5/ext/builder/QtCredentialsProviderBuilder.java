package cq.httpclients5.ext.builder;

import java.util.List;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.impl.auth.CredentialsProviderBuilder;

import cq.httpclients5.QtHttpProxy;

public class QtCredentialsProviderBuilder {

	public static CredentialsProvider getCredentialsProvider(List<QtHttpProxy> qhp) {
		CredentialsProviderBuilder cpb= CredentialsProviderBuilder.create();
		qhp.forEach(qtProxy->{
			if(null!=qtProxy.getUserName()&&qtProxy.getUserName().length()>0) {
				cpb.add(new AuthScope(qtProxy.getHostName(), qtProxy.getPort()), qtProxy.getUserName(), qtProxy.getPassword().toCharArray());
			}
		});
		return cpb.build();
	}
}
