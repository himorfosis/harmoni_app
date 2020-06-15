package com.harmonievent.utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageCorePermission {

    var STORAGE_PERMISSION_CODE = 123
    val directoryStorage = File(Environment.getExternalStorageDirectory().absolutePath, "/Harmoni")

    fun saveImageInStorage(bitmap: Bitmap): File? {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapdata = bos.toByteArray()
        //write the bytes in file
        try {
            val fos = FileOutputStream(directoryStorage)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return directoryStorage
    }

    fun selectedImageInStorage() {

        // Selected image is local image
//        val b = BitmapDrawable(applicationContext.resources, bitmapRotate).bitmap
//        val i = (b.height * (512.0 / b.width)).toInt()
//        bitmapRotate = Bitmap.createScaledBitmap(b, 512, i, true)

    }

    private fun checkImageInStorage(fileImageName: String):Boolean {
        val dir = File(Environment.getExternalStorageDirectory().absolutePath, "/Harmoni")
        val file = File(dir, fileImageName)
        return if (file.exists()) { true } else { false}
    }

    fun fecthImagePath(context: Context, uri: Uri): String? {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = context.contentResolver.query(
            uri, projection, null, null,
            null
        )
        return if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            filePath
        } else uri.path
    }

    fun isWriteStoragePermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                isLog("Permission Is  Granted Write")
                true
            } else {
                ActivityCompat.requestPermissions(
                    (context as Activity),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_CODE
                )
                isLog("Permission Is Revoked")
                false
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation
            isLog("Permission Is Granted")
            true
        }
    }

    fun isReadStoragePermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                isLog("Permission Is Granted Read")
                ActivityCompat.requestPermissions(
                    (context as Activity),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_CODE
                )
                true
            } else {
                ActivityCompat.requestPermissions(
                    (context as Activity),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_CODE
                )
                isLog("Permission is revoked")
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            ActivityCompat.requestPermissions(
                (context as Activity),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
            isLog("Permission Is Granted")
            true
        }
    }

    fun requestStoragePermission(context: Context?) {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)

            if (ActivityCompat.shouldShowRequestPermissionRationale((context as Activity?)!!, Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions((context as Activity?)!!,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    private fun isLog(msg: String) {

        Log.e("ImageCorePermis", msg)

    }
}