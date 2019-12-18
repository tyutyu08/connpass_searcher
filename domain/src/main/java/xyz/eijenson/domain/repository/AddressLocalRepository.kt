package xyz.eijenson.domain.repository

interface AddressLocalRepository {
    fun getAddress(latitude: Double, longitude: Double): String
}