package ch.b.retrofitandcoroutines.data.authorization.net.authorization

import ch.b.retrofitandcoroutines.data.authorization.net.update_token.UpdateTokenService
import ch.b.retrofitandcoroutines.data.core.TokenInterceptor
import ch.b.retrofitandcoroutines.data.core.authorization.cache.TokenToSharedPreferences
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.Authenticator
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class Authenticator(private val tokenFromShared: TokenToSharedPreferences) :
    Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        val okHttp = OkHttpClient.Builder()
            .connectTimeout(5,TimeUnit.MINUTES)
            .writeTimeout(5,TimeUnit.MINUTES)
            .readTimeout(5,TimeUnit.MINUTES)
            .addInterceptor(TokenInterceptor.RefreshToken(tokenFromShared))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://7094-84-39-247-127.ngrok.io/")
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(UpdateTokenService::class.java)
        if (!isRequestWithAccessToken(response)) {
            return@runBlocking null
        }
        val newResult: String = api.updateToken().accessToken()

        synchronized(this) {
            if (tokenFromShared.readAccessToken() != newResult) {
                return@runBlocking newRequestWithAccessToken(response.request, newResult)
            }
            val updatedAccessToken = tokenFromShared.readAccessToken()
            return@synchronized newRequestWithAccessToken(response.request, updatedAccessToken)
        }
    }


    private fun isRequestWithAccessToken(response: Response): Boolean {
        val header = response.request.header("Authorization")
        return header != null && header.startsWith("Bearer")
    }

    private fun newRequestWithAccessToken(request: Request, accessToken: String?): Request {
        return request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
    }
}



