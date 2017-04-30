package lt.welovedotnot.ktu_ais_app.api

import lt.welovedotnot.ktu_ais_app.api.intefaces.ApiInterface
import lt.welovedotnot.ktu_ais_app.api.models.LoginRequest
import lt.welovedotnot.ktu_ais_app.api.models.LoginResponse
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by simonas on 4/30/17.
 */
object Api {
    private val TAG = "Api"
    private val retrofit: Retrofit
        get() =
        Retrofit.Builder()
                .baseUrl(ApiConf.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

    private val service: ApiInterface
        get() = retrofit.create(ApiInterface::class.java)

    fun login(body: LoginRequest, callback: (LoginResponse?)->(Unit)) {
        service.loginReq(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                response?.also {
                    if (it.isSuccessful) {
                        val respString = response.body().string()
                        val jsonArr = JSONArray(respString)
                        val loginResponse = LoginResponse(jsonArr)
                        callback.invoke(loginResponse)
                    } else {
                        callback.invoke(null)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                callback.invoke(null)
            }
        })
    }
}