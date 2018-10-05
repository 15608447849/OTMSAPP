package ping.otmsapp.assemblys.fragments.imps.secondPage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import ping.otmsapp.R;
import ping.otmsapp.assemblys.fragments.base.BaseFragment;
import ping.otmsapp.entitys.apps.MediaBean;
import ping.otmsapp.entitys.dataBeans.dispatch.BoxBean;
import ping.otmsapp.entitys.dataBeans.dispatch.DispatchBean;
import ping.otmsapp.entitys.dataBeans.dispatch.DistributionPathBean;
import ping.otmsapp.entitys.dataBeans.dispatch.RecycleBoxBean;
import ping.otmsapp.entitys.dataBeans.dispatch.RecycleBoxListBean;
import ping.otmsapp.entitys.dataBeans.dispatch.VehicleInfoBean;
import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.viewAdpters.second.ScanRecycleBoxRecycleAdapter;
import ping.otmsapp.viewHolders.fragments.second.ScanRecycleBoxViewHolder;

import static ping.otmsapp.utils.STATE.BOX_DEAL_RECYCLE;
import static ping.otmsapp.utils.STATE.STORE_DEAL_COMPLETE;

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
    private String[] storeNameArr;
    private String[] storeIdArr;
    private int storePos = 0;
    private List<String> unloadBoxCodeList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (viewHolder==null){
            init();
            viewHolder = new ScanRecycleBoxViewHolder(mContext);
            adapter = new ScanRecycleBoxRecycleAdapter(mContext);
            viewHolder.list.setRecyclerDefaultSetting(adapter,1);
            viewHolder.radioGroup.setOnCheckedChangeListener(this);
            mediaBean = new MediaBean(mContext);
            viewHolder.setOnClickListener(this);
            initStoreInfo();
            listDataRefresh();
        }
        return viewHolder.getLayoutFileRid();
    }
    
    private void init() {
        try{
            DispatchBean dispatchBean = new DispatchBean().fetch();
            assert dispatchBean!=null;
            vehicleInfoBean  = dispatchBean.getVehicleInfoBean();
            List<DistributionPathBean> list = dispatchBean.getDistributionPathBean();
            storeNameArr = new String[list.size()];
            storeIdArr = new String[list.size()];

            //门店
            DistributionPathBean d;
            for (int i = 0; i< list.size();i++){
                d = list.get(i);
                storeNameArr[i] = d.getStoreName();
                storeIdArr[i] =  d.getCustomerAgency();
                if (storePos == 0){
                    if (d.getUnloadScanIndex() == d.getBoxSum()) storePos = i;
                }
                if (d.getState() == STORE_DEAL_COMPLETE){
                    continue;
                }
                //查看当前没有卸载的箱子箱号
                for (BoxBean boxBean : d.getBoxNoList()){
                    if (boxBean.getState() == BOX_DEAL_RECYCLE){
                        continue;
                    }
                    unloadBoxCodeList.add(boxBean.getBarCode());
                }
            }
            
        }catch (Exception e){
            finishSelf();
        }
    }
    
    private void initStoreInfo() {
        toUi(new Runnable() {
            @Override
            public void run() {
                viewHolder.address.setText(AppUtil.stringForart("当前门店: %s",storeNameArr[storePos]));
            }
        });
    }
    
    //初始化数据
    private void listDataRefresh() {
        toIO(new Runnable() {
            @Override
            public void run() {
                final RecycleBoxListBean recycleBoxListBean = new RecycleBoxListBean().fetch();
//                recycleBoxListBean.getList().clear();
//                recycleBoxListBean.save();
                if (recycleBoxListBean!=null && recycleBoxListBean.getList().size()>0){
                    adapter.clearAll();
                    for (RecycleBoxBean recycleBoxBean : recycleBoxListBean.getList()){
                        if (!recycleBoxBean.isBlank()) adapter.addData(recycleBoxBean);
                    }


                    toUi(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            viewHolder.title.setText(AppUtil.stringForart("回收列表(%s)",recycleBoxListBean.getList().size()));
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
        scanner(str);
    }

    private void scanner(final String str) {
        toIO(new Runnable() {
            @Override
            public void run() {
                scannerBox(str);
            }
        });
    }

    private void scannerBox(String str) {
        RecycleBoxListBean recycleBoxListBean = new RecycleBoxListBean().fetch();
        if (recycleBoxListBean==null){
            showSnackBar(viewHolder,"数据异常");
            return;
        }

        if (getScanType() == -1) {
            showSnackBar(viewHolder,"请选择回收类型");
            return;
        }

        if (unloadBoxCodeList.contains(str)) {
            showSnackBar(viewHolder,"箱号: "+ str+",请进行卸载操作再回收");
            return;
        }

        if (checkRepeatBox(str,recycleBoxListBean)) {
            createRecycleBox(str,recycleBoxListBean);
            mediaBean.replay(R.raw.success);
            showSnackBar(viewHolder,AppUtil.stringForart("[%s]已添加",str));
            recycleBoxListBean.save();
            listDataRefresh();
        }
    }


    private boolean checkRepeatBox(String str,RecycleBoxListBean recycleBoxListBean) {
        //判断是否存在列表,且状态一致

        List<RecycleBoxBean> recycleBoxBeanList = recycleBoxListBean.getList();
        for (RecycleBoxBean recycleBoxBean : recycleBoxBeanList){
            if (recycleBoxBean.getBoxNo().equals(str) ){
                if (recycleBoxBean.getType() == getScanType()){
                    showSnackBar(viewHolder,AppUtil.stringForart("[%s]已回收",str));
                    mediaBean.replay(R.raw.warn);
                }else{
                    recycleBoxBean.setType(getScanType());
                    mediaBean.replay(R.raw.success);
                    showSnackBar(viewHolder, AppUtil.stringForart("[%s]已更新",str));
                    recycleBoxListBean.save();
                    listDataRefresh();
                }
                return false;
            }
        }
        return true;
    }

    private void createRecycleBox(String str,RecycleBoxListBean recycleBoxListBean) {
        if (str==null) return;
        RecycleBoxBean recycleBoxBean = new RecycleBoxBean(
                str,
                getScanType(),
                storeIdArr[storePos],
                vehicleInfoBean.getDriverCode(),
                vehicleInfoBean.getCarNumber());

        List<RecycleBoxBean> recycleBoxBeanList = recycleBoxListBean.getList();
        recycleBoxBeanList.add(0,recycleBoxBean);
    }

    private int getScanType(){
        if (curType == viewHolder.recyclToBtn.getId()) return 1;
        if (curType == viewHolder.backToBtn.getId()) return 2;
        if (curType == viewHolder.tanslateToBtn.getId()) return 3;
        return curType;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == viewHolder.inputBtn.getId()){
            final String boxNo = viewHolder.input.getText().toString();
            if (!TextUtils.isEmpty(boxNo)){
                viewHolder.input.setText("");
                scanner(boxNo);
            }
        }else if (v.getId() == viewHolder.addBtn.getId()){
           openNumberInput();
        }else if (v.getId() == viewHolder.selectBtn.getId()){
            //打开门店选择列表
            openStoreList();
        }
    }

    private void openNumberInput() {
        if (getScanType() == -1 ) {
            showSnackBar(viewHolder,"请选择纸箱回收类型");
            return;
        }
        if (getScanType() == 1){
            showSnackBar(viewHolder,"不可选中的回收类型");
            return;
        }

        final EditText editText = new EditText(mContext);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("请输入纸箱数量");//提示框标题
        builder.setView(editText);
        builder.setPositiveButton("确定",//提示框的两个按钮
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final ProgressDialog progress = new ProgressDialog(mContext);
                            progress.setIcon(R.drawable.ic_launcher);
                            progress.setTitle("添加纸箱");
                            progress.setMessage("请等待");
                            progress.setIndeterminate(true);
                            progress.setCancelable(false);
                            progress.show();   //打开进度圈
                        toIO(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    int number = Integer.parseInt(editText.getText().toString());
                                    RecycleBoxListBean recycleBoxListBean = new RecycleBoxListBean().fetch();
                                    if (recycleBoxListBean==null){
                                        showSnackBar(viewHolder,"数据异常");
                                        return;
                                    }
                                    if (recycleBoxListBean.getList().size()+ number > 1000) {
                                        showSnackBar(viewHolder,"纸箱超过上限1000个");
                                        return;
                                    }
                                    for (int i = 0; i < number; i++){
                                        createRecycleBox("",recycleBoxListBean);
                                    }
                                    recycleBoxListBean.save();
                                    listDataRefresh();
                                    showLongSnackBar(viewHolder,"成功添加纸箱"+number+"个");
                                } catch (Exception ignored) {
                                    showLongSnackBar(viewHolder,"添加纸箱失败");
                                }finally {
                                   toUi(new Runnable() {
                                       @Override
                                       public void run() {
                                           progress.cancel();
                                       }
                                   });
                                }
                            }
                        });

                    }
                });
        builder.create().show();
    }

    private void openStoreList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("请选择");
        builder.setItems(storeNameArr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                storePos = which;
                initStoreInfo();
            }
        });
        builder.create().show();
    }
}
