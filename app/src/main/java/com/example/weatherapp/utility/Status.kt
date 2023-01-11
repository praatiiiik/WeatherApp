package com.example.weatherapp.utility


//sealed class for status of data
sealed class Status<T> {

    class Loading<T> : Status<T>()

    data class Success<T>(val data: T) : Status<T>()

    data class Error<T>(val message: String) : Status<T>()

    class Empty<T> : Status<T>()

    fun isLoading(): Boolean = this is Loading

    fun isSuccessful(): Boolean = this is Success

    fun isFailed(): Boolean = this is Error

    fun isEmpty(): Boolean = this is Empty

    companion object {

        fun <T> loading() = Loading<T>()

        private fun <T> success(data: T) =
            Success(data)

        private fun <T> error(message: String) =
            Error<T>(message)

        fun <T> empty() = Empty<T>()

        fun <T> fromResource(resource: Resource<T>): Status<T> = when (resource) {
            is Resource.Success -> success(resource.data)
            is Resource.Failed -> error(resource.msg)
        }
    }
}