package ping.otmsapp.entitys.interfaces;

import ping.otmsapp.entitys.dataBeans.dispatch.DispatchBean;
import ping.otmsapp.entitys.dataBeans.dispatch.DistributionPathBean;

/**
 * Created by Leeping on 2018/4/8.
 * email: 793065165@qq.com
 */

public interface ScanBoxHandleCallback {


    boolean starScanStore(DispatchBean dispatchBean, DistributionPathBean distributionPathBean);

    void changeStoreState(DistributionPathBean distributionPathBean);

    void changeDispatchState(DispatchBean dispatchBean);

    void occurChange();

    void boxScanRepeat(String scanBarNo);

    void boxScanAbnormal(String scanBarNo);


    boolean handworkStore(DispatchBean dispatchBean, DistributionPathBean distributionPathBean);

    void boxScannerSuccess(String scanBarNo);

    void boxScanNoFind(String scanBarNo);


    public class Adapter implements ScanBoxHandleCallback{

        @Override
        public boolean starScanStore(DispatchBean dispatchBean, DistributionPathBean distributionPathBean) {
            return false;
        }

        @Override
        public void changeStoreState(DistributionPathBean distributionPathBean) {

        }

        @Override
        public void changeDispatchState(DispatchBean dispatchBean) {
        }

        @Override
        public void occurChange() {

        }

        @Override
        public void boxScanRepeat(String scanBarNo) {

        }

        @Override
        public void boxScanAbnormal(String scanBarNo) {

        }

        @Override
        public boolean handworkStore(DispatchBean dispatchBean, DistributionPathBean distributionPathBean) {
            return false;
        }

        @Override
        public void boxScannerSuccess(String scanBarNo) {

        }

        @Override
        public void boxScanNoFind(String scanBarNo) {

        }
    }
}
