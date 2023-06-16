package org.d3if3062.mobpro1.netwrok

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if3062.mobpro1.model.Kegiatan
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL =  "https://raw.githubusercontent.com/" +
        "valezkaeugeniee/CalorieCount_Assesment2/staticAPI/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory((MoshiConverterFactory.create(moshi)))
    .baseUrl((BASE_URL))
    .build()

interface KegiatanApiService {
        @GET("apilocal.json")
        suspend fun getKegiatan():List<Kegiatan>
}

object KegiatanApi{

    enum class ApiStatus{LOADING, SUCCESS,FAILED}
    val service: KegiatanApiService by lazy{
        retrofit.create(KegiatanApiService::class.java)
    }

    fun getKegiatanUrl(imageId: String): String{
        return "$BASE_URL$imageId.jpg"
    }
}