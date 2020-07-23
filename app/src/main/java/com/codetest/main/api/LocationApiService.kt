package com.codetest.main.api

import com.codetest.main.KeyUtil
import com.codetest.main.model.LocationDto
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.Result
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface LocationApi {
    @GET
    fun get(@Header("X-Api-Key") apiKey: String, @Url url: String): Observable<JsonObject>

    @POST("locations")
    fun postLocation(
        @Header("X-Api-Key") apiKey: String,
        @Body location: LocationDto
    ): Single<Result<Void>>
}

class LocationApiService {
    private val api: LocationApi

    companion object {
        private val instance = LocationApiService()
        fun getApi(): LocationApiService =
            instance
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://app-code-test.kry.pet/")
                .client(OkHttpClient().newBuilder().build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()

        api = retrofit.create(LocationApi::class.java)
    }

    fun get(url: String, success: (JsonObject) -> Unit, error: (String?) -> Unit) {
        api.get(KeyUtil.getKey(), url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    success(it)
                },
                onError = {
                    error(it.message)
                }
            )
    }

    fun postLocation(
        location: LocationDto,
        success: () -> Unit,
        error: (Throwable) -> Unit
    ) {
        api.postLocation(KeyUtil.getKey(), location)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { success() },
                onError = ::error
            )
    }
}