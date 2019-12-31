package revoult.test.maulik.repository

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import revoult.test.maulik.model.RateList

/**
 * Service interface for declaring APIs to call from retrofit
 */
interface APIService {

    /**
     * API to get rate list
     */
    @GET("/latest")
    fun getRateList(@Query("base") base: String): Single<RateList>
}