package com.harmonievent.network

sealed class NetworkState <out T> {
    class OnSuccess<T>(var data: T) : NetworkState<T>()
    class OnError(var status: Boolean, var message: String) : NetworkState<Nothing>()
    class OnFailure(var error: Throwable) : NetworkState<Nothing>()
}