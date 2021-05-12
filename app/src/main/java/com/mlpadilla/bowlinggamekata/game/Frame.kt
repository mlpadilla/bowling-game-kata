package com.mlpadilla.bowlinggamekata.game

data class Frame (
    val roll1: Roll = object : Roll {},
    val roll2: Roll = object : Roll {}
)

interface Roll