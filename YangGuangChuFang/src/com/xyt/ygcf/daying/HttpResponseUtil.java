package com.xyt.ygcf.daying;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpResponseUtil {

	public static String asString(HttpResponse response) {
		if (response == null) {
			return null;
		}

		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String charset = EntityUtils.getContentCharSet(entity);
				if (charset == null) {
					charset = HTTP.UTF_8;
				}
				return new String(EntityUtils.toByteArray(entity), charset);
			}
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static void asFile(HttpResponse response, String filePath) {
		if (response == null) {
			return;
		}

		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				FileOutputStream output = new FileOutputStream(new File(
						filePath));
				InputStream input = entity.getContent();
				byte[] buffer = new byte[1024];
				int readLen = 0;
				while ((readLen = input.read(buffer)) != -1) {
					output.write(buffer, 0, readLen);
				}
				output.flush();
				output.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
