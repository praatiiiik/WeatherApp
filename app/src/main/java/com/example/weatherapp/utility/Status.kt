package com.example.weatherapp.utility

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

        /**
         * Returns [Status.Loading] instance.
         */
        fun <T> loading() = Loading<T>()

        /**
         * Returns [Status.Success] instance.
         * @param data Data to emit with status.
         */
        private fun <T> success(data: T) =
            Success(data)

        /**
         * Returns [Status.Error] instance.
         * @param message Description of failure.
         */
        private fun <T> error(message: String) =
            Error<T>(message)

        /**
         * Returns [Status.Empty] instance.
         */
        fun <T> empty() = Empty<T>()

        /**
         * Returns [Status] from [Resource]
         */
        fun <T> fromResource(resource: Resource<T>): Status<T> = when (resource) {
            is Resource.Success -> success(resource.data)
            is Resource.Failed -> error(resource.msg)
        }
    }
}