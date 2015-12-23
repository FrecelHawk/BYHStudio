package com.xyt.ygcf.daying;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

//import com.dyne.homeca.bean.CameraInfo;
import com.raycommtech.ipcam.VideoInfo;

public class CameraFun {
	public static int getStatus(CameraInfo camInfo) {
		String url = camInfo.getCameraserverurl();
		url = (url == null || url.length() < 1) ? "http://58.213.149.181/ddns/Device!toGetDevice.action"
				: url + "/ddns/Device!toGetDevice.action";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bean.ddns", camInfo.getPureCamerain());
		params.put("bean.userId", "2");
		params.put("oper", "1");

		String result = WebServiceHelper.httpGet(url, params);
		if (result == null || result.length() == 0)
			return 0;

		int status = 0;
		try {
			JSONObject json = new JSONObject(result);
			if (json.getInt("result") == 1) {
				json = json.getJSONObject("device");
				status = json.optInt("status");
				if (status < 0)
					status = 0;

				if (camInfo.isOldDDns())
					camInfo.setviStatus(-1);
				else
					camInfo.setviStatus(status);

				VideoInfo vi = camInfo.getVideoInfo();
				if (vi == null) {
					vi = new VideoInfo();
					vi.setTitle(camInfo.getCamerasn());
					vi.setDdnsname(camInfo.getPureCamerain());
					vi.setDdnsServer(camInfo.getCameraserverurl());
					camInfo.setVideoInfo(vi);
				}

				vi.setLinkTypeId(json.optInt("linkTypeId"));
				vi.setFactoryTypeId(json.optInt("factoryTypeId"));
				vi.setDeviceTypeId(json.optInt("deviceTypeId"));
				vi.setUsername(json.optString("username"));
				vi.setPassword(json.optString("password"));
				vi.setWebPort(json.optInt("webPort"));
				vi.setStatus(json.optInt("status"));
				vi.setIp(json.optString("ip"));
				vi.setPort(json.optInt("port"));
				vi.setRtmpPort(json.optInt("rtmpPort"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return status;
	}
}
