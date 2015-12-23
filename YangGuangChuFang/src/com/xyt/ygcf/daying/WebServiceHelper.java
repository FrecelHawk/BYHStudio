package com.xyt.ygcf.daying;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.text.TextUtils;
import android.util.Log;

public class WebServiceHelper {
	private static final String TAG = "WebServiceHelper";
	private static final int TIMEOUT = 60000;
	private static final int DNSTIMEOUT = 5000;

	public static String constructionGetURLParamsString(
			Map<String, Object> params) {
		if (params == null)
			return "";

		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, Object>> iter = params.entrySet().iterator();
		try {
			while (iter.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter
						.next();
				if (sb.length() > 0) {
					sb.append("&");
				}

				sb.append(entry.getKey())
						.append("=")
						.append(URLEncoder.encode(entry.getValue().toString(),
								"UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	// public static String httpPost(String address,
	// Map<String, Object> rqPropertiesMap) {
	// try {
	// URL url = new URL(address);
	//
	// if (TextUtils.isEmpty(url.getProtocol())) {
	// address = String.format("http://%s", address);
	// }
	// url = new URL(address);
	//
	// HttpURLConnection httpURLConnection = (HttpURLConnection) url
	// .openConnection();
	//
	// // �����Ƿ���connection�������Ϊ�����post���󣬲���Ҫ����
	// // http�����ڣ������Ҫ��Ϊtrue
	// httpURLConnection.setDoOutput(true);
	// // Read from the connection. Default is true.
	// httpURLConnection.setDoInput(true);
	//
	// httpURLConnection.setRequestMethod("POST");
	// // post������ʹ�û���
	// httpURLConnection.setUseCaches(false);
	// httpURLConnection.setInstanceFollowRedirects(true);
	// // ���ñ������ӵ�Content-type������Ϊapplication/x-www-form-urlencoded��
	// // ��˼��������urlencoded������form�����������ǿ��Կ������Ƕ���������ʹ��URLEncoder.encode
	// // ���б���
	// httpURLConnection.setRequestProperty("Content-Type",
	// "application/x-www-form-urlencoded");
	// DataOutputStream out = new DataOutputStream(
	// httpURLConnection.getOutputStream());
	// out.writeBytes(constructionGetURLParamsString(rqPropertiesMap));
	// out.flush();
	// out.close();
	//
	// InputStreamReader inputStreamReader = new InputStreamReader(
	// httpURLConnection.getInputStream(), "UTF-8");
	// BufferedReader bufferedReader = new BufferedReader(
	// inputStreamReader);
	//
	// StringBuilder sb = new StringBuilder();
	// String line;
	// while ((line = bufferedReader.readLine()) != null) {
	// sb.append(line);
	// }
	// inputStreamReader.close();
	// httpURLConnection.disconnect();
	// String result = sb.toString();
	// result = URLDecoder.decode(result, "utf-8");
	// Log.d(TAG, result);
	//
	// return result;
	// } catch (ProtocolException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (MalformedURLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return null;
	// }

	private static DefaultHttpClient getDefaultHttpClient(final String charset) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		httpclient.getParams().setParameter(
				CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
		httpclient.getParams().setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET,
				charset == null ? HTTP.UTF_8 : charset);

		httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
				20 * 1000); // socket��ʱ����
		httpclient.getParams().setIntParameter(
				HttpConnectionParams.CONNECTION_TIMEOUT, 15 * 1000);// ���ӳ�ʱ

		DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(
				2, true);// ��������2��

		httpclient.setHttpRequestRetryHandler(retryHandler);
		return httpclient;
	}

	private static DefaultHttpClient getDefaultHttpClient() {
		return getDefaultHttpClient(HTTP.UTF_8);
	}

	public static String httpGet(String address, List<Header> headers,
			Map<String, Object> rqPropertiesMap) {
		HttpResponse response = httpGetRes(address, headers, rqPropertiesMap);
		if (response != null) {
			String result = HttpResponseUtil.asString(response);
			Log.d(TAG, result);
			return result;
		}

		return null;
	}

	public static HttpResponse httpGetRes(String address, List<Header> headers,
			Map<String, Object> rqPropertiesMap) {
		try {
			URI uri = new URI(address);
			if (TextUtils.isEmpty(uri.getScheme())) {
				address = String.format("http://%s", address);
			}

			if (rqPropertiesMap != null) {
				if (address.indexOf("?") < 0) {
					address = String.format("%s?%s", address,
							constructionGetURLParamsString(rqPropertiesMap));
				} else {
					address = String.format("%s&%s", address,
							constructionGetURLParamsString(rqPropertiesMap));
				}
			}

			Log.d(TAG, address);

			HttpGet httpget = new HttpGet(address);
			// httpget.setHeader("Connection", "Close");
			if (headers != null) {
				httpget.setHeaders(headers.toArray(new Header[0]));
			}

			DefaultHttpClient httpclient = getDefaultHttpClient();

			HttpResponse response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return response;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String httpGet(String address,
			Map<String, Object> rqPropertiesMap) {
		return httpGet(address, null, rqPropertiesMap);
	}

	public static String httpPost(String address, List<Header> headers,
			List<NameValuePair> nvps) {

		HttpResponse response = httpPostRes(address, headers, nvps);
		if (response != null) {
			String result = HttpResponseUtil.asString(response);
			Log.d(TAG, result);
			return result;
		}

		return null;
	}

	public static HttpResponse httpPostRes(String address,
			List<Header> headers, List<NameValuePair> nvps) {
		try {
			URL url = new URL(address);

			if (TextUtils.isEmpty(url.getProtocol())) {
				address = String.format("http://%s", address);
			}

			Log.d(TAG, address);

			HttpPost httppost = new HttpPost(address);

			httppost.setHeader("Connection", "Close");
			if (headers != null) {
				httppost.setHeaders(headers.toArray(new Header[0]));
			}
			if (nvps != null) {
				httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			}

			DefaultHttpClient httpclient = getDefaultHttpClient();

			HttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return response;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String postFile(String address, List<Header> headers,
			String filePath) {
		try {
			URL url = new URL(address);

			if (TextUtils.isEmpty(url.getProtocol())) {
				address = String.format("http://%s", address);
			}

			Log.d(TAG, address);

			HttpPost httppost = new HttpPost(address);

			httppost.setHeader("Connection", "Close");
			if (headers != null) {
				httppost.setHeaders(headers.toArray(new Header[0]));
			}

			File file = new File(filePath);
			MultipartEntity mpEntity = new MultipartEntity();
			ContentBody cbFile = new FileBody(file);
			mpEntity.addPart("myUpload", cbFile);

			httppost.setEntity(mpEntity);

			DefaultHttpClient httpclient = getDefaultHttpClient();

			HttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = HttpResponseUtil.asString(response);
				Log.d(TAG, result);
				return result;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String putFile(String address, List<Header> headers,
			String filePath) {
		try {
			URL url = new URL(address);

			if (TextUtils.isEmpty(url.getProtocol())) {
				address = String.format("http://%s", address);
			}

			Log.d(TAG, address);

			HttpPut httpput = new HttpPut(address);

			httpput.setHeader("Connection", "Close");
			if (headers != null) {
				httpput.setHeaders(headers.toArray(new Header[0]));
			}

			File localFile = new File(filePath);

			Long length = localFile.length();

			FileInputStream inputStream = new FileInputStream(localFile);
			InputStreamEntity inputStreamEntity = new InputStreamEntity(
					inputStream, length);
			httpput.setEntity(inputStreamEntity);

			DefaultHttpClient httpclient = getDefaultHttpClient();

			HttpResponse response = httpclient.execute(httpput);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = HttpResponseUtil.asString(response);
				Log.d(TAG, result);
				return result;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String callWebService(String url, String nameSpace,
			String wsName, Map<String, Object> rqPropertiesMap) {
		Object o = callBaseWebService(url, nameSpace, wsName, rqPropertiesMap);
		if (o == null)
			return null;
		else {
			String result;
			result = o.toString();
			try {
				result = URLDecoder.decode(result, "UTF-8");
				Log.d(TAG, url);
				Log.d(TAG, result);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return result;
		}
	}

	public static Object callBaseWebService(String url, String nameSpace,
			String wsName, String actionName,
			Map<String, Object> rqPropertiesMap) {
		try {
			URI lurl = new URI(url);

			if (!testNet(lurl.getHost(), DNSTIMEOUT)) {
				return null;
			}

			SoapObject request = new SoapObject(nameSpace, wsName);
			Iterator<Entry<String, Object>> iter = rqPropertiesMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter
						.next();
				request.addProperty(entry.getKey(), entry.getValue());
			}
			Log.d(TAG, request.toString());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);

			envelope.dotNet = true;
			envelope.bodyOut = request;

			HttpTransportSE hts = new HttpTransportSE(url, TIMEOUT);
			// AndroidHttpTransport hts = new AndroidHttpTransport(url);
			hts.debug = true;
			// web service����
			hts.call(actionName, envelope);
			// �õ����ؽ��
			return envelope.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Object callBaseWebService(String url, String nameSpace,
			String wsName, Map<String, Object> rqPropertiesMap) {
		return callBaseWebService(url, nameSpace, wsName, nameSpace + wsName,
				rqPropertiesMap);
	}

	public static Boolean testNet(String url, int timeout) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Boolean result = false;
		Future<Boolean> future = executor.submit(new TestNet(url));

		try {
			result = future.get(timeout, TimeUnit.MILLISECONDS);// �趨��200�����ʱ�������
		} catch (InterruptedException e) {
			future.cancel(true);// �߳��жϳ���
		} catch (ExecutionException e) {
			future.cancel(true);// �̷߳������
		} catch (TimeoutException e) {// ��ʱ�쳣
			future.cancel(true);// ��ʱ
		} catch (Exception e) {
			future.cancel(true);
		} finally {
			executor.shutdown();
		}

		return result;
	}

	private static class TestNet implements Callable<Boolean> {
		private String mUrl;

		public TestNet(String url) {
			mUrl = url;
		}

		@Override
		public Boolean call() throws Exception {
			InetAddress.getByName(mUrl);
			return true;
		}
	}

}
