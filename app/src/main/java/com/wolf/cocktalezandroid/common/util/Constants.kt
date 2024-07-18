package com.wolf.cocktalezandroid.common.util

import androidx.datastore.preferences.core.intPreferencesKey

object Constants {

    const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"

    val THEME_OPTIONS = intPreferencesKey(name = "theme_option")
    const val PAGING_SIZE = 20

    const val DATABASE_NAME = "favorites_database"
    const val TABLE_NAME = "favorites_table"

    const val COCKTALEZ_PREFERENCES = "COCKTALEZ_PREFERENCES"

    const val STARTING_PAGE_INDEX = 0


}