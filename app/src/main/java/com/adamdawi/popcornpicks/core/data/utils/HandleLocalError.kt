package com.adamdawi.popcornpicks.core.data.utils

import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import timber.log.Timber

fun <T> handleLocalError(e: Exception): Result<T, DataError.Local> {
    Timber.e(e)
    return when (e) {
        is java.io.IOException -> Result.Error(DataError.Local.DISK_FULL)
        else -> Result.Error(DataError.Local.UNKNOWN)
    }
}