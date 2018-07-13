package ping.otmsapp.assemblys.activitys

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import ping.otmsapp.R
import ping.otmsapp.assemblys.dialogs.DialogBuilder
import ping.otmsapp.entitys.dataBeans.login.LoginUserBean
import ping.otmsapp.utils.MD5Util
import ping.otmsapp.utils.Ms
import ping.otmsapp.viewControls.ProgressBarControl
import ping.otmsapp.viewHolders.activity.LoginViewHolder
import ping.otmsapp.zeroCIce.IceIo
import java.lang.Exception
import java.util.*
import java.util.regex.Pattern

/**
 * Created by lzp on 2018/2/24.
 *权限与主界面选择
 */
class Login : Activity(), View.OnClickListener {

    var user: LoginUserBean? = null

    private fun getPage(name: String): Intent {
        val cls = pageMap.get(name)
        return Intent(Intent(this@Login, cls))
    }

    private fun toActivity(actName: String) {
        try {
            startActivity(getPage(actName))
            finish()
        } catch (e: Exception) {
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
            if (isIgnoreBatteryOption() && getPermissions()) {
                enter()
            }
        } else {
            enter()
        }
    }

    // 进入应用
    private fun enter() {
        if (user!!.isAccess) {
            toActivity("Index");
        }
    }

    @TargetApi(23)
    private fun getPermissions(): Boolean {
        if (isPermissionsDenied) {
            requestPermissions(permissions, SDK_PERMISSION_REQUEST)
            return false;
        }
        return true
    }

    /**权限申请回调
     * grantResults 授权结果数组
     * permissions 权限数组
     */
    @TargetApi(23)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Ms.Holder.get().info("授权结果: "
                + requestCode + "\n"
                + Arrays.toString(permissions) + "\n"
                + Arrays.toString(grantResults))

        if (requestCode == SDK_PERMISSION_REQUEST) {
            if (permissions.size == 0 || grantResults.size == 0) {
                Toast.makeText(this, "授权检测异常", Toast.LENGTH_SHORT).show()
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
        DialogBuilder.prompt(this,
                "应用授权",
                "您拒绝了相关权限,将无法正常使用,请手动授予",
                R.drawable.ic_launcher,
                "手动授权", DialogInterface.OnClickListener { dialog, which -> startSysSettingActivity() },
                "退出", DialogInterface.OnClickListener { dialog, which -> finish() })
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


    private fun startSysPowerSetting() {
        val intent = Intent()
        intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
        intent.data = Uri.parse("package:" + packageName)
        startActivityForResult(intent, SDK_POWER_REQUEST);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == SDK_POWER_REQUEST) {
            if (resultCode == RESULT_CANCELED) {
                DialogBuilder.prompt(this,
                        "应用授权",
                        "请忽略系统电量优化方案",
                        R.drawable.ic_launcher,
                        "接受", DialogInterface.OnClickListener { dialog, which -> startSysPowerSetting() },
                        "拒绝", DialogInterface.OnClickListener { dialog, which -> finish() })
            } else {
                check()
            }
        }
    }


    /**
     * 针对M以上的Doze模式
     *忽略电池的优化
     * @param activity
     */
    fun isIgnoreBatteryOption(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val packageName = getPackageName();
                val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    startSysPowerSetting()
                    return false;
                }
            } catch (e: Exception) {
                Ms.get().error(e)
            }
        }
        return true;
    }

    //验证手机号码
    private fun validatePhone(phone: String?): Boolean {
        if (phone == null) return false
        val PHONE_PATTERN = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}\$"
        return Pattern.compile(PHONE_PATTERN).matcher(phone).matches()
    }

    //验证密码
    private fun validatePassword(password: String): Boolean {
        return password.length > 5
    }

    override fun onClick(v: View) {
        if (progressControl!!.isShowing){
            Snackbar.make(viewHolder!!.layoutFileRid, "正在连接中,请稍后", Snackbar.LENGTH_LONG).show()
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
                viewHolder!!.passTextView.error = "无效密码"
                return
            }
        }

        progressControl!!.showProgressBar()//打开进度条

        IceIo.get().pool.post {
            try {
                val u = IceIo.get().login(phone, MD5Util.encryption(password))
                Ms.get().printObject(u)
                if (u!=null && u.userid != 0 && u.roleid.toInt() == LoginUserBean.driverCode) {
                   saveUser(u.userid.toString());

                }else{
                    throw IllegalAccessException("用户不存在或角色不正确")
                }
            } catch (e: Exception) {
                Ms.get().error(e);
                Snackbar.make(viewHolder!!.layoutFileRid, "登陆失败,用户名密码不正确或网络连接失败", Snackbar.LENGTH_LONG).show()
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
    private val pageMap = mapOf<String, Class<out Activity>>(
            "Index" to Index::class.java
    )
    private var viewHolder: LoginViewHolder? = null
    private var progressControl: ProgressBarControl? = null
}