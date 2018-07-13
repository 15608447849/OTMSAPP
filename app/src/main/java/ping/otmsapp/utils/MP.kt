package ping.otmsapp.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import ping.otmsapp.entitys.interfaces.ReflectionTool
import java.io.File
import java.lang.Exception




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

    private var logFile:File? = null;

    fun init(c:Context,fileName:String){
        logFile = File(c.filesDir, fileName)
    }

    fun info(any:Any?){
        try {
            Log.println(Log.ASSERT, TAG, any.toString())
        } catch (e: Exception) {
            error(e)
        }
    }


    fun info(isPrint:Boolean,any:Any?){
        if(isPrint)  Log.println(Log.ASSERT, TAG, any.toString())
    }
    fun debug(any: Any?){
       debug(true,any)
    }
    fun debug(isPrint: Boolean,any: Any?){
        if(isPrint)  Log.println(Log.DEBUG, TAG, any.toString())
    }

    fun <E : Exception> error(isPrint:Boolean,e:E){
        if (isPrint) Log.println(Log.ERROR, TAG, AppUtil.printExceptInfo(e))
        else e.printStackTrace()
    }
    fun <E : Exception> error(e:E){
       error(false,e)
    }

    fun fileWrite(context: Context,fileName: String,text:String){
        val f = File(context.filesDir, fileName)
        f.writeText(text)

    }
    /**
     * 追加到某个文件
     */
    fun fileAppend(str:String){
//        debug(str)
        logFile!!.appendText(str)
    }
    /**
     * 读取到SD卡下某个文件
     */
    fun fileReadAll(fileName:String ){
        try {
            val parent_path = Environment.getExternalStorageDirectory()
            val file = File(parent_path.absoluteFile, fileName)
            info( file.absoluteFile)
            file.writeText(logFile!!.readText())
        } catch (e: Exception) {
            error(e)
        }
    }
    fun fileClean(){
        logFile!!.writeText("")
    }

    fun printObject(`object`: Any?) {
        if (`object` != null) {

            debug(ReflectionTool.reflectionObjectFields(`object`))
        }
    }
}