package jp.eijenson.connpass_searcher.domain.repository

interface AddressLocalRepository {
    fun getAddress(latitube: Double, longitube: Double): String
}