package lt.hacker_house.ktu_ais.api

import lt.hacker_house.ktu_ais.models.*
import okhttp3.ResponseBody
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
                .baseUrl(ApiConf.HTTPS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

    private val service: ApiInterface
        get() = retrofit.create(ApiInterface::class.java)

    fun login(body: LoginRequest, callback: (UserModel?)->(Unit)) {
        service.loginReq(body).enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>?, response: Response<UserModel>?) {
                if (response!!.isSuccessful) {
                    response.body().username = body.username
                    response.body().password = body.password
                    callback.invoke(response.body())
                } else {
                    callback.invoke(null)
                }
            }

            override fun onFailure(call: Call<UserModel>?, t: Throwable?) {
                callback.invoke(null)
            }
        })
    }

    fun modules(body: ModulesRequest, cookie: String, callback: (ResponseBody?)->(Unit)) {
        service.modulesReq(body, cookie).enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>?) {
                if (response!!.isSuccessful) {
                    callback.invoke(response.body())
                } else {
                    callback.invoke(null)
                }
            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                callback.invoke(null)
            }
        })
    }

    fun grades(body: ModulesRequest, cookie: String, callback: (List<GetGradesResponse>?)->(Unit)) {
        service.gradesReq(body, cookie).enqueue(object : Callback<List<GetGradesResponse>?> {
            override fun onResponse(call: Call<List<GetGradesResponse>?>?, response: Response<List<GetGradesResponse>?>?) {
                if (response!!.isSuccessful) {
                    val gradesList = response.body()!!
                    gradesList.forEach {
                        it.mark.forEachIndexed { index, string ->
                            if (string == "") {
                                it.mark[index] = GradeModel.EMPTY_MARK
                            }
                        }
                        it.rlMark = it.mark.joinToString(";")
                    }
                    callback.invoke(gradesList)
                } else {
                    callback.invoke(null)
                }
            }

            override fun onFailure(call: Call<List<GetGradesResponse>?>?, t: Throwable?) {
                callback.invoke(null)
            }
        })
    }
}