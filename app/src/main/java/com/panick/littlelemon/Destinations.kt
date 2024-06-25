package com.panick.littlelemon

interface Destinations {
    val route: String
}

object HomeDestination : Destinations {
    override val route: String = "Home"
}

object ProfileDestination : Destinations {
    override val route: String = "Profile"
}


object OnboardingDestination : Destinations {
    override val route: String = "Onboarding"
}