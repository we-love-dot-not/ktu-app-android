package lt.hacker_house.ktu_ais.api

import lt.hacker_house.ktu_ais.models.GetGradesResponse
import lt.hacker_house.ktu_ais.models.LoginRequest
import lt.hacker_house.ktu_ais.models.UserModel
import lt.hacker_house.ktu_ais.models.ModulesRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by simonas on 4/30/17.
 */
interface ApiInterface {

    @POST("api/login")
    fun loginReq(@Body body: LoginRequest): Call<UserModel>

    @POST("api/get_modules")
    fun modulesReq(@Body body: ModulesRequest, @Header("cookie") cookie: String): Call<ResponseBody>

    @POST("api/get_grades")
    fun gradesReq(@Body body: ModulesRequest, @Header("cookie") cookie: String): Call<List<GetGradesResponse>>

}