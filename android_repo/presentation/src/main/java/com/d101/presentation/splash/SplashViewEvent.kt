package com.d101.presentation.splash

sealed class SplashViewEvent {

    data object ShowSplash : SplashViewEvent()
    data object AutoSignInSuccess : SplashViewEvent()
    data object AutoSignInFailure : SplashViewEvent()
    data class CheckAppStatus(
        val minVersion: String,
        val storeUrl: String,
    ) : SplashViewEvent()
    data class OnServerMaintaining(val message: String) : SplashViewEvent()
    data class SetBackGroundMusic(val musicName: String) : SplashViewEvent()

    data class OnShowToast(val message: String) : SplashViewEvent()
}
