package com.pekon.kotlin

import org.junit.Test

import org.junit.Assert.*


class ExampleUnitTest {
    @Test
    fun test() {
        val book=Book("Sen")
        book.print()
        book.changeVar(1,2,3)
        println(book.age)
        book.show()


        val kuo=Kuo()
        kuo.Kuo()
    }
}

class Box<T>(t: T) {
    var value = t
}

enum class Color{
    RED,BLACK,BLUE,GREEN,WHITE
}


open class Base{
    open fun show(){
        println("a")
    }
}
class Kuo{

    fun Kuo(){
        println("成员函数")
    }
}
fun Kuo.Kuo(){
    println("扩展函数 扩展函数和成员函数一致，则使用该函数时，会优先使用成员函数")
}
class Book constructor(private val name:String):Base(),MyInterface{
    override fun show() {
        super.show()
        println("继承")
    }

    val age:Int=12
    init{
        println("init")
    }
    fun print(): Unit{
        println(name)
    }

    fun changeVar(vararg l: Int){
        for(ll in l){
            println(ll)
        }
    }

    //实现接口方法  可以选择实现
    override fun bar() {
        TODO("Not yet implemented")
    }

    override fun foo() {
        super.foo()
    }


    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }
}

interface MyInterface{
    fun bar()
    fun foo(){

    }
}
