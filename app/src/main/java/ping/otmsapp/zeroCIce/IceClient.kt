package ping.otmsapp.zeroCIce

import Ice.ObjectPrx
import Ice.Util.initialize
import ping.otmsapp.utils.Ms
import java.io.IOException
import java.util.*

@Suppress("UNCHECKED_CAST")
/**
 * Created by user on 2018/3/5.
 *  ICE 客户端
 */
class IceClient :Thread()  {
    private val LOOP_TIME= 30 * 1000L
    private var lastAccessTimestamp = 0L //最后接入时间
    private val idleTimeOutSeconds = 5 * 60 * 1000L//秒
    var args:Array<String>? = null

    init {
        isDaemon = true
        name = this.javaClass.simpleName
        start()
    }

    //代理类模块存储
    private val cls2PrxMap = HashMap<Class<out Ice.ObjectPrx>, Ice.ObjectPrx>()
    //是否检测连接
    private var isLoop = true
    //IC 客户端连接
    @Volatile private var iceCommunicator: Ice.Communicator? = null

    //获取代理 ,强制转换类型
    fun <T  : Ice.ObjectPrx> getServicePrx(serviceCls: Class<T>):T?{
            lastAccessTimestamp = System.currentTimeMillis()
            var proxy:T? = cls2PrxMap[serviceCls] as? T
            if (proxy == null) {
                val proxyCls = createIceProxy(getIceCommunicator(), serviceCls)
                val proxyHelper = Class.forName(serviceCls.name + "Helper").newInstance()
                val method = proxyHelper.javaClass.getDeclaredMethod("checkedCast", ObjectPrx::class.java)
                try {
                    proxy = method.invoke(proxyHelper, proxyCls) as T
                    cls2PrxMap.put(serviceCls, proxy)
                } catch (e: Exception) {
                    throw e
                }
            }
            return proxy
    }

        private fun createIceProxy(communicator: Ice.Communicator, serviceCls: Class<out Ice.ObjectPrx>): Ice.ObjectPrx {
        val serviceName = serviceCls.simpleName
        val pos = serviceName.lastIndexOf("Prx")
        if (pos <= 0) throw IllegalArgumentException("Invalid ObjectPrx class ,class name must end with Prx")
        return communicator.stringToProxy(serviceName.substring(0, pos))
    }
    //获取ICE 客户端连接
    private fun getIceCommunicator(): Ice.Communicator {
        if (iceCommunicator == null) {
            synchronized(this) {
                if (iceCommunicator == null) {
                    try {
                        iceCommunicator = initialize(this.args!!)
                    } catch (e: IOException) {
                        iceCommunicator = null
                    }
                }
            }
        }
        return iceCommunicator!!
    }
    
    //关闭客户端连接
    private fun closeCommunicator() {
        if (iceCommunicator != null){
            synchronized(this) {
                if (iceCommunicator != null){
                    try {
                        iceCommunicator!!.shutdown()
                    } catch (e: Exception) {
                        Ms.Holder.get().error(e)
                    } finally {
                        iceCommunicator!!.destroy()
                        iceCommunicator = null
                        if (!cls2PrxMap.isEmpty()) {
                                cls2PrxMap.clear()
                        }
                        lastAccessTimestamp = 0L
                    }
                }
            }
        }
    }

    override fun run() {
        while (isLoop){
            //30秒循环一次检测
            try { Thread.sleep(LOOP_TIME) } catch (e: Exception) { }
            // 最后时间 + 空闲时间 < 当前时间 ,认为长时间未使用连接.
            if (checkTimeout() ) {
                //关闭连接
                closeCommunicator()
            }
        }
    }

    private fun checkTimeout(): Boolean {
        if(lastAccessTimestamp == 0L) return false
        return (lastAccessTimestamp + idleTimeOutSeconds ) < System.currentTimeMillis()
    }

    fun close(){
        isLoop = false
        closeCommunicator()
    }


}