package xyz.eijenson.infra.repository.db

import android.content.Context
import android.location.Address
import android.location.Geocoder
import xyz.eijenson.domain.repository.AddressLocalRepository
import timber.log.Timber
import java.io.IOException
import java.util.Locale

/**
 * Created by kobayashimakoto on 2018/04/06.
 */
class AddressGeoCoderRepository(context: Context) : AddressLocalRepository {
    private val geocoder = Geocoder(context, Locale.JAPAN)

    override fun getAddress(latitube: Double, longitube: Double): String {
        return try {
            val addresses: List<Address> = geocoder.getFromLocation(latitube, longitube, 1)
            addresses.firstOrNull()?.adminArea ?: ""
        } catch (e: IOException) {
            Timber.d(e)
            ""
        }
    }
}