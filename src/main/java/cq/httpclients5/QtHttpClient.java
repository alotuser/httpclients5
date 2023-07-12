package cq.httpclients5;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.DnsResolver;
import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.SystemDefaultDnsResolver;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.StandardAuthScheme;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.auth.CredentialsProviderBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.hc.client5.http.impl.io.ManagedHttpClientConnectionFactory;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.io.ManagedHttpClientConnection;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.config.CharCodingConfig;
import org.apache.hc.core5.http.config.Http1Config;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.impl.io.DefaultClassicHttpResponseFactory;
import org.apache.hc.core5.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.hc.core5.http.impl.io.DefaultHttpResponseParser;
import org.apache.hc.core5.http.io.HttpConnectionFactory;
import org.apache.hc.core5.http.io.HttpMessageParser;
import org.apache.hc.core5.http.io.HttpMessageParserFactory;
import org.apache.hc.core5.http.io.HttpMessageWriterFactory;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.message.BasicLineParser;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.message.LineParser;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.CharArrayBuffer;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import cq.httpclients5.ext.QtDefaultClient;
import cq.httpclients5.ext.builder.QtCredentialsProviderBuilder;
import cq.httpclients5.ext.enums.ResultType;
import cq.httpclients5.ext.strategy.QtDefaultRedirectStrategy;
import cq.httpclients5.ext.strategy.QtLinkedRedirectStrategy;
import cq.httpclients5.util.IOUtil;

 

/***
 * 简易HttpClient
 * 
 * @author ChileQi
 * @since 2017年8月4日 17:15:43
 */
public class QtHttpClient {

	public String defaultUserAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36 QtHttpClient/1.1.0";
	public CookieStore defaultCookieStore = new BasicCookieStore();// Use custom cookie store if necessary.
	private PoolingHttpClientConnectionManager defaultConnManager;
	private ConnectionKeepAliveStrategy defaultKeepAliveStrategy;
	private CredentialsProvider defaultCredentialsProvider;
	private RequestConfig defaultRequestConfig;
	private QtHttpProxy defaultProxy;
	private String defaultCookieType="easy";
	private CloseableHttpClient httpclient;
	private QtHttpProxy qtProxy; 
	private boolean isRun = false;
	public boolean isIgnoeSSL=true;
	public int defaultMaxTotal = 1000;
	public int defaultMaxPerRoute = 10;
	public int maxPerRoute = 20;
	public int defaultSocketTimeout = 5000;//单位秒
	public int defaultConnectTimeout = 5000;
	public int defaultConnectionRequestTimeout = 5000;
	public int defaultKeepAliveTimeout;

	private QtDefaultClient qtDefaultClient;
	private List<QtHttpProxy> qtProxyList;
	 
	public QtHttpClient() {
		httpclient=customHttpClient(null);
	}
	
	public QtHttpClient(QtDefaultClient defaultClient) {
		this.qtDefaultClient=defaultClient;
		httpclient=customHttpClient(null);
	}
	
	/**
	 * 初始化QtHttpClient
	 * @param qtProxy qtProxy
	 */
	public QtHttpClient(QtHttpProxy qtProxy) {
		this.qtProxy=qtProxy;
		HttpHost proxyHttpHost = null;
		if (null != qtProxy) {
			proxyHttpHost = (new HttpHost(qtProxy.getHostName(), qtProxy.getPort()));
		}
		httpclient=customHttpClient(proxyHttpHost);
		if (null != qtProxy) {
			addAuthProxy(qtProxy);
		}
	}
	
	public static QtHttpClient create() {
		return new QtHttpClient(QtDefaultClient.empty());
	}
	
	public QtHttpClient build() {
		HttpHost proxyHttpHost = null;
		if (null != qtProxy) {
			proxyHttpHost = (new HttpHost(qtProxy.getHostName(), qtProxy.getPort()));
		}
		httpclient=customHttpClient(proxyHttpHost);
		if (null != qtProxy) {
			addAuthProxy(qtProxy);
		}
		return this;
	}
	
