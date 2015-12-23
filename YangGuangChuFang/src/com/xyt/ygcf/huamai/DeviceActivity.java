package com.xyt.ygcf.huamai;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.huamaitel.api.HMDefines.NodeTypeInfo;
import com.huamaitel.api.HMDefines.UserInfo;
import com.huamaitel.api.HMDefines;
import com.huamaitel.api.HMJniInterface;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.adpter.MerchantMenuTypeAdapter.ViewHolder;
import com.xyt.ygcf.base.AppContext;
import com.xyt.ygcf.base.BaseActivity;
import com.xyt.ygcf.daying.VideoActivity2;
import com.xyt.ygcf.entity.BaseListBeen;
import com.xyt.ygcf.entity.NearbyListItemBean;
import com.xyt.ygcf.entity.VideoItem;
import com.xyt.ygcf.entity.VideoSupplier;
import com.xyt.ygcf.home.MerchantDatails;
import com.xyt.ygcf.urls.UrlMy;

public class DeviceActivity extends BaseActivity {
	private static final String TAG = "DeviceActivity";

	private static final int VIDEO = 0;
	private static final int PLAY = 1;
	private static final int EVENT_LOGIN_SUCCESS = 3;
	private static final int EVENT_LOGIN_FAIL = 4;
	
//	private static final String SERVER_ADDR = "www.huamaiyun.com";
//	private static final short SERVER_PORT = 80;
//	private String ADDR;
//	private short PORT;
//	private String USER;
//	private String PASSWARD;
	private ProgressDialog loginProcessDialog;
	// private Handler handler;
	private Thread mServerThread = null;
	
	private ListView mListView;
	private TextView emptyView;
//	private List<Map<String, Object>> mListData;
	private List<VideoItem> videoItems = new ArrayList<VideoItem>();
//	private List<VideoSupplier> supplierItems = new ArrayList<VideoSupplier>();
	
	private VideoAdapter adapter;
	private VideoSupplier supplierItem;

	private String id;  //商家ID
	private Context context = this;
	private int nodeId ;  
	private int chanleId;   //通道号
//	private int mVideoStream = HMDefines.CodeStream.MAIN_STREAM;
	private String sN; //设备ID
	private String ip; //设备服务地址
	private Handler handle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case VIDEO:
				
				videoItems.addAll((List<VideoItem>)msg.obj);
				adapter.notifyDataSetChanged();
				break;
			case PLAY:
//				LoginDevice();
				break;
			case EVENT_LOGIN_SUCCESS:
				int treeId = AppContext.treeId;
				int rootId = AppContext.getJni().getRoot(treeId);
				HMJniInterface sdk = AppContext.getJni();
				nodeId = sdk.findDeviceBySn(treeId, sN);
				closeWaitDoalog();
//				Intent intent = new Intent();
//				intent.setClass(DeviceActivity.this, PlayActivity.class);
//				intent.putExtra("nodeId", nodeId);
//				AppContext.mIsUserLogin=true;
//				startActivity(intent);
				gotoPlayActivity(nodeId,chanleId);
				break;
			case EVENT_LOGIN_FAIL:
				closeWaitDoalog();
				AppContext.getJni().disconnectServer(AppContext.serverId);
				Toast.makeText(DeviceActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
				break;
			}
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_activity);
		setTitle("监控区");
		// 华迈
		AppContext.getJni().init();
		getIntentData();
		
		mListView = (ListView) findViewById(R.id.id_device_list);
		emptyView = (TextView) findViewById(R.id.empty);
		mListView.setEmptyView(emptyView);
//		mListData = new ArrayList<Map<String, Object>>();
//
		adapter = new VideoAdapter();
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
//				supplierRequest(videoItems.get(position).getSupplierId());
				sN = videoItems.get(position).getSn();
				ip = videoItems.get(position).getIp();
				chanleId = Integer.parseInt(videoItems.get(position).getChannelNo());
				if(ip.equals("www.huamaiyun.com")||ip.equals("yun.huamaiyun.com")){ //华迈
					LoginDevice(videoItems.get(position).getIp(),
							(short) Integer.parseInt(videoItems.get(position).getPort()),
							videoItems.get(position).getUser(),
							videoItems.get(position).getPassward());
				}
				else{//达盈
					Intent intent = new Intent();
					intent.setClass(DeviceActivity.this, VideoActivity2.class);
					intent.putExtra("ip", ip);
					intent.putExtra("sN", sN);
					startActivity(intent);
				}
//				handle.sendEmptyMessage(EVENT_LOGIN_SUCCESS);
			}
		});
				
