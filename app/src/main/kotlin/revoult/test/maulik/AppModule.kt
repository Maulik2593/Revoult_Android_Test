package revoult.test.maulik

import dagger.Module
import dagger.Provides
import revoult.test.maulik.MyRetroApplication

@Module
class AppModule constructor(myRetroApplication: MyRetroApplication){

    var myRetroApplication:MyRetroApplication

    init {
        this.myRetroApplication = myRetroApplication
    }

    @Provides
    fun provideMyRetroApplication():MyRetroApplication{
        return myRetroApplication
    }
}