	/**
	 * 自定义方法
	 * 
	 * @param proxyHttpHost 代理ip
	 */
	private CloseableHttpClient customHttpClient(final HttpHost defaultProxyHttpHost) {
		// Use custom message parser / writer to customize the way HTTP
		// messages are parsed from and written out to the data stream.
		 final HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {

            @Override
            public HttpMessageParser<ClassicHttpResponse> create(final Http1Config h1Config) {
                final LineParser lineParser = new BasicLineParser() {
                    @Override
                    public Header parseHeader(final CharArrayBuffer buffer) {
                        try {
                            return super.parseHeader(buffer);
                        } catch (final ParseException ex) {
                            return new BasicHeader(buffer.toString(), null);
                        }
                    }
                };
                return new DefaultHttpResponseParser(lineParser, DefaultClassicHttpResponseFactory.INSTANCE, h1Config);
            }
        };
        
        final HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

        // Create HTTP/1.1 protocol configuration
        final Http1Config h1Config = Http1Config.custom()
                .setMaxHeaderCount(200)
                .setMaxLineLength(2000)
                .build();
        // Create connection configuration
        final CharCodingConfig connectionConfig = CharCodingConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(StandardCharsets.UTF_8)
                .build();

        // Use a custom connection factory to customize the process of
        // initialization of outgoing HTTP connections. Beside standard connection
        // configuration parameters HTTP connection factory can define message
        // parser / writer routines to be employed by individual connections.
        final HttpConnectionFactory<ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(h1Config, connectionConfig, requestWriterFactory, responseParserFactory);

		// Client HTTP connection objects when fully initialized can be bound to
		// an arbitrary network socket. The process of network socket initialization,
		// its connection to a remote address and binding to a local one is controlled
		// by a connection socket factory.

		// SSL context for secure connections can be created either based on
		// system or application specific properties.
		SSLConnectionSocketFactory sslConnectionSocketFactorys;
		if(isIgnoeSSL) {
			try {
				//SSLContext sslcontext=SSLContextBuilder.create().loadTrustMaterial(null, new TrustAllStrategy()).build();
				sslConnectionSocketFactorys=new SSLConnectionSocketFactory(SSLContextBuilder.create().loadTrustMaterial(null, new TrustAllStrategy()).build(),NoopHostnameVerifier.INSTANCE);
			} catch (Exception e) {//注意：走默认方式
				sslConnectionSocketFactorys=new SSLConnectionSocketFactory(SSLContexts.createDefault());
			}
		}else {
			sslConnectionSocketFactorys=new SSLConnectionSocketFactory(SSLContexts.createSystemDefault());
		}
		
		// Create a registry of custom connection socket factories for supported
		// protocol schemes.
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
																				 .register("http" , PlainConnectionSocketFactory.INSTANCE)
																				 .register("https", sslConnectionSocketFactorys)
																				 .build();
//		//BrowserCompatSpec
//		CookieSpecProvider easySpecProvider = new QtCookieSpecProvider();
//		PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.getDefault();  
//		// Create a registry of custom cookie for supported
//		final Registry<CookieSpecFactory> defaultCookieSpecRegistry = RegistryBuilder.<CookieSpecFactory>create()
//																		 .register(CookieSpecs.DEFAULT,(CookieSpecProvider) new DefaultCookieSpecProvider(publicSuffixMatcher))  
//																		 .register(CookieSpecs.STANDARD,new RFC6265CookieSpecProvider(publicSuffixMatcher))  
//																		 .register(defaultCookieType, easySpecProvider)  
//																		 .build();  
		
 
		
		// Use custom DNS resolver to override the system DNS resolution. $$$
		DnsResolver dnsResolver = new SystemDefaultDnsResolver() {
			@Override
			public InetAddress[] resolve(final String host) throws UnknownHostException {
				if (host.equalsIgnoreCase("myhost")) {
					return new InetAddress[] { InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 }) };
				} else {
					return super.resolve(host);
				}
			}
		};

		// Create a connection manager with custom configuration.
		
		
		defaultConnManager = new PoolingHttpClientConnectionManager( socketFactoryRegistry, PoolConcurrencyPolicy.STRICT, PoolReusePolicy.LIFO, TimeValue.ofMinutes(5),null, dnsResolver, connFactory);
		// Create socket configuration
		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).setSoKeepAlive(true).setSoTimeout(defaultSocketTimeout,TimeUnit.SECONDS).build();
		// Configure the connection manager to use socket configuration either
		// by default or for a specific host.
		defaultConnManager.setDefaultSocketConfig(socketConfig);
        // Validate connections after 10 sec of inactivity
		defaultConnManager.setDefaultConnectionConfig(ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(defaultConnectTimeout))
                .setSocketTimeout(Timeout.ofSeconds(defaultSocketTimeout))
                .setValidateAfterInactivity(TimeValue.ofSeconds(1000))
                .setTimeToLive(TimeValue.ofHours(1))
                .build());
        // Use TLS v1.3 only
		defaultConnManager.setDefaultTlsConfig(TlsConfig.custom()
                .setHandshakeTimeout(Timeout.ofSeconds(30))
                .setSupportedProtocols(TLS.V_1_3)
                .build());
		// Configure total max or per route limits for persistent connections
		// that can be kept in the pool or leased by the connection manager.
		defaultConnManager.setMaxTotal(defaultMaxTotal);
		defaultConnManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
		defaultConnManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), maxPerRoute);// $$$
		
		// Use custom credentials provider if necessary.
		defaultCredentialsProvider = CredentialsProviderBuilder.create().build();

		// Create global request configuration
		defaultRequestConfig = RequestConfig.custom()
	            .setCookieSpec(StandardCookieSpec.STRICT)
	            .setExpectContinueEnabled(true)
	            .setTargetPreferredAuthSchemes(Arrays.asList(StandardAuthScheme.NTLM, StandardAuthScheme.DIGEST))
	            .setProxyPreferredAuthSchemes(Collections.singletonList(StandardAuthScheme.BASIC))
	            .build();

		defaultKeepAliveStrategy = new DefaultConnectionKeepAliveStrategy() {
			@Override
			public TimeValue getKeepAliveDuration(HttpResponse response, HttpContext context) {
				TimeValue keepAlive = super.getKeepAliveDuration(response, context);
				// HttpHost target = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
				// target.getHostName()
				if (keepAlive.getDuration() == -1||defaultKeepAliveTimeout>0) {
					// 如果服务器没有设置keep-alive这个参数，我们就把它设置成1分钟
					keepAlive = TimeValue.ofSeconds(defaultConnectTimeout);
				}
				return keepAlive;
			}
		};
		
		//Lookup<CookieSpecProvider> cookieSpecRegistry=new Lookup<CookieSpecProvider>() {public CookieSpecProvider lookup(String s) {System.out.println(s);return null;}};
		//Create an HttpClient with the given custom dependencies and configuration.
		HttpClientBuilder defaultHttpClientBuilder=HttpClients.custom()
				.setConnectionManager(defaultConnManager)
				.setDefaultRequestConfig(defaultRequestConfig)
				//.setDefaultCookieSpecRegistry(defaultCookieSpecRegistry)
				.setDefaultCookieStore(defaultCookieStore)
				.setDefaultCredentialsProvider(defaultCredentialsProvider)
				.setKeepAliveStrategy(defaultKeepAliveStrategy)
				.setUserAgent(defaultUserAgent)
				.setProxy(defaultProxyHttpHost);
		
		if(null!=qtDefaultClient&&qtDefaultClient.referers!=null) {
			switch (qtDefaultClient.referers) {
			case DEFAULT:
				defaultHttpClientBuilder.setRedirectStrategy(new QtDefaultRedirectStrategy());
				break;
			case LINKED:
			default:
				defaultHttpClientBuilder.setRedirectStrategy(new QtLinkedRedirectStrategy());
				break;
			}
		}
		return defaultHttpClientBuilder.build();
	}

	/**
	 * GET(Main)
	 * 
	 * @param request request
	 * @return QtHttpResult
	 * @throws ClientProtocolException ClientProtocolException
	 * @throws IOException IOException
	 */
	public QtHttpResult get(QtHttpRequest request) throws ClientProtocolException, IOException {
		
		QtHttpResult qhr = new QtHttpResult();
		defaultKeepAliveTimeout = request.keepAlive;
		String url = request.url;
		HttpHost otherProxyHttpHost = null;
		if (null != request.proxy) {
			otherProxyHttpHost = new HttpHost(request.proxy.getHostName(), request.proxy.getPort());
		}

		try {
			HttpGet httpget = new HttpGet(url);
			// Request configuration can be overridden at the request level.
			// They will take precedence over the one set at the client level.
			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig) 
					.setConnectionRequestTimeout(Timeout.ofSeconds(request.getConnectionRequestTimeout()))
					.setProxy(otherProxyHttpHost)
					.setRedirectsEnabled(request.redirectsEnabled)
					.setCookieSpec(defaultCookieType).build();
			httpget.setConfig(requestConfig);
			request.headers.getHeaders().forEach((key, value) -> {
				httpget.addHeader(key, value);
			});
			// Execution context can be customized locally.
			HttpClientContext context = HttpClientContext.create();
			// Contextual attributes set the local context level will take
			// precedence over those set at the client level.
			if (null != request.cookieStore && !request.cookieStore.getCookies().isEmpty()) {
				context.setCookieStore(request.cookieStore);
			}
			context.setCredentialsProvider(defaultCredentialsProvider);
			
			qhr=httpclient.execute(httpget, context,response->{
				return getHttpResult(request,context, response);
			});
			
		} finally {
			closeHttpClient();
		}
		return qhr;
	}

 

	/**
	 * GET
	 * 
	 * @param url url
	 * @param proxyIP proxyIP
	 * @param proxyPort proxyPort
	 * @return QtHttpResult
	 * @throws ClientProtocolException ClientProtocolException
	 * @throws IOException IOException
	 */
	public QtHttpResult get(String url, String proxyIP, int proxyPort) throws ClientProtocolException, IOException {

		QtHttpRequest request = new QtHttpRequest(url) {
			{
				proxy = new QtHttpProxy(proxyIP, proxyPort);
			}
		};

		
		return get(request);

	}

	/**
	 * GET
	 * 
	 * @param url url
	 * @return QtHttpResult
	 * @throws ClientProtocolException ClientProtocolException
	 * @throws IOException  IOException
	 */
	public QtHttpResult get(String url) throws ClientProtocolException, IOException {

		QtHttpRequest request = new QtHttpRequest(url) {
			{
				if (null != defaultProxy) {
					proxy = new QtHttpProxy(defaultProxy.getHostName(), defaultProxy.getPort());
				}
			}
		};
		return get(request);

	}

	/**
	 * POST(main)
	 * 
	 * @param request request
	 * @return QtHttpResult
	 * @throws ClientProtocolException ClientProtocolException
	 * @throws IOException IOException
	 */
	public QtHttpResult post(QtHttpRequest request) throws ClientProtocolException, IOException {
		defaultKeepAliveTimeout = request.keepAlive;
		String url = request.url;
		ContentType contentType = request.contentType;
		List<NameValuePair> nvps = request.formData;
		HttpHost otherProxyHttpHost = null;
		if (null != request.proxy) {
			otherProxyHttpHost = new HttpHost(request.proxy.getHostName(), request.proxy.getPort());
		}
		QtHttpResult qhr = new QtHttpResult();
		try {
			// List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			// nvps.add(new BasicNameValuePair("username", "vip"));
			// nvps.add(new BasicNameValuePair("password", "secret"));

			// Request configuration can be overridden at the request level.
			// They will take precedence over the one set at the client level.
			RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
					//.setSocketTimeout(request.getSocketTimeout())
					//.setConnectTimeout(Timeout.ofSeconds(request.getConnectTimeout())
					.setConnectionRequestTimeout(Timeout.ofSeconds(request.getConnectionRequestTimeout()))
					.setProxy(otherProxyHttpHost)
					 
					.setRedirectsEnabled(request.redirectsEnabled)
					.setCookieSpec(defaultCookieType)
					.build();
			HttpPost httppost = new HttpPost(url);
			httppost.setConfig(requestConfig);
			// Header
			if (null != request.headers) {
				request.headers.getHeaders().forEach(httppost::setHeader);
			}
			
			//File
			if (null != request.postFile&&!request.postFile.isEmpty()) {
				/*InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(request.postFile), -1, ContentType.APPLICATION_OCTET_STREAM);
				reqEntity.setChunked(true);
				// It may be more appropriate to use FileEntity class in this particular
				// instance but we are using a more generic InputStreamEntity to demonstrate
				// the capability to stream out data from any arbitrary source
				//
				// FileEntity entity = new FileEntity(file, "binary/octet-stream");
				// FileEntity entity = new FileEntity(request.postFile, contentType.APPLICATION_OCTET_STREAM);
				httppost.setEntity(reqEntity);*/
				// Post files
				MultipartEntityBuilder multipartEntityBuilder=MultipartEntityBuilder.create();
				
				
				request.postFile.forEach(file->{multipartEntityBuilder.addBinaryBody(file.getName(), file);});
				if(null != nvps && !nvps.isEmpty()){
					nvps.forEach(nvp->{multipartEntityBuilder.addPart(nvp.getName(), new StringBody(nvp.getValue(), contentType));});
				}
				HttpEntity reqEntity = multipartEntityBuilder.build();
				httppost.setEntity(reqEntity);
			}else{
				// Post form
				if (null != nvps && !nvps.isEmpty()) {
					httppost.setEntity(new UrlEncodedFormEntity(nvps, Charset.forName(request.charset)));
				}
				// Post text,json,xml...
				if (null != request.postData && !request.postData.isEmpty()) {
					StringEntity reqEntity = new StringEntity(request.postData, contentType);
					httppost.setEntity(reqEntity);
				}
			}
			// Execution context can be customized locally.
			HttpClientContext context = HttpClientContext.create();
			// Contextual attributes set the local context level will take
			// precedence over those set at the client level.
			if (null != request.cookieStore && !request.cookieStore.getCookies().isEmpty()) {
				context.setCookieStore(request.cookieStore);
			}
			context.setCredentialsProvider(defaultCredentialsProvider);

			qhr = httpclient.execute(httppost,response->{
				return getHttpResult(request,context, response);
			});
			 
		} finally {
			closeHttpClient();
		}
		return qhr;
	}


	/**
	 * POST提交参数
	 * @param url url
	 * @param nameValues nameValues
	 * @return QtHttpResult
	 * @throws ClientProtocolException ClientProtocolException
	 * @throws IOException IOException
	 */
	public QtHttpResult post(String url,Map<String,String> nameValues) throws ClientProtocolException, IOException {
		
		QtHttpRequest request = new QtHttpRequest(url) {
			{
				timeout=200000;
				if (null != defaultProxy) {
					proxy = new QtHttpProxy(defaultProxy.getHostName(), defaultProxy.getPort());
				}
			}
		};
		if(null!=nameValues) {
			nameValues.forEach((name,value)->{
				request.formData.add(new BasicNameValuePair(name, value));
			});
		}
		return post(request);
	}
	
	/**
	 * POST提交参数
	 * @param url url
	 * @param bodyStr bodyStr
	 * @return QtHttpResult
	 * @throws ClientProtocolException ClientProtocolException
	 * @throws IOException IOException
	 */
	public QtHttpResult post(String url,String bodyStr) throws ClientProtocolException, IOException {
		
		QtHttpRequest request = new QtHttpRequest(url) {
			{
				timeout=200000;
				postData=bodyStr;
				if (null != defaultProxy) {
					proxy = new QtHttpProxy(defaultProxy.getHostName(), defaultProxy.getPort());
				}
			}
		};
		
		return post(request);
	}

	/**
	 * 根据文件路径提交
	 * @param url url
	 * @param filePaths filePaths
	 * @throws ClientProtocolException ClientProtocolException
	 * @throws IOException IOException
	 * @return QtHttpResult
	 */
	public QtHttpResult post(String url, String... filePaths) throws ClientProtocolException, IOException {
		return post(url,null,filePaths);
	}
	/**
	 * 根据文件路径和参数提交
	 * @param url url
	 * @param nameValues nameValues
	 * @param filePaths filePaths
	 * @return QtHttpResult
	 * @throws ClientProtocolException ClientProtocolException
	 * @throws IOException IOException
	 */
	public QtHttpResult post(String url,Map<String,String> nameValues,String... filePaths) throws ClientProtocolException, IOException {
		List<File> files =new ArrayList<File>();
		for (String filePath : filePaths) {
			files.add(new File(filePath));
		}
		return post(url,nameValues,files);
	}

	/**
	 * 根据文件路径和参数提交
	 * @param url url
	 * @param nameValues nameValues
	 * @param files files
	 * @return QtHttpResult
	 * @throws ClientProtocolException ClientProtocolException
	 * @throws IOException IOException
	 */
	public QtHttpResult post(String url,Map<String,String> nameValues,Collection<? extends File> files) throws ClientProtocolException, IOException {
		
		QtHttpRequest request = new QtHttpRequest(url) {
			{
				postFile =files;
				timeout=200000;
				if (null != defaultProxy) {
					proxy = new QtHttpProxy(defaultProxy.getHostName(), defaultProxy.getPort());
				}
			}
		};
		
		if(nameValues!=null)
			nameValues.forEach((name,value)->{request.formData.add(new BasicNameValuePair(name, value));});
		
		return post(request);
	}

	/**
	 * 根据文件路径和参数提交
	 * @param url url
	 * @param nameValues nameValues
	 * @param files files
	 * @return QtHttpResult
	 * @throws ClientProtocolException ClientProtocolException
	 * @throws IOException IOException
	 */
	public QtHttpResult post(String url,Map<String,String> nameValues,File... files) throws ClientProtocolException, IOException {
		
		final List<File> arrayList =  new ArrayList<>(files.length);
		Collections.addAll(arrayList, files);
		
		return post(url,nameValues, arrayList);
	}

	/**
	 * 只执行一次(注)
	 * 
	 * @param callBack callBack
	 * @throws IOException IOException
	 */
	public void runs(QtHttpCallBack callBack) throws IOException {
		try {
			isRun = true;
			callBack.completed(this);
		} finally {
			httpclient.close();
		}
	}

	/**
	 * 设置默认代理
	 * @param hostName
	 * @param port
	 */
	public void setAuthProxy(String hostName, int port) {
		defaultProxy = new QtHttpProxy(hostName, port);
		addAuthProxy(defaultProxy);
	}
	
	/**
	 * 设置默认代理
	 * 
	 * @param hostName hostName
	 * @param port   port
	 * @param userName userName
	 * @param password password
	 */
	public void setAuthProxy(String hostName, int port, String userName, String password) {
		defaultProxy = new QtHttpProxy(hostName, port, userName, password);
		addAuthProxy(defaultProxy);
	}

	/**
	 * 设置代理（可以多个）
	 * 
	 * @param hostName host地址
	 * @param port 端口号
	 * @param userName 账号名称
	 * @param password 账号密码
	 */
	public void addAuthProxy(String hostName, int port, String userName, String password) {
		QtHttpProxy qtProxy = new QtHttpProxy(hostName, port, userName, password);
		addAuthProxy(qtProxy);
	}
	
	/**
	 * 设置代理（可以多个）
	 * @param qtProxys 代理
	 */
	public void addAuthProxy(QtHttpProxy... qtProxys) {
		if(qtProxyList==null) {
			synchronized(QtHttpClient.class) {
				if(qtProxyList==null) {
					qtProxyList=new ArrayList<QtHttpProxy>();
				}
			}
		}
		qtProxyList.addAll(Arrays.asList(qtProxys));
		
		defaultCredentialsProvider=QtCredentialsProviderBuilder.getCredentialsProvider(qtProxyList);
		
	}
	/**
	 * 获取结果
	 * @param request request
	 * @param context context
	 * @param response response
	 * @throws ParseException ParseException
	 * @throws IOException IOException
	 * @return QtHttpResult
	 */
 
	 
	private QtHttpResult getHttpResult(QtHttpRequest request,HttpClientContext context, ClassicHttpResponse response) throws ParseException, IOException {
		// Once the request has been executed the local context can
		// be used to examine updated state and various objects affected
		// by the request execution.
		// // Last executed request
		// HttpRequest lastReq= context.getRequest();
		// // Execution route
		// context.getHttpRoute();
		// // Target auth state
		// context.getTargetAuthState();
		// // Proxy auth state
		// context.getTargetAuthState();
		// // Cookie origin
		// context.getCookieOrigin();
		// // Cookie spec used
		// context.getCookieSpec();
		// // User security token
		// context.getUserToken();
		QtHttpResult qhr = new QtHttpResult();
		HttpEntity entity =response.getEntity();
		qhr.setStatusCode(new StatusLine(response).getStatusCode());
		
		 
		qhr.setHeader(response.getHeaders());
		qhr.setCookieStore(context.getCookieStore());
		if(request.getResultType()==ResultType.Byte) {
			qhr.setResultByte(IOUtil.toByteArray(entity.getContent()));
		}else {
			qhr.setHtml(EntityUtils.toString(entity,request.charset));
		}
		if(response.getHeaders().length>0){
			List<Header> headers= Arrays.asList(response.getHeaders());
			Header locaHeader=headers.stream().filter(x->"Location".equalsIgnoreCase(x.getName())).findFirst().orElse(null);
			if(locaHeader!=null){
				qhr.redirectUrl=locaHeader.getValue();
			}
		}
		
		qhr.redirectLocations=context.getRedirectLocations();
		
		EntityUtils.consume(entity);
		return qhr;
	}

	/**
	 * 关闭httpclient客户端
	 * 
	 * @throws IOException IOException
	 */
	private void closeHttpClient() throws IOException {
		if (!isRun) {
			httpclient.close();
		}
	}

	
}