//		// Get the root of the tree.
//
//		AppContext.rootList.clear();
//		AppContext.rootList.add(rootId);
//
//		getChildrenByNodeId(rootId);
//		SimpleAdapter adapter = new SimpleAdapter(this, 
//				mListData, R.layout.item_device,
//				new String[] { "img", "name" }, 
//				new int[] { R.id.id_img_deviceIcon, R.id.id_device_name });
//		mListView.setAdapter(adapter);
//		mListView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//				@SuppressWarnings("unchecked")
//				Map<String, Object> map = (Map<String, Object>) arg0.getItemAtPosition(position);
//				int nodeType = (Integer) map.get("type");
//				Log.i("DeviceActivity:", "nodeType:" + nodeType);
//				int nodeId = (Integer) map.get("id");
//				Log.i("DeviceActivity:", "nodeId:" + nodeId);
//
//				AppContext.curNodeHandle = nodeId;
//				if (nodeType == NodeTypeInfo.NODE_TYPE_DVS || nodeType == NodeTypeInfo.NODE_TYPE_GROUP) {
//					AppContext.rootList.add(nodeId);
//					getChildrenByNodeId(nodeId);
//
//					((SimpleAdapter) mListView.getAdapter()).notifyDataSetChanged();
//				} else if (nodeType == NodeTypeInfo.NODE_TYPE_DEVICE) {
//					Intent intent = new Intent();
//					intent.setClass(DeviceActivity.this, PlayActivity.class);
//					intent.putExtra("nodeId", nodeId);
//					AppContext.mIsUserLogin=true; //komorilee
//					startActivity(intent);
//				}
//			}
//		});

	}
	
	private void showWaitDialog(String str) {
		loginProcessDialog = ProgressDialog.show(this, null, str);
		loginProcessDialog.setCancelable(true);
	}
	
	private void closeWaitDoalog() {
		if (loginProcessDialog != null) {
			if (loginProcessDialog.isShowing()) {
				loginProcessDialog.dismiss();
				loginProcessDialog = null;
			}
		}
	}
	
	protected void LoginDevice(final String ip, final short port, final String user, final String password) {
		showWaitDialog("logining...");
		mServerThread = new Thread() {
			@Override
			public void run() {
				HMJniInterface jni = AppContext.getJni();
				int result = 0;

				// 平台相关
				HMDefines.LoginServerInfo info = new HMDefines.LoginServerInfo();
				info.ip = ip; // 平台地址
				info.port = port; // 平台端口
				info.user = user; // 用户名
				info.password = password; // 密码
				info.model = android.os.Build.MODEL; // 手机型号
				info.version = android.os.Build.VERSION.RELEASE; // 手机系统版本号

				// 添加登录信息返回的错误码。
				// step 1: Connect the server.
//				int serverId = jni.connectServer(info, AppContext.mLoginServerError);
				int serverId = jni.connectServer(info);
				if (serverId != 0) {
					AppContext.serverId = serverId;
					result = jni.getDeviceList(serverId);
					if (result != HMDefines.HMEC_OK) {
						jni.disconnectServer(serverId);
						handle.sendEmptyMessage(EVENT_LOGIN_FAIL);
						return;
					}

					// step 2: Get user information.
					UserInfo userInfo = jni.getUserInfo(serverId);
					if (userInfo == null) {
						jni.disconnectServer(serverId);
						handle.sendEmptyMessage(EVENT_LOGIN_FAIL);
						return;
					}

					/**
					 * TODO:判断选择：
					 * huamaiyun和see1000中需要添加userInfo.useTransferService !=8
					 *
					 * 这个判断 seebao中需要去掉，否则容易报错！
					 */
					// step 3: Get transfer service.
					if (userInfo.useTransferService != 0) {
						result = jni.getTransferInfo(serverId);
						if (result != HMDefines.HMEC_OK) {
							jni.disconnectServer(serverId);
							handle.sendEmptyMessage(EVENT_LOGIN_FAIL);
							return;
						}
					}

					// step 4: Get tree id.
					AppContext.treeId = jni.getTree(serverId);
					handle.sendEmptyMessage(EVENT_LOGIN_SUCCESS);
				} else {
					handle.sendEmptyMessage(EVENT_LOGIN_FAIL);
				}
			}
		};
		mServerThread.start();
	}

	public void getIntentData() {
		id = getIntent().getStringExtra("id");
		videoRequest(id);
	}
	
	private void videoRequest(String storeId) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("merchantId", storeId);
		sendRequest(HttpMethod.GET, UrlMy.VIDEO_DETAILS, params, VIDEO, true);
	}
	
	private void supplierRequest(String supId){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("id", supId);
		sendRequest(HttpMethod.GET, UrlMy.VIDEO_SUPPLIER, params, PLAY, true);
	}
	
	
	/**
	 * 发送信息
	 * 
	 * @param what
	 * @param obj
	 */
	private void sendMessage(int what, Object obj) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		handle.sendMessage(msg);

	}
	
	@Override
	public void handleJson(String json, int which) {
		
		try {
//			if(which == VIDEO){
				parserVideo(json, handle, VIDEO);
//			}else if(which == PLAY){
//				parserPlay(json, handle, PLAY);
//			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	};

	private void parserPlay(String json, Handler handler2, int play2) throws JSONException{
		Log.e("supInfo",json);
				JSONObject jsonObject = new JSONObject(json);
				supplierItem = new VideoSupplier();
				supplierItem.id = jsonObject.optString("id"); 
				supplierItem.name =jsonObject.optString("name");
				supplierItem.ip = jsonObject.optString("ip");
				supplierItem.port = jsonObject.optString("port");
				supplierItem.user = jsonObject.optString("user");
				supplierItem.passward = jsonObject.optString("passward");
			handle.sendEmptyMessage(PLAY);
		
	}

	private void parserVideo(String json, Handler handler2, int video2) throws JSONException{
		Log.e("video", json);
		JSONArray jsonArray = new JSONArray(json);
		if(jsonArray != null){
			List<VideoItem> videoList = new ArrayList<VideoItem>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				VideoItem videoItem = new VideoItem();
				videoItem.id = jsonObject.optString("id");
				videoItem.name =jsonObject.optString("name");
				videoItem.ip = jsonObject.optString("ip");
				videoItem.port = jsonObject.optString("port");
				videoItem.user = jsonObject.optString("user");
				videoItem.passward = jsonObject.optString("pwd");
				videoItem.sn = jsonObject.optString("sn");
				videoItem.devName = jsonObject.optString("devName");
				videoItem.status = jsonObject.optString("status");
				videoItem.supplierId = jsonObject.optString("supplierId");
				videoItem.isDigit = jsonObject.optString("isDigit");
				if(videoItem.isDigit.equals("0")){
					videoItem.channelNo = "0";
				}else{
					videoItem.channelNo = jsonObject.optString("channelNo");
				}
				videoList.add(videoItem);
			}
			sendMessage(VIDEO, videoList);
		}
		
	}
	
	private void gotoPlayActivity(int nodeId, int channel) {
		Intent intent = new Intent();
		intent.setClass(DeviceActivity.this, PlayActivity.class);
		intent.putExtra("nodeId", nodeId);
		intent.putExtra("channel", channel);
//		intent.putExtra("video_stream", videoStream);
		
		AppContext.mIsUserLogin = true;
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (AppContext.treeId != 0) {
			AppContext.getJni().releaseTree(AppContext.treeId);
		}
		
		if (AppContext.serverId != 0) {
			AppContext.getJni().disconnectServer(AppContext.serverId);
		}
		
		AppContext.getJni().uninit();
		finish();
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (AppContext.rootList.size() != 1) {
//				int nodeId = AppContext.rootList.get(AppContext.rootList.size() - 2);
//				AppContext.rootList.remove(AppContext.rootList.size() - 1);
//
//				getChildrenByNodeId(nodeId);
//
//				((SimpleAdapter) mListView.getAdapter()).notifyDataSetChanged();
//				return true;
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	// Get the children list by this parent node.
//	private void getChildrenByNodeId(int nodeId) {
//		Log.d(TAG, "getDeviceListByNodeId nodeId: " + nodeId);
//		if (nodeId != 0) {
//			HMJniInterface sdk = AppContext.getJni();
//			mListData.clear();
//
//			int count = sdk.getChildrenCount(nodeId);
//			Log.d(TAG, "getChildrenCount: " + count);
//			for (int i = 0; i < count; ++i) {
//				Map<String, Object> obj = new HashMap<String, Object>();
//				int childrenNode = sdk.getChildAt(nodeId, i);
//				int nodeType = sdk.getNodeType(childrenNode);
//
//				obj.put("type", nodeType);
//
//				if (nodeType == NodeTypeInfo.NODE_TYPE_GROUP) { 
//					obj.put("img", R.drawable.folder);
//				} else if (nodeType == NodeTypeInfo.NODE_TYPE_DEVICE) {
//					obj.put("img", R.drawable.device);
//				} else if (nodeType == NodeTypeInfo.NODE_TYPE_DVS) {
//					obj.put("img", R.drawable.dvs);
//				} else if (nodeType == NodeTypeInfo.NODE_TYPE_CHANNEL) {
//					obj.put("img", R.drawable.device);
//				}
//
//				Log.d(TAG, " childNode: " + childrenNode);
//				Log.d(TAG, "childNode Url: " + sdk.getNodeUrl(childrenNode));
//				Log.d(TAG, "childNode sn: " + sdk.getDeviceSn(childrenNode));
//
//				obj.put("id", childrenNode);
//				obj.put("name", sdk.getNodeName(childrenNode));
//
//				mListData.add(obj);
//			}
//		}
//	}
	
	private class VideoAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return videoItems.size();
		}

		@Override
		public Object getItem(int arg0) {
			return videoItems.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder holder;
			if(arg1 == null){
				holder = new ViewHolder();
				arg1 = LayoutInflater.from(context).inflate(R.layout.video_list_item, null);
				holder.nameText = (TextView) arg1.findViewById(R.id.menu_type_name);
				arg1.setTag(holder);
			}else {
				holder = (ViewHolder) arg1.getTag();
			}
			VideoItem videoItem = videoItems.get(arg0);
			holder.nameText.setText(videoItem.devName);
			return arg1;
		}
		
		public class ViewHolder{
			TextView nameText;
		}
	}
}
