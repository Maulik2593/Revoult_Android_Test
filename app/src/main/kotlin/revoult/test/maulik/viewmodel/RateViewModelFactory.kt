package revoult.test.maulik.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import revoult.test.maulik.MyRetroApplication
import revoult.test.maulik.di.APIModule
import revoult.test.maulik.di.APIComponent
import revoult.test.maulik.di.DaggerAPIComponent
import revoult.test.maulik.repository.APIURL
import revoult.test.maulik.repository.RateRepository
import javax.inject.Inject

class RateViewModelFactory : ViewModelProvider.Factory {
    lateinit var apiComponent: APIComponent
    @Inject
    lateinit var rateRepository: RateRepository
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//       initDaggerComponent()
       var apiComponent :APIComponent =  MyRetroApplication.apiComponent
        apiComponent.inject(this)
        if (modelClass.isAssignableFrom(RateViewModel::class.java)) {
            return RateViewModel(rateRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    /**
     * Init dagger components
     */
    /*fun initDaggerComponent(){
        apiComponent =   DaggerAPIComponent
            .builder()
            .aPIModule(APIModule(APIURL.BASE_URL))
            .build()
        apiComponent.inject(this)
    }*/
}