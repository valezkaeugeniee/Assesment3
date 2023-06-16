package org.d3if3062.mobpro1.ui.api

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3062.mobpro1.model.Kegiatan
import org.d3if3062.mobpro1.netwrok.KegiatanApi
import org.d3if3062.mobpro1.netwrok.UpdateWorker
import java.util.concurrent.TimeUnit

class ApiViewModel : ViewModel() {
    private val data = MutableLiveData<List<Kegiatan>>()
    private val status = MutableLiveData<KegiatanApi.ApiStatus>()
    init {
        retrieveData()
    }

    private fun retrieveData(){
        viewModelScope.launch (Dispatchers.IO){
           status.postValue(KegiatanApi.ApiStatus.LOADING)
            try {
              data.postValue((KegiatanApi.service.getKegiatan()))
                status.postValue(KegiatanApi.ApiStatus.SUCCESS)
            }catch (e:Exception){
                Log.d("ApiViewModel", "Failure:${e.message}")
                status.postValue(KegiatanApi.ApiStatus.FAILED)
            }
        }
    }
    fun getData(): LiveData<List<Kegiatan>> = data
    fun getStatus(): LiveData<KegiatanApi.ApiStatus> = status
    fun scheduleUpdater(app: Application) {
        val request = OneTimeWorkRequestBuilder<UpdateWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(app).enqueueUniqueWork(
            UpdateWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

}