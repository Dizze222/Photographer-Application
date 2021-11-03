package ch.b.retrofitandcoroutines.utils
/*
class Resource<T>(val status: Status,val data:T,val message: String?) {

    companion object {
        fun<T> success(data: T): Resource<T> = Resource(status = Status.SUCCESS, data = data, message = null)

        fun<T> error(data: T?,message: String) : Resource<T?> = Resource(status = Status.ERROR, data = data, message = message)

        fun<T> loading(data: T?) : Resource<T?> = Resource(status = Status.LOADING, data = data, message = null)
    }


} */


sealed class Resource<T>(val data: T?,val message: String?){
    class Success<T>(data: T) : Resource<T>(data,null)
    class Error<T>(message: String?) : Resource<T>(null,message)
}