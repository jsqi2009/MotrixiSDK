package com.motrixi.datacollection.network.event

import com.motrixi.datacollection.network.response.UploadDataResponse
import retrofit2.Response

/**
 * @author jsqi
 * @time 2019/12/4 12:28
 */
class UploadDataResponseEvent: ResponseEvent<UploadDataResponse> {

    constructor(basicResponse: UploadDataResponse, response: Response<*>) : super(basicResponse, response) {}

    constructor(paramRetrofitError: Throwable) : super(paramRetrofitError) {}
}