package com.adamdawi.popcornpicks.core.presentation.ui

import com.adamdawi.popcornpicks.core.domain.util.DataError

fun DataError.asUiText(): String {
    return when(this) {
        DataError.Local.DISK_FULL -> "Failed to save the item because your disk is full."
        DataError.Network.REQUEST_TIMEOUT -> "The request timed out."
        DataError.Network.NO_INTERNET -> "Couldn't reach server, please check your connection."
        DataError.Network.SERVER_ERROR -> "Oops, something went wrong, please try again."
        DataError.Network.SERIALIZATION -> "Oops, couldn't parse data."
        else -> "An unknown error occurred."
    }
}