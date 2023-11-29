package com.example.mediademo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import com.blankj.utilcode.util.PermissionUtils

/**
 * @description:
 * @author yanglei
 * @date :2023/11/29
 * @version 1.0.0
 */
class PermissionUtil {


    private val externalStoragePermission = arrayOf(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )
    /**
     * 检查是否有 路径读写权限
     */
    fun isExternalStoragePermission(): Boolean {
        return PermissionUtils.isGranted(*externalStoragePermission)
    }

    /**
     * 申请 路径读写权限
     */
    fun requestExternalStoragePermission(){
        PermissionUtils.permission(*externalStoragePermission).request()
    }


    /**
     * 检查是否有 所有文件管理权限，Android 11以上必须申请此权限
     * 返回false 代表没有权限
     * 返回true 代表有权限或当前不需要此权限
     */
    fun isStorageManagerPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                return false
            }
        }
        return true
    }

    /**
     * 申请 所有文件管理权限
     */
    fun requestStorageManagerPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val uri: Uri = Uri.parse("package:${context.packageName}")
                val intent =
                    Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                context.startActivity(intent)
            } catch (ex: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                context.startActivity(intent)
            }
        }
    }

}