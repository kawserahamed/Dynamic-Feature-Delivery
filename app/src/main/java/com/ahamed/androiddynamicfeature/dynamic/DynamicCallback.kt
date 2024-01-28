package com.ahamed.androiddynamicfeature.dynamic

interface DynamicCallback {
    fun onDownloading()
    fun onDownloadCompleted()
    fun onInstallSuccess()
    fun onFailed(errorMessage: String)
}