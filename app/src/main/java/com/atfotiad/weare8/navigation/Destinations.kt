package com.atfotiad.weare8.navigation

interface AppDestination {
    val route: String
}

object FeedListScreen: AppDestination {
    override val route: String = "feed"
}