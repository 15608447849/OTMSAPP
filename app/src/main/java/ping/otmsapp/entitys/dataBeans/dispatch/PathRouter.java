package ping.otmsapp.entitys.dataBeans.dispatch;

import com.amap.api.services.core.LatLonPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ping.otmsapp.entitys.maps.location.GdMapUtils;

/**
 * Created by Leeping on 2018/5/25.
 * email: 793065165@qq.com
 */

public class PathRouter {
    private static class LocBean{
        public String tag = "";
        public LatLonPoint latLon;
        public LocBean(String addStr, LatLonPoint latLonPoint) {
            this.tag = addStr;
            this.latLon = latLonPoint;
        }
    }
    private static class LocBeanDisTemp{
        public LocBean start;
        public LocBean end;
        public long distance;
        public long duration;
        public LocBeanDisTemp(LocBean start, LocBean end, long distance,long duration) {
            this.start = start;
            this.end = end;
            this.distance = distance;
            this.duration = duration;
        }
    }
    private static class LocBeanNode{
        public LocBean loc;//本地坐标信息
        public LocBeanNode next;//我的下一个
        public long toNextDistance;//到下一个的距离
        public long toNextDuration;//到下一个的时间
        public LocBeanNode(LocBean loc) {
            this.loc = loc;
        }
        public void setNext(LocBeanNode next,long distance,long duration){
            this.next = next;
            this.toNextDistance = distance;
            this.toNextDuration = duration;
        }
    }
    private DispatchBean dispatchBean;
    public PathRouter(DispatchBean dispatchBean){this.dispatchBean = dispatchBean;}
    public String execute() {

        LocBean warehouse  = new LocBean("总仓",GdMapUtils.get().addressConvertLatLon(dispatchBean.getAddr()));
        List<LocBean> list =  initData();
        List<LatLonPoint> latLonPoints = new ArrayList<>();
        for (LocBean locBean : list){
            latLonPoints.add(locBean.latLon);
        }
        LocBeanNode locBeanNode = new LocBeanNode(warehouse);
        calculate(locBeanNode,list);
        closeCycle(locBeanNode,locBeanNode);
        setPathValue(locBeanNode,locBeanNode,0);
        return print(locBeanNode);
    }
    private List<LocBean> initData() {
        List<LocBean> list = new ArrayList<>();
        for (DistributionPathBean distributionPathBean : dispatchBean.getDistributionPathBean()){
            list.add(new LocBean(distributionPathBean.getStoreName(), GdMapUtils.get().addressConvertLatLon(distributionPathBean.getDetailedAddress())));
        }
        return list;
    }
    private void calculate(LocBeanNode startNode, List<LocBean> list) {
            if (list.size() == 0) return;
            //先取起始点到各点距离
            LocBean start = startNode.loc;
            long[] arr;
            LocBeanDisTemp locBeanDisTemp;
            List<LocBeanDisTemp> locBeanDisTempList = new ArrayList<>();
            for (LocBean locBean : list){
                arr = GdMapUtils.get()
                        .pointConvertTimeAndDistance3(start.latLon,locBean.latLon);
//                Log.w("计算距离",start.addStr+" -> "+ locBean.addStr+" , "+arr[1]);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
                locBeanDisTemp = new LocBeanDisTemp(start,locBean,arr[1],arr[0]);
                locBeanDisTempList.add(locBeanDisTemp);
            }
            if (locBeanDisTempList.size()>1){
                //排序 取出最小值
                Collections.sort(locBeanDisTempList,new Comparator<LocBeanDisTemp>(){
                    @Override
                    public int compare(LocBeanDisTemp arg0, LocBeanDisTemp arg1) {
                        return Long.compare(arg0.distance,arg1.distance);
                    }
                });
            }
        LocBeanNode next = new LocBeanNode(locBeanDisTempList.get(0).end);
        startNode.setNext(next,locBeanDisTempList.get(0).distance,locBeanDisTempList.get(0).duration);
        list.remove(locBeanDisTempList.get(0).end);
        calculate(next,list);
    }
    private void closeCycle(LocBeanNode origin, LocBeanNode locBeanNode) {
        if (locBeanNode.next!=null){
            closeCycle(origin,locBeanNode.next);
        }else{
            long[] arr = GdMapUtils.get()
                    .pointConvertTimeAndDistance3(locBeanNode.loc.latLon,origin.loc.latLon);
//            Log.w("计算距离",locBeanNode.loc.addStr+" -> "+ origin.loc.addStr+" , "+arr[1]);
            locBeanNode.setNext(origin,arr[1],arr[0]);
        }
    }
    private String print(LocBeanNode locBeanNode) {
        StringBuffer stringBuffer = new StringBuffer("线路:\n");
        long[] totalArr = new long[]{0,0};
        printResult(locBeanNode,locBeanNode,stringBuffer,totalArr);
        stringBuffer.append("\n线路总距离:"+ totalArr[0]+" 米,线路总耗时:"+totalArr[1]+" 秒");
        return stringBuffer.toString();
    }
    private void printResult(LocBeanNode startNode,LocBeanNode node,StringBuffer stringBuffer,long[] total) {
        stringBuffer.append(node.loc.tag +" ==> "+node.next.loc.tag +"\t"+node.toNextDistance+" 米, "+node.toNextDuration+" 秒\n");
        total[0]+=node.toNextDistance;
        total[1]+=node.toNextDuration;
        if (node.next.equals(startNode)) return;
        printResult(startNode,node.next,stringBuffer,total);

    }
    private void setPathValue(LocBeanNode startNode,LocBeanNode node,int index) {
        if (node.next.equals(startNode)) return;
        for (DistributionPathBean distributionPathBean : dispatchBean.getDistributionPathBean()){
            if (node.next.loc.tag.equals(distributionPathBean.getStoreName())){
                distributionPathBean.setFactMileage( ((int)(node.toNextDistance+0.5))/1000); //四舍五入
                distributionPathBean.setSpecifiedOrder(++index);
                break;
            }
        }
        setPathValue(startNode,node.next,index);
    }
}
