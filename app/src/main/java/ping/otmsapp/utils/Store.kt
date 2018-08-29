package ping.otmsapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
/**
 * Created by lzp on 2018/2/24.
 * 持久化存储
 */

//存储接口
interface IStore{
    fun create(context:Context)
    fun putString(k:String,v:String)
    fun getString(k:String):String?
    fun delString(k:String)
    fun destroy()
}

private val CONSTANTS_CONFIG_NAME = "otms_sp" //shaedPre 存储的文件名
private val DB_VERSION = 1 //数据库版本
private val DB_NAME ="otmsapp.db" //数据库名
private val DB_TABLE_NAME = "storeK_V" //数据库表名
private val DB_KEY_K = "key"
private val DB_KEY_V = "value"
private val DB_CREATE_SQL = "create table if not exists ${DB_TABLE_NAME} (${DB_KEY_K} text,${DB_KEY_V} text)";
private val DB_COLUMNS_ARRAY = arrayOf(DB_KEY_V)
private val DB_SELECTION_WHERE_ARRAY = "${DB_KEY_K}=?"

/**
 * SharedPreference实现
 */
class SP private constructor():IStore {
    companion object Holder{
        @SuppressLint("StaticFieldLeak")
        @Volatile private var instance: SP? = null
        fun get(): SP {
            if (instance == null) {
                synchronized(SP::class) {
                    if (instance == null) {
                        instance = SP()
                    }
                }
            }
            return instance!!
        }
    }
    private var context: Context? = null
    override fun create(context:Context){
        this.context = context;
    }
    override fun destroy(){
        this.context = null;
    }
    override fun putString(k:String, v:String) {
        val sharedPreferences = context!!.getSharedPreferences(CONSTANTS_CONFIG_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().apply({
            this.putString(k,v)
        }).apply()
    }
    override fun delString(k:String) {
        val sharedPreferences = context!!.getSharedPreferences(CONSTANTS_CONFIG_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().apply({
            this.remove(k)
        }).apply()
    }
    override fun getString(k:String):String?{
        val sharedPreferences = context!!.getSharedPreferences(CONSTANTS_CONFIG_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(k,null)
    }
}
/** 数据库存储实现*/
class DB private constructor() : IStore{
    companion object Holder{
        @Volatile private var instance: DB? = null
        fun get():DB{
            if (instance == null) {
                synchronized(DB::class) {
                    if (instance == null) {
                        instance = DB()
                    }
                }
            }
            return instance!!
        }
    }
    private var sqlHelper:SQLiteOpenHelper? = null
    override fun create(context: Context){
        sqlHelper = object :SQLiteOpenHelper(context, DB_NAME,null, DB_VERSION){
            /**
             * 创建
             */
            override fun onCreate(db: SQLiteDatabase?) {
                db!!.execSQL(DB_CREATE_SQL);
            }
            /**
             * 升级
             */
            override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
        }
    }
    override fun destroy(){
        sqlHelper!!.close()
        sqlHelper = null
    }
    /**
     * 存
     */
    override fun putString(k:String,v:String){
        val value  = getString(k)
        var sql = StringBuffer();
        if (value==null){
            sql.append("insert into $DB_TABLE_NAME($DB_KEY_K,$DB_KEY_V) values('$k','$v')")
        }else{
            sql.append("update $DB_TABLE_NAME set $DB_KEY_V = '$v' where $DB_KEY_K = '$k'")
        }
        sqlHelper!!.writableDatabase.execSQL(sql.toString())
    }
    /**
     * 取
     * query(String table,String[] columns,String selection,String[]  selectionArgs,String groupBy,String having,String orderBy,String limit);
    参数table:表名称
    参数columns:列名称数组
    参数selection:条件字句，相当于where的约束条件
    参数selectionArgs:条件字句，参数数组 为where中的占位符提供具体的值
    参数groupBy:分组列
    参数having:分组条件
    参数orderBy:排序列
    参数limit:分页查询限制
    select k,v form tablename where k="?"
     */
    override fun getString(k:String):String?{
        val db:SQLiteDatabase = sqlHelper!!.readableDatabase
        val cursor = db.query(DB_TABLE_NAME,DB_COLUMNS_ARRAY,
                DB_SELECTION_WHERE_ARRAY,
                arrayOf(k),
                null,
                null,
                null,
                null)
        if (cursor.count>0){
            if (cursor.moveToFirst()){
                return cursor.getString(0);
            }
        }
        return null
    }
    /**
     * 删
     */
    override fun delString(k:String){
        val db:SQLiteDatabase = sqlHelper!!.writableDatabase
        db.delete(DB_TABLE_NAME, DB_SELECTION_WHERE_ARRAY, arrayOf(k));
    }
}

//对象存储
interface IStoreData{
    fun selfKey():String
    fun getValue():String
    fun save()
    fun <T:IStoreData?> fetch():T?
    fun remove()
}



