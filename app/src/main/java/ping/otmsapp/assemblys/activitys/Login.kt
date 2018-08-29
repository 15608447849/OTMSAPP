package ping.otmsapp.assemblys.activitys

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import ping.otmsapp.R
import ping.otmsapp.assemblys.dialogs.DialogBuilder
import ping.otmsapp.entitys.dataBeans.login.LoginUserBean
import ping.otmsapp.utils.AppUtil
import ping.otmsapp.utils.MD5Util
import ping.otmsapp.utils.Ms
import ping.otmsapp.viewControls.ProgressBarControl
import ping.otmsapp.viewHolders.activity.LoginViewHolder
import ping.otmsapp.zeroCIce.IceIo
import java.lang.Exception

/**
 * Created by lzp on 2018/2/24.
 * 权限与主界面选择
 */
class Login : Activity(), View.OnClickListener {

    //当前用户
    var user: LoginUserBean? = null

    //进入主界面
    private fun toIndex() {
        try {
            startActivity(Intent(this@Login, Index::class.java))
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewHolder = LoginViewHolder(this)
        setContentView(viewHolder!!.layoutFileRid)
        viewHolder!!.setOnClickListener(this)
        viewHolder!!.setProgressIndeterminate(true)
        progressControl = ProgressBarControl(Handler(), viewHolder!!.progressBar)
        initUser()
        check()
    }

    private fun initUser() {
        user = LoginUserBean().fetch() //创建用户对象
        if (user == null) user = LoginUserBean()
    }


    private fun check() {
        //查看当前API等级是否大于6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissions()) {
                enter()
            }
        } else {
            enter()
        }
    }

    // 进入应用
    private fun enter() {
        if (user!!.isAccess) {
            toIndex();
        }
    }

    //检查权限
    @TargetApi(23)
    private fun checkPermissions(): Boolean {
        if (isPermissionsDenied) {
            requestPermissions(permissions, SDK_PERMISSION_REQUEST)
            return false;
        }
        return true
    }

    /** 权限申请回调
     * grantResults 授权结果数组
     * permissions 权限数组
     */
    @TargetApi(23)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == SDK_PERMISSION_REQUEST) {
            if (permissions.isEmpty() || grantResults.isEmpty()) {
                AppUtil.toast(this@Login,"授权检测异常");
                return
            }
            if (grantResults.isNotEmpty()) {
                isPermissionsDenied = false //假设已授权
                for (result in grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        isPermissionsDenied = true //发现有一个权限未授予,则无权限访问
                        break
                    }
                }
            } else {
                isPermissionsDenied = true
            }
            if (isPermissionsDenied) {
                openAppDetails()
            } else {
                enter()
            }
        }
    }


    /**
     * 打开 APP 的详情设置
     */
    private fun openAppDetails() {
        DialogBuilder.prompt(
                this,
                "应用授权",
                "您拒绝了相关权限,将无法正常使用,请手动授予",
                R.drawable.ic_launcher,
                "手动授权",
                DialogInterface.OnClickListener { dialog, which -> startSysSettingActivity() },
                "退出",
                DialogInterface.OnClickListener { dialog, which -> finish() })
    }

    private fun startSysSettingActivity() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:" + packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        startActivity(intent)
        finish()
    }


    //验证手机号码
    private fun validatePhone(phone: String): Boolean {
        return phone.length == 11;
    }

    //验证密码
    private fun validatePassword(password: String): Boolean {
        return password.length > 5
    }

    override fun onClick(v: View) {
        if (progressControl!!.isShowing){
            AppUtil.toast(this@Login,"正在连接服务器");
            return
        }

        viewHolder!!.phoneTextView.isErrorEnabled = false
        viewHolder!!.passTextView.isErrorEnabled = false

        //获取输入框内容
        val phone = viewHolder!!.phoneTextView.editText!!.text.toString()
        val password = viewHolder!!.passTextView.editText!!.text.toString()

        if (!user!!.isAccess) {
            if (!validatePhone(phone)) {
                viewHolder!!.phoneTextView.error = "请输入正确格式的手机号码"
                return
            } else if (!validatePassword(password)) {
                viewHolder!!.passTextView.error = "密码长度不正确"
                return
            }
        }

        progressControl!!.showProgressBar()//打开进度条

        IceIo.get().pool.post {
            try {
                val u = IceIo.get().login(phone, MD5Util.encryption(password))
                Ms.Holder.get().printObject(u)

                if (u!=null && u.userid != 0 && u.roleid.toInt() == LoginUserBean.driverCode) { //司机角色码2
                   saveUser(u.userid.toString());

                }else{
                    throw IllegalAccessException("用户不存在或角色不正确")
                }
            } catch (e: Exception) {
                Ms.Holder.get().error(e);
                runOnUiThread{
                    AppUtil.toast(this@Login,"用户名密码不正确或网络不可用")
                }
            }
            runOnUiThread {
                progressControl!!.hideProgressBar()
            }
            check()
        }

    }
    //保存用户
    private fun saveUser(uid: String) {
        user!!.isAccess = true
        user!!.userCode = uid
        user!!.save()
    }


    override fun onDestroy() {
        viewHolder!!.destroy()
        super.onDestroy()
    }

    private val SDK_PERMISSION_REQUEST = 127
    private val SDK_POWER_REQUEST = 128
    private var isPermissionsDenied = true //权限被拒绝

    private val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE, // 写sd卡
            Manifest.permission.ACCESS_FINE_LOCATION, //GPS
            Manifest.permission.ACCESS_COARSE_LOCATION, //NET LOCATION
            Manifest.permission.READ_PHONE_STATE//获取手机信息
    )

    private var viewHolder: LoginViewHolder? = null
    private var progressControl: ProgressBarControl? = null
}