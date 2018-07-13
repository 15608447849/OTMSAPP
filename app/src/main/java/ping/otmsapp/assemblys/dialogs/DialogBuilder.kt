package ping.otmsapp.assemblys.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import ping.otmsapp.R
import ping.otmsapp.entitys.interfaces.Action0

/**
 * Created by lzp on 2018/3/12.
 *
 */
object  DialogBuilder {
        fun prompt(context: Context,
                   title: String,
                   message: String,
                   iconRid: Int,
                   sureStr: String,
                   sureListener: DialogInterface.OnClickListener,
                   cancleStr: String,
                   cancleListener: DialogInterface.OnClickListener?) {

            val builder = AlertDialog.Builder(context)  //先得到构造器
            builder.setTitle(title) //设置标题
            builder.setMessage(message) //设置内容
            builder.setIcon(iconRid)//设置图标，
            builder.setPositiveButton(sureStr,sureListener)

            if(cancleListener!=null){
                builder.setNegativeButton(cancleStr,cancleListener)
            }else{
                builder.setNegativeButton(cancleStr){dialog,which->
                    dialog.dismiss()
                }
            }
            var dialog  = builder.create();
                dialog.setCancelable(false)//点击对话框外面和按返回键对话框不会消失
                dialog.show() //创建并显示出来

        }

    fun dialogSimple(c:Context,msg: String, action0: Action0) {
        //弹出提示
        DialogBuilder.prompt(c,
                "提示",
                msg,
                R.drawable.icon_warning,
                "确定",
                DialogInterface.OnClickListener { dialog, which ->
                    action0.onAction0()
                    dialog.dismiss()
                },
                "取消", null)
    }


}