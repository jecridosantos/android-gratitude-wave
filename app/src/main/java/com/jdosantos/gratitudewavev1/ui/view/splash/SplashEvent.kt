package com.jdosantos.gratitudewavev1.ui.view.splash

sealed class SplashEvent {
    object CheckAuthentication : SplashEvent()
    // Puedes agregar más eventos si es necesario
}