package ping.otmsapp.assemblys.activitys

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_picture_up.*
import ping.otmsapp.R
import ping.otmsapp.viewControls.ProgressBarControl
import ping.otmsapp.viewHolders.activity.LoginViewHolder
import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import ping.otmsapp.utils.AppUtil
import ping.otmsapp.utils.AppUtil.saveBitmapFile
import ping.otmsapp.utils.Ms
import ping.otmsapp.zeroCIce.IceClient
import ping.otmsapp.zeroCIce.IceIo
import java.io.File


class PictureUp : Activity(){

    val imageFile = File(Environment.getExternalStorageDirectory(),"temp.png")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_up)
        pictureup_ibtn_back.setOnClickListener { finish() }
        pictureup_btn_album.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent,100)
        }
        pictureup_btn_take.setOnClickListener {
            val intent = Intent()
            intent.action = MediaStore.ACTION_IMAGE_CAPTURE
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            val uri = Uri.fromFile(imageFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(intent,200)
        }
        pictureup_btn_up.setOnClickListener {
            IceIo.get().pool.post {
                if (imageFile.exists()&&imageFile.length()>0){
                    IceIo.get().uploadImage(imageFile)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            if (resultCode == RESULT_OK){

                if (requestCode == 100){
                    IceIo.get().pool.post{
                        val uri = data?.data
                        val cr = this.contentResolver
                        val bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri))
                        if (AppUtil.saveBitmapFile(bitmap,imageFile)){
                            pictureup_iv.setImageBitmap(bitmap)
                        }

                    }
                }
                else if (requestCode == 200){
                    val bitmap = BitmapFactory.decodeStream(imageFile.inputStream())
                    pictureup_iv.setImageBitmap(bitmap)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}