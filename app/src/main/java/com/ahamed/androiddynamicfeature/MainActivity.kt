package com.ahamed.androiddynamicfeature

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.ahamed.androiddynamicfeature.databinding.ActivityMainBinding
import com.ahamed.androiddynamicfeature.dynamic.DynamicCallback
import com.ahamed.androiddynamicfeature.dynamic.DynamicModuleUtil
import java.util.Calendar

class MainActivity : AppCompatActivity(), DynamicCallback {

    private val DYNAMIC_MODULE = "dynamic_activity"
    private val stateLiveData = MutableLiveData<String>()
    private lateinit var dynamicModuleUtil: DynamicModuleUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dynamicModuleUtil = DynamicModuleUtil(baseContext, this)

        stateLiveData.observe(this) {
            binding.tvState.text = it
        }

        binding.btnOpen.setOnClickListener {
            if (dynamicModuleUtil.isModuleDownloaded(DYNAMIC_MODULE)) {
                stateLiveData.value += "${getCurrentTimestamp()}: Module is already downloaded.\n"
                startCustomerSupportActivity()
            } else {
                dialogShow()
            }
        }
    }

    private fun dialogShow() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setIcon(R.drawable.ic_launcher_foreground)
            setMessage("Download Dynamic Module")
            setPositiveButton("YES") { _: DialogInterface?, _: Int ->
                stateLiveData.value += "${getCurrentTimestamp()}: Call for download.\n"
                dynamicModuleUtil.downloadDynamicModule(DYNAMIC_MODULE)
            }
            setNegativeButton("NO") { _, _ ->

            }

        }.create().show()
    }

    override fun onDownloading() {
        stateLiveData.value += "${getCurrentTimestamp()}: Downloading...\n"
    }

    override fun onDownloadCompleted() {
        stateLiveData.value += "${getCurrentTimestamp()}: Module download completed.\n"
    }

    override fun onInstallSuccess() {
        stateLiveData.value += "${getCurrentTimestamp()}: Module install Success!\n"
        startCustomerSupportActivity()
    }

    override fun onFailed(errorMessage: String) {
        stateLiveData.value += "${getCurrentTimestamp()}: $errorMessage\n"
    }

    private fun getCurrentTimestamp(): String {
        val calendar = Calendar.getInstance()
        return "${calendar.get(Calendar.HOUR).toString().padStart(2, '0')}:" + "${
            calendar.get(
                Calendar.MINUTE
            ).toString().padStart(2, '0')
        }:" + calendar.get(Calendar.SECOND).toString().padStart(2, '0')
    }

    private fun startCustomerSupportActivity() {
        val intent = Intent()
        intent.setClassName(
            "com.ahamed.androiddynamicfeature", "com.ahamed.dynamic_activity.DynamicActivity"
        )
        startActivity(intent)
    }
}