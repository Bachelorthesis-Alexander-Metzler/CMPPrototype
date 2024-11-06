package de.doubleslash.cmpprototype.domain.model.auth

// A sealed class to represent the different states of a request.
// It is generic and takes a type parameter T, which represents the type of data returned on a successful request.
sealed class RequestCondition<out T> {

    // Represents the "idle" state, indicating no request is in progress.
    data object IdleCondition : RequestCondition<Nothing>()

    // Represents the "loading" state, indicating that a request is currently in progress.
    data object LoadingCondition : RequestCondition<Nothing>()

    // Represents the "success" state, indicating the request was successful.
    // It holds data of type `T` as part of this state.
    data class SuccessCondition<out T>(val data: T) : RequestCondition<T>()

    // Represents the "error" state, indicating the request failed.
    // It holds an error message as a String to describe the failure.
    data class ErrorCondition(val errorMsg: String) : RequestCondition<Nothing>()

    // Helper methods to check if the current state
    fun isLoading(): Boolean = this is LoadingCondition
    fun isError(): Boolean = this is ErrorCondition
    fun isSuccess(): Boolean = this is SuccessCondition

    // Gets the data from the `SuccessCondition` state.
    // This method should be called only if the current state is `SuccessCondition`.
    fun getSuccessInfo() = (this as SuccessCondition).data

    // Gets the error message from the `ErrorCondition` state.
    // This method should be called only if the current state is `ErrorCondition`.
    fun getErrorMessage(): String = (this as ErrorCondition).errorMsg
}
