package jp.eijenson.connpass_searcher.repository.db

import android.content.Context
import android.location.Address
import android.location.Geocoder
import timber.log.Timber
import java.io.IOException
import java.util.*

/**
 * Created by kobayashimakoto on 2018/04/06.
 */
class AddressLocalRepository(context: Context) {
    private val geocoder = Geocoder(context, Locale.JAPAN)

    fun getAddress(latitube: Double, longitube: Double): String {
        return try {
            val addresses: List<Address> = geocoder.getFromLocation(latitube, longitube, 1)
            addresses.firstOrNull()?.adminArea ?: ""
        } catch (e: IOException) {
            Timber.d(e)
            ""
        }
    }
}