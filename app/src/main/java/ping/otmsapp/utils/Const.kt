package ping.otmsapp.utils

/**
 * Created by user on 2018/3/7.
 */
object KEY {
    const val SP_KEY_LOGIN_USER = "SP_KEY_LOGIN_USER";

    const val BUNDLE_KEY_TYPE = "BUNDLE_KEY_TYPE";
    const val BUNDLE_KEY_DATA = "ACYK_BUNDLE_DATA";
    const val INTENT_KEY_BUNDLE = "INTENT_KEY_BUNDLE";



    const val TYPE_BY_MEMORY = "TYPE_BY_MEMORY"; //收到内存存储对象
    const val TYPE_BY_DISPATCH = "TYPE_BY_DISPATCH"; //收到调度信息类型
    const val TYPE_BY_CLEAR_DISPATCH = "TYPE_BY_CLEAR_DISPATCH"; //收到清理调度信息类型
    const val TYPE_BY_WARN = "TYPE_BY_WARN"; //收到预警信息类型
    const val TYPE_BY_OPEN_GPS = "TYPE_BY_OPEN_GPS"; //打开GPS


    const val STORE_KEY_DISPATCH_BEAN = "STORE_KEY_DISPATCH_BEAN";
    const val STORE_KEY_WARN_LIST = "STORE_KEY_WARN_LIST";
    const val STORE_KEY_WARN_UNREAD_INDEX = "MEMK_WARNINDEX";

    //远程状态同步对象 sp 标识
    const val SP_FLAG_REMOTE = "FLAG_REMOTE";

    //异常列表-存储标识
    const val SP_FLAG_ABNORMAL = "SP_FLAG_ABNORMAL";
    //intent key
    const val INTENT_DETAIL_TYPE = "INTENT_DETAIL_TYPE"
    //预警标识存储
    const  val WARN_STORE_KEY = "WARN_STORE_KEY";

}

object STATE{
    /**
     * 调度对象 -状态
     */
    const val DISPATCH_DEAL_LOAD = 10;//待装载
    const val DISPATCH_DEAL_TAKEOUT = DISPATCH_DEAL_LOAD+1;//待启程
    const val DISPATCH_DEAL_UNLOAD = DISPATCH_DEAL_TAKEOUT+1;//待卸货
    const val DISPATCH_DEAL_BACK = DISPATCH_DEAL_UNLOAD+1;//待返程
    const val DISPATCH_DEAL_COMPLETE = DISPATCH_DEAL_BACK+1;//完成

    /**
     * 门店状态
     */
    const val STORE_DEAL_LOAD = 20;//等待装货完成
    const val STORE_DEAL_UNLOAD = STORE_DEAL_LOAD+1;//等待卸货完成
    const val STORE_DEAL_COMPLETE = STORE_DEAL_UNLOAD+1;//卸货完成


    /**
     * 箱子状态
     */
    const val BOX_DEAL_LOAD = 30;// 等待扫码装箱
    const val BOX_DEAL_UNLOAD = BOX_DEAL_LOAD+1;// 等待扫码卸货
    const val BOX_DEAL_RECYCLE = BOX_DEAL_UNLOAD+1; // 等待扫码回收
}

object RecodeTraceState {
    const val RECODE_WAIT = 0;  //等待记录
    const val RECODE_ING = RECODE_WAIT+1;    //记录中
    const val RECODE_FINISH = RECODE_ING+1; //记录完成
}