package revoult.test.maulik.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import revoult.test.maulik.BuildConfig
import revoult.test.maulik.MyRetroApplication
import revoult.test.maulik.di.APIComponent
import revoult.test.maulik.di.APIModule
import revoult.test.maulik.di.DaggerAPIComponent
import revoult.test.maulik.model.Rate
import revoult.test.maulik.model.RateList
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Repository to get data from api calls
 */
class RateRepository {
    lateinit var apiComponent: APIComponent
    var rateListMutableList: MutableLiveData<List<Rate>> = MutableLiveData()
    private var viewStopped = false
    var baseCurrencySymbol = "EUR"
    private var isLoaded = false
    lateinit var callBack: Disposable
    @Inject
    lateinit var retrofit: Retrofit

    init {
        apiComponent = DaggerAPIComponent
                .builder()
                .aPIModule(APIModule(APIURL.BASE_URL))
                .build()
        apiComponent.inject(this)

        var apiComponent: APIComponent = MyRetroApplication.apiComponent
        apiComponent.inject(this)
    }

    /**
     * Call rate list data api
     * @param baseCurrency : base currency for setting it to rate list api
     *
     */
    fun fetchRateInfoList(baseCurrency: String): LiveData<List<Rate>> {
        var apiService: APIService = retrofit.create(APIService::class.java)
        callBack = apiService.getRateList(baseCurrencySymbol)
                .doOnSubscribe {
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val rates = ArrayList<Rate>()
                    rates.add(Rate(it.base, 1.0F))
                    rates.addAll(it.rates.map { Rate(it.key, it.value) })
                    isLoaded = true
                    rateListMutableList.value = rates
                }, {

                })
        return rateListMutableList
    }
}