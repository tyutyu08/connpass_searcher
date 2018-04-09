package jp.eijenson.connpass_searcher.repository.local

import android.content.Context
import android.location.Geocoder
import timber.log.Timber
import java.io.IOException
import java.util.*

/**
 * Created by kobayashimakoto on 2018/04/06.
 */
class AddressLocalRepository(context: Context) {
    val geocoder = Geocoder(context, Locale.getDefault())

    fun getAddress(latitube: Double, longitube: Double): String {
        try {
            val addresses = geocoder.getFromLocation(latitube, longitube, 1)
            for (address in addresses) {
                return address.adminArea
            }
            return ""
        } catch (e: IOException) {
            Timber.d(e)
            return ""
        }
    }
}