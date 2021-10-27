package com.pekon.kotlin

import org.junit.Test

//数据类
class DataClass {
    @Test
    fun create() {
        val jack=User(name = "Jack",age = 12)
        val jack1=jack.copy(age = 2)
        println(jack)
        println(jack1)

    }
}
data class User(val name: String,val age: Int)