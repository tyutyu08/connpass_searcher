package jp.eijenson.connpass_searcher.infra.repository.api.entity.response

import com.google.gson.annotations.SerializedName

data class EventJson(
    @SerializedName("event_id") val eventId: Long?,
    val title: String?,
    @SerializedName("catch") val catchPhrase: String?,
    val description: String?,
    @SerializedName("event_url") val eventUrl: String?,
    @SerializedName("hash_tag") val hashTag: String?,
    @SerializedName("started_at") val startedAt: String?,
    @SerializedName("ended_at") val endedAt: String?,
    val limit: Int?,
    @SerializedName("event_type") val eventType: EventType?,
    val series: SeriesJson?,
    val address: String?,
    val place: String?,
    val lat: Double?,
    val lon: Double?,
    @SerializedName("owner_id") val ownerId: Int?,
    @SerializedName("owner_nickname") val ownerNickname: String?,
    @SerializedName("owner_display_name") val ownerDisplayName: String?,
    val accepted: Int?,
    val waiting: Int?,
    @SerializedName("updated_at") val updatedAt: String?
) {
    enum class EventType {
        PARTICIPATION, ADVERTISEMENT
    }
}