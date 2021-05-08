package matt.hurricanefx.tornadofx.async

/*slightly modified code I stole from tornadofx*/

import javafx.application.Platform
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicLong

private enum class ThreadPoolType { NoDaemon, Daemon }

private val threadPools = mutableMapOf<ThreadPoolType, ExecutorService>()

internal val tfxThreadPool: ExecutorService
    get() = threadPools.getOrPut(ThreadPoolType.NoDaemon) {
        Executors.newCachedThreadPool(TFXThreadFactory(daemon = false))
    }

internal val tfxDaemonThreadPool: ExecutorService
    get() = threadPools.getOrPut(ThreadPoolType.Daemon) {
        Executors.newCachedThreadPool(TFXThreadFactory(daemon = true))
    }


internal fun shutdownThreadPools() {
    threadPools.values.forEach { it.shutdown() }
    threadPools.clear()
}

private class TFXThreadFactory(val daemon: Boolean) : ThreadFactory {
    private val threadCounter = AtomicLong(0L)
    override fun newThread(runnable: Runnable?) = Thread(runnable, threadName()).apply {
        isDaemon = daemon
    }

    private fun threadName() = "tornadofx-thread-${threadCounter.incrementAndGet()}" + if (daemon) "-daemon" else ""
}


/**
 * Run the specified Runnable on the JavaFX Application Thread at some
 * unspecified time in the future.
 */
fun runLater(op: () -> Unit) = Platform.runLater(op)

private val runLaterTimer: Timer by lazy { Timer(true) }