package com.freemimp.main.utils

// Needed to fix issue described here https://github.com/mockk/mockk/issues/485
@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "UNCHECKED_CAST")
fun <T> Result<T>.safeGetOrThrow(): T {
    val unboxed: Any? = value
    when (unboxed) {
        is Result.Failure -> throw unboxed.exception
        !is Result<*> -> return unboxed as T
    }
    return (unboxed as Result<T>).getOrThrow()
}
