package revoult.test.maulik.di

import dagger.Component
import revoult.test.maulik.AppModule
import revoult.test.maulik.view.RateFragment
import revoult.test.maulik.repository.RateRepository
import revoult.test.maulik.viewmodel.RateViewModel
import revoult.test.maulik.viewmodel.RateViewModelFactory
import javax.inject.Singleton

/**
 * Interface to inject all necessary component to use in application
 */
@Singleton
@Component(modules = [AppModule::class, APIModule::class])
interface APIComponent {
    fun inject(rateRepository: RateRepository)
    fun inject(rateViewModel: RateViewModel)
    fun inject(rateFragment: RateFragment)
    fun inject(rateViewModelFactory: RateViewModelFactory)
}