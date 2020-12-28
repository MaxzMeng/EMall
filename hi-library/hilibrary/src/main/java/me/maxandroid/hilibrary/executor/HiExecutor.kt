package me.maxandroid.hilibrary.executor

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.IntRange
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

object HiExecutor {
    private const val TAG = "HiExecutor"
    private var isPaused = false
    private var hiExecutor: ThreadPoolExecutor
    private var lock: ReentrantLock = ReentrantLock()
    private var pauseCondition: Condition
    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        pauseCondition = lock.newCondition()

        val cpuCount = Runtime.getRuntime().availableProcessors()
        val corePoolSize = cpuCount + 1
        val maxPoolSize = cpuCount shl 1 + 1
        val blockingQueue: BlockingQueue<Runnable> = PriorityBlockingQueue()
        val keepAliveTime = 30L
        val unit = TimeUnit.SECONDS
        val seq = AtomicLong()
        val threadFactor = ThreadFactory {
            val thread = Thread(it)
            thread.name = "hi-executor-${seq.getAndIncrement()}"
            return@ThreadFactory thread
        }
        hiExecutor = object : ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            keepAliveTime,
            unit,
            blockingQueue,
            threadFactor
        ) {
            override fun beforeExecute(t: Thread?, r: Runnable?) {
                if (isPaused) {
                    lock.lock()
                    try {
                        pauseCondition.await()
                    } finally {
                        lock.unlock()
                    }
                }
            }

            override fun afterExecute(r: Runnable?, t: Throwable?) {
                super.afterExecute(r, t)
                Log.e(TAG, "已执行完的任务的优先级是"+(r as PriorityRunnable).priority)
            }
        }
    }

    @JvmOverloads
    fun execute(@IntRange(from = 0, to = 10) priority: Int = 0, runnable: Callable<*>) {
        hiExecutor.execute(PriorityRunnable(priority, runnable))
    }

    @Synchronized
    fun pause() {
        isPaused = true
        Log.e(TAG, "hiExecutor is paused")
    }

    @Synchronized
    fun resume() {
        isPaused = false
        lock.lock()
        try {
            pauseCondition.signalAll()
        } finally {
            lock.unlock()
        }
        Log.e(TAG, "hiExecutor is resumed")
    }

    abstract class Callable<T> : Runnable {
        override fun run() {
            mainHandler.post {
                onPrepare()
            }

            val t = onBackground()

            mainHandler.post { onCompleted(t) }
        }

        open fun onPrepare() {

        }
        abstract fun onBackground(): T
        abstract fun onCompleted(t: T)
    }

    class PriorityRunnable(val priority: Int, private val runnable: Runnable) : Runnable,
        Comparable<PriorityRunnable> {
        override fun run() {
            runnable.run()
        }

        override fun compareTo(other: PriorityRunnable): Int {
            return when {
                this.priority < other.priority -> {
                    1
                }
                this.priority > other.priority -> {
                    -1
                }
                else -> {
                    0
                }
            }
        }
    }
}