package cn.mealkey.mkservice.business.init

import dagger.Module
import dagger.Provides

@Module
class LoginModule {

    @Provides
    fun provideView(activity: LoginActivity):LoginContract.View = activity

    @Provides
    fun providePresenter(presenter: LoginPresenter):LoginContract.Presenter = presenter
}
