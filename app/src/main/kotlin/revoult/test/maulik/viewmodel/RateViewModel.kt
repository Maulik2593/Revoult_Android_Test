package revoult.test.maulik.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import revoult.test.maulik.model.Rate
import revoult.test.maulik.repository.RateRepository

/**
 * Viewmodel for rate data
 */
class RateViewModel(rateRepository: RateRepository) : ViewModel() {

    lateinit var rateRepository: RateRepository
    var baseCurrency: String = "EUR"
    var rateListLiveData: LiveData<List<Rate>> = MutableLiveData()

    init {
        this.rateRepository = rateRepository
        fetchRateInfoFromRepository()
    }

    /**
     * Change base currency to call in API
     */
    fun changeBaseCurrency(baseCurrencySymbol:String)
    {
        rateRepository.baseCurrencySymbol=baseCurrencySymbol
        rateRepository.callBack.dispose()
        rateRepository.fetchRateInfoList(baseCurrency)
    }

    /**
     * Call Rate list API
     */
    fun fetchRateInfoFromRepository() {
        rateListLiveData = rateRepository.fetchRateInfoList(baseCurrency)
    }


}