package com.codewithhiren.ekart.utils

sealed class NetworkResponse<T> {

    data class Success<T> (val data : T ) : NetworkResponse<T>()
    data class Error<T>(val error:String) : NetworkResponse<T>()
    class Loading<T> : NetworkResponse<T>()
}