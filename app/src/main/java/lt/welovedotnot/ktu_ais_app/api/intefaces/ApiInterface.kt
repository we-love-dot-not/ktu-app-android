package lt.welovedotnot.ktu_ais_app.api.intefaces

import lt.welovedotnot.ktu_ais_app.api.models.LoginRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by simonas on 4/30/17.
 */
interface ApiInterface {

    @POST("api/login")
    fun loginReq(@Body body: LoginRequest): Call<ResponseBody>
}