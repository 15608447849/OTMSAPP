package ping.otmsapp.assemblys.fragments.imps.secondPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.List;

import ping.otmsapp.R;
import ping.otmsapp.assemblys.fragments.base.BaseFragment;
import ping.otmsapp.entitys.apps.MediaBean;
import ping.otmsapp.entitys.dataBeans.dispatch.DispatchBean;
import ping.otmsapp.entitys.dataBeans.dispatch.RecycleBoxBean;
import ping.otmsapp.entitys.dataBeans.dispatch.RecycleBoxListBean;
import ping.otmsapp.entitys.dataBeans.dispatch.VehicleInfoBean;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.viewAdpters.second.ScanRecycleBoxRecycleAdapter;
import ping.otmsapp.viewHolders.fragments.second.ScanRecycleBoxViewHolder;

/**
 * Created by Leeping on 2018/4/10.
 * email: 793065165@qq.com
 */

public class ScanRecycleBox  extends BaseFragment implements RadioGroup.OnCheckedChangeListener,View.OnClickListener {
    private ScanRecycleBoxViewHolder viewHolder;
    private ScanRecycleBoxRecycleAdapter adapter;
    private int curType = -1;
    private VehicleInfoBean vehicleInfoBean;
    private MediaBean mediaBean;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (viewHolder==null){
            init();
            viewHolder = new ScanRecycleBoxViewHolder(mContext);
            adapter = new ScanRecycleBoxRecycleAdapter(mContext);
            viewHolder.list.setRecyclerDefaultSetting(adapter,1);
            viewHolder.radioGroup.setOnCheckedChangeListener(this);
            mediaBean = new MediaBean(mContext);
            viewHolder.setOnClickListener(this);
            listDataRefresh();
        }
        return viewHolder.getLayoutFileRid();
    }
    private void init() {
        try{
            vehicleInfoBean  = new DispatchBean().fetch().getVehicleInfoBean();
        }catch (Exception e){
            finishSelf();
        }
    }
    //初始化数据
    private void listDataRefresh() {
        pool.post(new Runnable() {
            @Override
            public void run() {
                final RecycleBoxListBean recycleBoxListBean = new RecycleBoxListBean().fetch();
                if (recycleBoxListBean!=null && recycleBoxListBean.getList().size()>0){
                    adapter. addDataList(recycleBoxListBean.getList(),true);

                    toUi(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            viewHolder.title.setText(AppUtil.stringForart("回收列表\t\t(%s)",recycleBoxListBean.getList().size()));
                        }
                    });

                }
            }
        });
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        curType = checkedId;
    }
    //退出
    @Override
    public boolean onExit() {
        return true;
    }

    //扫码
    @Override
    public void onActivityCallback(String str) {
        RecycleBoxListBean recycleBoxListBean = new RecycleBoxListBean().fetch();
        if (recycleBoxListBean==null){
            showSnackBar(viewHolder,"数据异常");
            return;
        }

        if (getScanType() == -1) {
            showSnackBar(viewHolder,"请选择回收类型");
            return;
        }

        boolean flag = checkRepeatBox(str,recycleBoxListBean);
        if (flag){
            createRecycleBox(str,recycleBoxListBean);
        }

    }



    private boolean checkRepeatBox(String str,RecycleBoxListBean recycleBoxListBean) {
        //判断是否存在列表,且状态一致

        List<RecycleBoxBean> recycleBoxBeanList = recycleBoxListBean.getList();
        for (RecycleBoxBean recycleBoxBean : recycleBoxBeanList){
            if (recycleBoxBean.getBoxNo().equals(str) ){
                if (recycleBoxBean.getType() == getScanType()){
                    showSnackBar(viewHolder,"重复扫描");
                    mediaBean.replay(R.raw.warn);
                }else{
                    recycleBoxBean.setType(getScanType());
                    mediaBean.replay(R.raw.success);
                    showSnackBar(viewHolder, AppUtil.stringForart("[%s] 已更新类型",str));
                    recycleBoxListBean.save();
                    listDataRefresh();
                }
                return false;
            }
        }
        return true;
    }

    private void createRecycleBox(String str,RecycleBoxListBean recycleBoxListBean) {
        RecycleBoxBean recycleBoxBean = new RecycleBoxBean(str,getScanType(),
                vehicleInfoBean.getDriverCode(),
                vehicleInfoBean.getCarNumber(),
                System.currentTimeMillis());

        List<RecycleBoxBean> recycleBoxBeanList = recycleBoxListBean.getList();
        recycleBoxBeanList.add(0,recycleBoxBean);
        mediaBean.replay(R.raw.success);
        showSnackBar(viewHolder,AppUtil.stringForart("[%s] 已添加至回收列表",str));
        recycleBoxListBean.save();
        listDataRefresh();
    }

    private int getScanType(){
        if (curType == viewHolder.recyclToBtn.getId()) return 1;
        if (curType == viewHolder.backToBtn.getId()) return 2;
        if (curType == viewHolder.tanslateToBtn.getId()) return 3;
        return curType;
    }

    @Override
    public void onClick(View v) {
        final String boxNo = viewHolder.input.getText().toString();
        if (!TextUtils.isEmpty(boxNo)){
            pool.post(new Runnable() {
                @Override
                public void run() {
                    onActivityCallback(boxNo);
                }
            });
            viewHolder.input.setText("");
        }
    }
}
