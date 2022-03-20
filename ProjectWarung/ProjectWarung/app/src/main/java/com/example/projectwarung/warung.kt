package com.example.projectwarung

data class Warung(
    val id : String,
    val barang : String,
    val harga : String
){
    constructor(): this("", "", "")
}