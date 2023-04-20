package com.lit.remindme

import android.icu.util.Calendar
import android.util.Log

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.time.LocalDateTime
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleInstrumentedTest {
}

internal class Foo {
    private val str = "hello"

    inline fun hello(extFun: (String) -> Unit) {
        extFun(str)
    }
}