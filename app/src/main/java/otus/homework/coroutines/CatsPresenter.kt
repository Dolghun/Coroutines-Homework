package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var job: Job? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception = exception)
    }

    private val presenterScope = CoroutineScope(CoroutineName("CatsCoroutine") + Dispatchers.Main)
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            if(job?.isActive == true) return@launch
            job = launch(handler) {
                _catsView?.showLoading()
                try {
                    val catFact = catsService.getCatFact()
                    val imageResource = catsService.getImageResource()
                    _catsView?.populate(catFact, imageResource)
                    _catsView?.hideLoading()
                } catch (ex: Exception) {
                    _catsView?.hideLoading()
                    when (ex) {
                        is SocketTimeoutException -> {
                            _catsView?.showToast("Не удалось получить ответ от сервера")
                        }
                        else -> {
                            CrashMonitor.trackWarning(ex)
                        }
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun onStopCoroutine() {
        job?.cancel()
        _catsView?.hideLoading()
    }
}