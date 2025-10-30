package com.on.weather.data

data class CountryData(
    val capitalInfo: CapitalInfo?,
    val name: Name,
    val cca2: String,
    val capital: List<String>?
)

data class CapitalInfo(
    val latlng: List<Double>?
)

data class Name(
    val common: String,
    val official: String,
    val nativeName: Map<String, NativeLanguage>?
)

data class NativeLanguage(
    val official: String,
    val common: String
)
