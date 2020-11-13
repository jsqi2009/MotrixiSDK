package com.motrixi.datacollection.network


import com.motrixi.datacollection.network.response.BResponse

import retrofit2.Response

/**
 * Created by liming on 17/4/18.
 */
interface DispatchCallback<T : BResponse> {

    fun onDispatchError(paramT: T, paramResponse: Response<*>)

    fun onDispatchNetworkError(paramRetrofitError: Throwable)

    fun onDispatchSuccess(paramT: T, paramResponse: Response<*>)
}