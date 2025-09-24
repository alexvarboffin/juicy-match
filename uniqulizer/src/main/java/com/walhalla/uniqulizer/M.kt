package com.walhalla.uniqulizer

class M {


//Нужно в классе м создать уникализатор у него много умений
//    я указываю тип замены
//
//    1. тип layout
//
//    listOfLayoutsProject

    val project=listOf(
        "C:\\Users\\combo\\Desktop\\juicy-match\\zsdk",

    )


   project + "\src\main\res\layout" - добавить всем xml в папке префикс, или заменить первые части
    например dialog_  accivity_ на массив соответсвий

    dialog_ to dialog_abc_
    accivity_ to match_accivity_

    ВАЖНО! рекурсивно обойти код и исправить замены в коде

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            println("@@@@@@@@@@@@@@")
        }
    }
}
