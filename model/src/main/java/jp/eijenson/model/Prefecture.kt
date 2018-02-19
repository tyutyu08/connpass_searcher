package jp.eijenson.model

/**
 * Created by kobayashimakoto on 2018/02/10.
 */
enum class Prefecture(val prefectureName: String, val prefix: String) {
    HOKKAIDO("北海道", ""),
    AOMORI("青森", "県"),
    IWATE("岩手", "県"),
    MIYAGI("宮城", "県"),
    AKITA("秋田", "県"),
    YAMAGATA("山形", "県"),
    FUKUSHIMA("福島", "県"),
    IBARAKI("茨城", "県"),
    TOCHIGI("栃木", "県"),
    GUNMA("群馬", "県"),
    SAITAMA("埼玉", "県"),
    CHIBA("千葉", "県"),
    TOKYO("東京", "都"),
    KANAGAWA("神奈川", "県"),
    NIIGATA("新潟", "県"),
    TOYAMA("富山", "県"),
    ISHIKAWA("石川", "県"),
    FUKUI("福井", "県"),
    YAMANASHI("山梨", "県"),
    NAGANO("長野", "県"),
    GIFU("岐阜", "県"),
    SHIZUOKA("静岡", "県"),
    AICHI("愛知", "県"),
    MIE("三重", "県"),
    SHIGA("滋賀", "県"),
    KYOTO("京都", "府"),
    OSAKA("大阪", "府"),
    HYOGO("兵庫", "県"),
    NARA("奈良", "県"),
    WAKAYAMA("和歌山", "県"),
    TOTTORI("鳥取", "県"),
    SHIMANE("島根", "県"),
    OKAYAMA("岡山", "県"),
    HIROSHIMA("広島", "県"),
    YAMAGUCHI("山口", "県"),
    TOKUSHIMA("徳島", "県"),
    KAGAWA("香川", "県"),
    EHIME("愛媛", "県"),
    KOCHI("高知", "県"),
    FUKUOKA("福岡", "県"),
    SAGA("佐賀", "県"),
    NAGASAKI("長崎", "県"),
    KUMAMOTO("熊本", "県"),
    OITA("大分", "県"),
    MIYAZAKI("宮崎", "県"),
    KAGOSHIMA("鹿児島", "県"),
    OKINAWA("沖縄", "県"),
    UNDEFINED("", "");

    companion object {
        fun getPreference(name: String): Prefecture {
            Prefecture.values().map {
                if (name.indexOf(it.prefectureName) != -1) return it
            }
            return Prefecture.UNDEFINED
        }
    }
}
