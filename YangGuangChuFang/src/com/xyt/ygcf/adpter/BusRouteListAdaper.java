package com.xyt.ygcf.adpter;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.Doorway;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteBusWalkItem;
import com.xyt.yangguangchufang.R;
import com.xyt.ygcf.util.TimeUtils;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class BusRouteListAdaper extends BaseAdapter{
	private Context context;
	private List<BusPath> busPaths;
	Honlder honlder;
	public BusRouteListAdaper(Context context,List<BusPath> busPaths){
		this.context =context;
		this.busPaths = busPaths;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return busPaths ==null?0:busPaths.size();
	}

	@Override
	public Object getItem(int position) {
		return busPaths.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	

	@Override
	public View getView(final int position, View contView, ViewGroup paramViewGroup) {
		
		if(null ==contView){
			contView = LinearLayout.inflate(context,R.layout.bus_route_item, null);
			honlder = new Honlder();
//			honlder.tv_dot = (TextView) contView.findViewById(R.id.bus_route_item_dot);
			honlder.tv_name = (TextView) contView.findViewById(R.id.bus_route_item_busMode);
			honlder.tv_detail = (TextView) contView.findViewById(R.id.bus_route_item_detail);
			contView.setTag(honlder);
		}else{
			honlder = (Honlder) contView.getTag();
		}
		
		BusPath busPath = busPaths.get(position);
		List<BusStep> busSteps = busPath.getSteps();
		StringBuffer sb = new StringBuffer();
		StringBuffer instruction = new StringBuffer();
		instruction.append("从我的位置出发").append(";");
		String walkDistance = TimeUtils.meterTransformKilometer((int)busPath.getWalkDistance());  //步行总距离
		int time =0;
		int stationNum =0;
		for(BusStep busStep :busSteps){
			RouteBusLineItem busItem	=busStep.getBusLine();
			 Doorway exit = busStep.getExit();
			RouteBusWalkItem busWalkItem = busStep.getWalk();
			if(busWalkItem != null){
				time += (int) busWalkItem.getDuration();
				float distance =busWalkItem.getDistance();
				instruction.append("步行").append(distance).append("米")
				.append(";");
			}
			
			if(busItem != null){
               BusStationItem departureBusStation = busItem.getDepartureBusStation(); 
				String startStion = departureBusStation.getBusStationName(); 
				 instruction.append("到达").append(startStion).append("站").append(";");
				 String name =busItem.getBusLineName();
				 String nameStr = name;
				 name = name.substring(0, name.indexOf("("));
				 nameStr = nameStr.substring(0,nameStr.indexOf("(")+1)+nameStr.substring(nameStr.lastIndexOf("-")+1);
				 instruction.append("乘坐").append(nameStr).append(";");
				 time +=  busItem.getDuration();
				 stationNum +=busItem.getPassStationNum()+1;
				 instruction.append("经过").append(busItem.getPassStationNum()+1).append("站  ");
				 
				 BusStationItem arrivalBusStation = busItem.getArrivalBusStation();
				 String arrivalStation = arrivalBusStation.getBusStationName();
				 if(exit != null){
					    instruction.append(arrivalStation);
						String exitStr = exit.getName();
						instruction.append("(").append(exitStr).append("出);");
					}else{
						instruction.append(arrivalStation).append("站;");
					}
				 
				 String to =context.getResources().getString(R.string.jiantou);
				 sb.append(name).append(to);
			}
			
			
			
		}
		
		String routeLine= sb.substring(0, sb.length()-1);
		instruction.append("到达目的地");
//		honlder.tv_dot.setText(""+(position+1));
		honlder.tv_name.setText(routeLine);
		honlder.tv_name.setTag(instruction.toString());
		String costTime = TimeUtils.cal(time);
		honlder.tv_detail.setText(costTime+" 乘车"+stationNum+"站  步行"+walkDistance);
//		
		return contView;
	}

	
	private class Honlder{
		TextView tv_dot, tv_name,tv_detail;
	}
}
