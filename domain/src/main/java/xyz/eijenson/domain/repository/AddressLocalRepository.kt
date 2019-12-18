package xyz.eijenson.domain.repository

interface AddressLocalRepository {
    fun getAddress(latitube: Double, longitube: Double): String
}