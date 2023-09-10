package github.alexzhirkevich.studentbsuby.di

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import github.alexzhirkevich.studentbsuby.BuildConfig
import github.alexzhirkevich.studentbsuby.api.*
import github.alexzhirkevich.studentbsuby.util.LoginCookieManager
import github.alexzhirkevich.studentbsuby.util.PreferencesCookieCache
import io.harkema.retrofitcurlprinter.Logger
import io.harkema.retrofitcurlprinter.RetrofitCurlPrinterInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule
{
    private lateinit var loginCookieManager: LoginCookieManager

    @Provides
    @Singleton
    fun provideBaseUrl(): Uri = Uri.parse("https://student.bsu.by")

    @Provides
    @Singleton
    fun provideHttpClient(
        @ApplicationContext context: Context, @CookiesPrefsQualifier preferences: SharedPreferences
                         ): OkHttpClient
    {
        val cache = PreferencesCookieCache(preferences)
        val cookieJar = PersistentCookieJar(cache, SharedPrefsCookiePersistor(context))
        loginCookieManager = cache

        return OkHttpClient.Builder().cookieJar(cookieJar).followRedirects(false)
            .followSslRedirects(false).retryOnConnectionFailure(false)
            .connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).addInterceptor {
                val cookies = cookieJar.loadForRequest(it.request().url)
                val sCookies = cookies.joinToString(separator = "; ") { it.name + '=' + it.value }
                val newReq = it.request().newBuilder().let {
                        if (sCookies.isNotEmpty()) it.addHeader("Cookie", sCookies) else it
                    }.build()
                it.proceed(newReq)
            }.addNetworkInterceptor(Interceptor {
                val request = it.request().newBuilder().addHeader("Connection", "close").build()
                it.proceed(request)
            }).let {
                if (BuildConfig.DEBUG)
                {
                    it.addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger
                    {
                        override fun log(message: String)
                        {
                            Log.e("log", message)
                        }

                    }).apply { level = HttpLoggingInterceptor.Level.BODY })
                        .addInterceptor(RetrofitCurlPrinterInterceptor(object : Logger
                        {
                            override fun log(message: String)
                            {
                                Log.e("CURL", message)
                            }
                        }))
                } else it
            }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient, baseUri: Uri): Retrofit
    {
        return Retrofit.Builder().baseUrl(baseUri.toString()).client(httpClient).build()
    }

    @Provides
    @Singleton
    fun provideCookieCleaner() = loginCookieManager

    @Provides
    @Singleton
    fun provideLoginApi(retrofit: Retrofit): LoginApi
    {
        return LoginApiWrapper(retrofit.create(LoginApi::class.java))
    }

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi
    {
        return retrofit.create(ProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTimetableApi(retrofit: Retrofit): TimetableApi
    {
        return TimetableApiWrapper(retrofit.create(TimetableApi::class.java))
    }

    @Provides
    @Singleton
    fun providePaidServicesApi(retrofit: Retrofit): PaidServicesApi
    {
        return retrofit.create(PaidServicesApi::class.java)
    }
}