package ping.otmsapp.entitys.dataBeans.sys;

import java.util.HashMap;

/**
 * Created by user on 2018/3/20.
 */

public class MemoryStoreBean {
    //对象存储
    private final HashMap<String,Object> map;
    public MemoryStoreBean() {
        this.map = new HashMap();
    }
    public void put(String k,Object v){
        map.put(k,v);
    }
    public <T> T get(String k,T def){
        Object o = map.get(k);
        if (o==null) return def;
        else return (T)o;
    }
    public Object remove(String k) {
        if (map.containsKey(k))  return map.remove(k);
        else return null;
    }
    public HashMap<String, Object> getMap() {
        return map;
    }
}
