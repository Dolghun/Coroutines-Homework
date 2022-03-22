package otus.homework.coroutines

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(exception: Throwable) {
        println("${exception.message}")
    }
}