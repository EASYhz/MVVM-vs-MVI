package com.easyhz.mvvm_vs_mvi.network

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type

class ResultCallAdapter(
    private val responseType: Type
) : CallAdapter<Type, Call<Result<Type>>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<Type>): Call<Result<Type>> = ResultCall(call)
}


private class ResultCall<T>(
    private val delegate: Call<T>,
) : Call<Result<T>> {

    override fun enqueue(callback: Callback<Result<T>>) {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(this@ResultCall, Response.success(response.toResult()))
            }

            private fun Response<T>.toResult(): Result<T> {
                val body = body()
                val errorBody = errorBody()?.string()

                if (isSuccessful) {
                    return if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(NullPointerException())
                    }
                }

                if (errorBody == null) {
                    return Result.failure(NullPointerException())
                }

                return Result.failure(NullPointerException())
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(
                    this@ResultCall,
                    Response.success(Result.failure(t))
                )
            }
        })
    }


    override fun clone(): Call<Result<T>> = ResultCall(delegate.clone())

    override fun execute(): Response<Result<T>> = throw UnsupportedOperationException()

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}