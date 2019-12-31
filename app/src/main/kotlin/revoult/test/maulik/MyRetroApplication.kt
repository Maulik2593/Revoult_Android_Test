package revoult.test.maulik

import android.app.Application
import android.content.Context

import revoult.test.maulik.di.APIModule
import revoult.test.maulik.di.DaggerAPIComponent
import revoult.test.maulik.di.APIComponent
import revoult.test.maulik.repository.APIURL


/**
 * Application class where we are declaring all necessary components including dagger2 dependancies
 */
class MyRetroApplication : Application() {

    companion object {
        var ctx: Context? = null
        lateinit var apiComponent: APIComponent
    }
    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
        apiComponent = initDaggerComponent()
    }

    fun getMyComponent(): APIComponent {
        return apiComponent
    }

    /**
     * Init dagger components
     */
    fun initDaggerComponent():APIComponent{
        apiComponent =   DaggerAPIComponent
            .builder()
            .aPIModule(APIModule(APIURL.BASE_URL))
            .build()
        return  apiComponent

    }
}