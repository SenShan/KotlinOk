package com.pekon.kotlin

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val disposable = Observable.create(ObservableOnSubscribe<String> { emitter ->
            emitter.onNext("2")
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(Consumer { t ->
            Log.e("tt=", t)

        })
    }
    @Test
    fun ap(){
        val disposable = Observable.create(ObservableOnSubscribe<String> { emitter ->
            emitter.onNext("2")
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(Consumer { t ->
            println(t)
            Log.e("tt=", t)
            System.out.println(t)

        })
    }
}