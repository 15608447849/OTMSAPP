package ping.otmsapp.entitys.background;

import android.support.annotation.NonNull;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ping.otmsapp.utils.Ms;

/**
 * Created by lzp on 2018/3/5.
 * 网络访问
 * 线程池
 *
 */

public class IOThreadPool {
    private ThreadPoolExecutor executor;
    public IOThreadPool() {

        executor = createIoExecutor();
        createIoExecutor();
    }
    private ThreadPoolExecutor createIoExecutor() {
        //搭建线程池
         return new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),//核心线程数
                200,//线程池所能容纳的最大线程数
                30L,//非核心线程的闲置超时时间
                TimeUnit.SECONDS,//指定keepAliveTime的单位
                new ArrayBlockingQueue<Runnable>(500), //线程池中的任务队列
                new ThreadFactory(){

                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("ping@tio#"+thread.getId());
                        return thread;
                    }
                },
                new RejectedExecutionHandler(){
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        //超过IO线程池处理能力的任务, 丢弃
                        Ms.Holder.get().error(new IllegalThreadStateException("Pool - "+executor+" 无法处理任务 - "+ r));
                    }
                }
        );
    }
    public void post(Runnable runnable){
        executor.execute(runnable);
    }
    public void close(){
        if (executor!=null) executor.shutdownNow();
    }
}
