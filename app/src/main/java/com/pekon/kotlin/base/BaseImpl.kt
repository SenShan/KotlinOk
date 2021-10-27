package com.pekon.kotlin.base

//实现此接口的被委托的类
class BaseImpl (var x:Int) :Base{
    override fun print() {
        println(x)
    }
}