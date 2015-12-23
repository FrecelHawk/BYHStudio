package com.xyt.ygcf.daying;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import android.text.TextUtils;

//import com.dyne.homeca.demo.FileUtils;
import com.raycommtech.ipcam.VideoInfo;

/**
 * 摄像头类
 * 
 * @author lvtaizhi
 * 
 */
public class CameraInfo implements Comparable<CameraInfo>, Serializable,
		Cloneable {

	public static enum CloudState {
		UNSURPPORT, INACTIVE, ACTIVE, UNUSE, ORDERED
	}

	public static enum CameraType {
		PERSONAL, PERSONALSHARE, AGENTSHARE, PUBLIC, DEMO
	}

	public boolean canControl() {
		return cameraType == CameraType.PERSONAL
				|| cameraType == CameraType.PERSONALSHARE;
	}

	public boolean canModify() {
		return cameraType == CameraType.PERSONAL;
	}

	private CameraType cameraType = CameraType.PERSONAL;
	/**
	 * 
	 */
	private static final long serialVersionUID = -4470864887433704824L;
	public static final int LINK_TYPE_UDNP = 1;
	public static final int LINK_TYPE_P2P = 2;
	public static final int LINK_TYPE_P2POTHER = 3;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getCamerain() {
		return camerain;
	}

	public void setCamerain(String camerain) {
		this.camerain = camerain;
	}

	public String getCamera_type() {
		return camera_type;
	}

	public void setCamera_type(String camera_type) {
		this.camera_type = camera_type;
	}

	public String getCamerasn() {
		String ss[] = camerasn.split(":|：");
		String realCamerasn = camerasn;
		if (ss.length > 1) {
			realCamerasn = ss[0];
		}
		return realCamerasn;
	}

	public void setCamerasn(String camerasn) {
		this.camerasn = camerasn;
	}

	public String getLinkedtype() {
		return linkedtype;
	}

	public void setLinkedtype(String linkedtype) {
		this.linkedtype = linkedtype;
	}

	public String getFullCamerasn() {
		return camerasn;
	}

	public String getMemo() {
		String ss[] = camerasn.split(":|：");
		String memo = "";
		if (ss.length > 1) {
			memo = ss[1];
		}
		return memo;
	}

	public int getNvrCnt() {
		String ss[] = camerain.split(":|：");
		int nvrCnt = -1;
		if (ss.length > 1) {
			nvrCnt = Integer.parseInt(ss[1]) - 1;
		}
		return nvrCnt;
	}

	public String getPureCamerain() {
		String ss[] = camerain.split(":|：");
		String realCamerain = camerain;
		if (ss.length > 1) {
			realCamerain = ss[0];
		}
		return realCamerain;
	}

	public String getRealCamerain() {
		String ss[] = camerain.split(":|：");
		String realCamerain = camerain;
		int nvrCnt = -1;
		if (ss.length > 1) {
			realCamerain = ss[0];
			nvrCnt = Integer.parseInt(ss[1]) - 1;
		}
		if (nvrCnt > -1) {
			realCamerain = new StringBuilder().append(realCamerain).append(":")
					.append(nvrCnt).toString();
		}
		return realCamerain;
	}

	// 1. 编码
	// 2. 通讯
	// 3. 存储
	// 4. wifi
	// 5. PPPoE
	// 6. 3G
	// 7.高清
	// 8.红外
	// 9. 防雨
	// 10. 智能
	// 11. 无线安防
	// 12. 移动侦测
	// 13. 遥控求助
	// 14. 智能家居
	// 15. onvif
	// 16. 云台
	public boolean isShared() {
		return "1".equals(linkedtype);
	}

	public String getSupportservice() {
		return supportservice;
	}

	public Boolean isHD() {
		return supportservice != null && supportservice.length() > 6
				&& supportservice.charAt(6) == '1';
	}

	public Boolean isAutoRecord() {
		return supportservice != null && supportservice.length() > 2
				&& supportservice.charAt(2) == '1';
	}

	public Boolean isZoom() {
		return supportservice != null && supportservice.length() > 16
				&& supportservice.charAt(16) == '1';
	}

	public Boolean isResolution() {
		return supportservice != null && supportservice.length() > 6
				&& supportservice.charAt(6) == '1';
	}

	public Boolean isH264() {
		String lowerCamerain = camerain.toLowerCase(Locale.CHINA);
		return (supportservice != null && supportservice.length() > 0 && supportservice
				.charAt(0) == '1') && !lowerCamerain.startsWith("parc");
	}

	public Boolean isMutilChannel() {
		return supportservice != null && supportservice.length() > 19
				&& supportservice.charAt(19) == '1';
	}

	public Boolean isCloudSupport() {
		return supportservice != null && supportservice.length() > 17
				&& supportservice.charAt(17) == '1';
	}

	public Boolean isOldDDns() {
		return supportservice != null && supportservice.length() > 18
				&& supportservice.charAt(18) == '1';
	}

	public void setSupportservice(String supportservice) {
		this.supportservice = supportservice;
	}

	public String getAccess_code() {
		return access_code;
	}

	public void setAccess_code(String access_code) {
		this.access_code = access_code;
	}

	public String getCameraserverurl() {
		return cameraserverurl;
	}

	public void setCameraserverurl(String cameraserverurl) {
		cameraserverurl = cameraserverurl.replace("http://", "");
		cameraserverurl = cameraserverurl.replace("/", "");
		cameraserverurl = cameraserverurl.replace("\\", "");
		this.cameraserverurl = cameraserverurl;
	}

	public static enum CameraState {
		STOP, NOTVALID, OFFLINE, ONLINE, REFRESH, SHARED, EMPTY
	};

	public boolean isOutOfService() {
		CameraState cameraState = isValid();
		return cameraState == CameraState.STOP
				|| cameraState == CameraState.NOTVALID;
	}

	int viStatus = 0;

	public void setviStatus(int status) {
		viStatus = status;
	}

	public int getviStatus() {
		return viStatus;
	}

	// viStatus -1 0 1
	public boolean isRealOffLine() {
		return viStatus == 0;
	}

	public CameraState isValid() {

		if (viStatus == -1)
			return CameraState.EMPTY;

		// isOnline 显示连接成功还是失败
		if (viStatus != 1)
			return CameraState.OFFLINE;

		return CameraState.ONLINE;
	}

	public String toString() {
		String s = "{设备服务器地址:" + cameraserverurl + ",设备域名:" + camerain
				+ ",设备类型:" + camera_type + ",设备名称:" + camerasn + ",连接类型:"
				+ linkedtype + ",设备supportservice:" + supportservice
				+ ",共享访问密码:" + access_code + ",设备访问名:" + equipadmin
				+ ",设备访问密码:" + equippass;
		if (videoInfo == null && videoInfo.getStatus() == -1) {
			s += ",设备状态:设备服务器访问失败}";
		} else if (videoInfo.getStatus() == 0) {
			s += ",设备状态：不在线}";
		} else if (videoInfo.getStatus() == 1) {
			s += ",设备状态:在线}";
		}
		s += "激活开始时间：" + activedate + "，结束时间:" + deactivedate + ",状态" + ":"
				+ status;
		return s;
	}

	public VideoInfo getVideoInfo() {
		return videoInfo;
	}

	public void setVideoInfo(VideoInfo videoInfo) {
		this.videoInfo = videoInfo;
	}

	private VideoInfo videoInfo;

	private String cameraserverurl;// 服务器地址
	private String camerain;// 设备域名
	private String camera_type;// 设备类型
	private String camerasn;// 设备名称
	private String linkedtype;// 连接类型0 注册，1 共享
	private String supportservice;
	private String access_code;// 共享访问密码
	private String saccessCode;// 观看访问密码

	public boolean isNeedSharePassword() {
		return !TextUtils.isEmpty(access_code);
	}

	public boolean isNeedPassword() {
		return !TextUtils.isEmpty(saccessCode);
	}

	public boolean isAccessPassed(String password) {
		return isPassed(saccessCode, password);
	}

	public boolean isSharedPassed(String password) {
		return isPassed(access_code, password);
	}

	private boolean isPassed(String saccessCode, String password) {
		if (TextUtils.isEmpty(saccessCode) && TextUtils.isEmpty(password))
			return true;
		return (saccessCode != null && saccessCode.trim().equals(password));
	}

	public String getSaccessCode() {
		return saccessCode;
	}

	public void setSaccessCode(String saccessCode) {
		this.saccessCode = saccessCode;
	}

	private Date activedate;
	private Date deactivedate;
	private String status;
	private boolean isOnline;
	private boolean isRefresh;
	private String codeRate;
	private CloudState cloudstate = CloudState.UNSURPPORT;
	private int CameraStrategy = 0;

	public void setCodeRate(String codeRate) {
		this.codeRate = codeRate;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public Date getActivedate() {
		return activedate;
	}

	public void setActivedate(Date activedate) {
		this.activedate = activedate;
	}

	public Date getDeactivedate() {
		return deactivedate;
	}

	public void setDeactivedate(Date deactivedate) {
		this.deactivedate = deactivedate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEquipadmin() {
		return equipadmin;
	}

	public void setEquipadmin(String equipadmin) {
		this.equipadmin = equipadmin;
	}

	public String getEquippass() {
		return equippass;
	}

	public void setEquippass(String equippass) {
		this.equippass = equippass;
	}

	private String equipadmin;
	private String equippass;

	@Override
	public int compareTo(CameraInfo another) {
		CameraInfo c = another;
		return camerain.compareTo(c.getCamerain());
	}

	private String linkInfo;

	public String getLinkInfo() {
		return linkInfo;
	}

	public void setLinkInfo(String linkInfo) {
		this.linkInfo = linkInfo;
	}

	public boolean isRefresh() {
		return isRefresh;
	}

	public void setRefresh(boolean isRefresh) {
		this.isRefresh = isRefresh;
	}

	public String getSnapFilePath() {
		if (snapFilePath == null) {
			snapFilePath = FileUtils.getSnapFilePath(getCamerain());
		}
		return snapFilePath;
	}

	public void setSnapFilePath(String snapFilePath) {
		this.snapFilePath = snapFilePath;
	}

	public CameraType getCameraType() {
		return cameraType;
	}

	public void setCameraType(CameraType cameraType) {
		this.cameraType = cameraType;
	}

	public CloudState getCloudstate() {
		return cloudstate;
	}

	public void setCloudstate(CloudState cloudstate) {
		this.cloudstate = cloudstate;
	}

	public int getCameraStrategy() {
		return CameraStrategy;
	}

	public void setCameraStrategy(int cameraStrategy) {
		CameraStrategy = cameraStrategy;
	}

	public String getCamerabelongto() {
		return camerabelongto;
	}

	public void setCamerabelongto(String camerabelongto) {
		this.camerabelongto = camerabelongto;
	}

	private String snapFilePath;

	private String camerabelongto;
}
