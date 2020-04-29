package com.inquallty.sandbox_kotlin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val proc = BehaviorProcessor.create<Long>()

    private var disposable1: Disposable? = null
    private var disposable2: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_main)
        val bind: (Int, (View) -> Unit) -> Unit = { id, listener -> findViewById<View>(id).setOnClickListener(listener) }

        bind(R.id.btn_start) {
            log("Starter", "Start new sequence...")
            Observable.interval(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(30)
                .subscribe(
                    { millis -> proc.offer(millis) },
                    { e -> Log.e("OYAEBU", e.message, e) })
        }
        bind(R.id.subscribe_1) {
            disposable1 = flow()
                .doOnNext { Log.d("OYAEBU", "Subscribe(1) on thread ${Thread.currentThread().name}") }
                .subscribe(
                    { millis -> log("FIRST_SUBSCRIBER", "Recieved: $millis") },
                    { e -> Log.e("OYAEBU", e.message, e) })
        }
        bind(R.id.dispose_1) { disposable1?.dispose() }

        bind(R.id.subscribe_2) {
            disposable2 = flow()
                .doOnNext { Log.d("OYAEBU", "Subscribe(1) on thread ${Thread.currentThread().name}") }
                .subscribe(
                    { millis -> log("SECOND_SUBSCRIBER", "Received: $millis") },
                    { e -> Log.e("OYAEBU", e.message, e) })
        }
        bind(R.id.dispose_2) { disposable2?.dispose() }

        bind(R.id.dispose) {
            disposable1?.dispose()
            disposable2?.dispose()
        }
    }

    private fun flow(): Flowable<Long> = proc.onBackpressureLatest()
        .doOnNext { Log.d("IN_FLOW", "Thread: ${Thread.currentThread().name}") }

    private fun log(tag: String, msg: String) = findViewById<TextView>(R.id.log).append("$tag: $msg\n")
}
