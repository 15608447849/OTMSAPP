package ping.otmsapp.utils

import android.content.Context
import android.util.Log
import ping.otmsapp.entitys.interfaces.ReflectionTool
import java.io.File
import java.lang.Exception
import java.util.*


/**
 * Created by lzp on 2018/2/24.
 * 消息记录
 *
 */
class Ms private constructor(){

    companion object Holder {
        @Volatile private var instance: Ms? = null
        fun get():Ms{
            if (instance == null) {
                synchronized(Ms::class) {
                    if (instance == null) {
                        instance = Ms()
                    }
                }
            }
            return instance!!
        }
    }

    private val TAG = "TMS"

    //日志文件
    private var logFile:File? = null;

    fun init(c:Context){
        val folder = AppUtil.createFolder(c.filesDir.canonicalPath + File.separator+"logger")
        logFile = File(
                folder,
                  AppUtil.formatUTC(Date().time,"yyyyMMdd")+".log")
    }

    fun debug(any: Any?){
        try {
            Log.println(Log.DEBUG, TAG, any.toString())
        } catch (e: Exception) {
            error(e)
        }
    }

    fun debug(isPrint: Boolean,any: Any?){
        if(isPrint) debug(any)
    }

    fun <E : Exception> error(e:E){
        Log.println(Log.ERROR, TAG, AppUtil.printExceptInfo(e))
    }

    fun <E : Exception> error(isPrint:Boolean,e:E){
        if (isPrint) error(e)
    }

    // 写入日志文件
    fun writeFile(str:String){
        logFile!!.appendText(str)
    }

    //清理日志文件
    fun cleanFile(){
        logFile!!.writeText("")
    }

    //打印对象
    fun printObject(`object`: Any?) {
        if (`object` != null) {
            debug(ReflectionTool.reflectionObjectFields(`object`))
        }
    }

}