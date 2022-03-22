package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*

class CatViewModel @AssistedInject constructor(val service: CatsService): ViewModel() {
    private val TAG: String = "ViewModel";
    private val _catData: MutableLiveData<Result<Cat>> = MutableLiveData<Result<Cat>>();
    val catData: LiveData<Result<Cat>> = _catData
    private var dataJob: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler{
            context, exception -> println("coroutine exception ${CrashMonitor.trackWarning(exception)}")
    }
    init {
        Log.i("ViewModel", "viewModel init")
    }
    fun loadData() {
        viewModelScope.launch {
            _catData.value = Result.Loading
            if(dataJob?.isActive == true) return@launch
            dataJob = launch(exceptionHandler) {
                coroutineScope {
                    delay(10000);
                    val fact     = async { service.getCatFact() }
                    val imageUrl = async { service.getImageResource() }
                    try {
                        _catData.value =  Result.Success(Cat(fact.await().text, imageUrl.await().fileUrl))
                    } catch (ex: Exception) {
                        if(ex is CancellationException) {
                            Log.i(TAG, "Job was cancelled")
                        } else {
                            _catData.value = Result.Error(ex)
                        }
                    } finally {
                        withContext(NonCancellable) {
                            delay(1000);
                            Log.i(TAG, "cleaning process")
                        }
                    }
                }
            }
        }
    }

    fun cancelLoad() {
        dataJob?.cancel()
    }

    @AssistedFactory
    interface Factory {
        fun create(): CatViewModel
    }
}