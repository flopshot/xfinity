package com.xfinity.api

data class ApiResource<out T>(val status: Status, val data: T?, val message: String?): Any() {
    companion object {
        fun <T> success(data: T?): ApiResource<T> {
            return ApiResource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): ApiResource<T> {
            return ApiResource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): ApiResource<T> {
            return ApiResource(Status.LOADING, data, null)
        }
    }
}