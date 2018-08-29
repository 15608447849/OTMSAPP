package ping.otmsapp.entitys.dataBeans.localStore;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ping.otmsapp.utils.AppUtil;
import ping.otmsapp.utils.DB;
import ping.otmsapp.utils.IStoreData;

/**
 * Created by Leeping on 2018/8/27.
 * email: 793065165@qq.com
 */

public abstract class DbJsonStoreAbs implements IStoreData {

    @NotNull
    @Override
    public String getValue() {
        return AppUtil.javaBeanToJson(this);
    }

    @Override
    public void save() {
        DB.Holder.get().putString(selfKey(),getValue());
    }

    @Nullable
    @Override
    public <T extends IStoreData> T fetch() {
        try {
            String json = DB.Holder.get().getString(selfKey());
            if (json != null) return (T) AppUtil.jsonToJavaBean(json,this.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove() {
        DB.Holder.get().delString(selfKey());
    }
}
