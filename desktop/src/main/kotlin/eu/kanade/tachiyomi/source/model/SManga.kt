package eu.kanade.tachiyomi.source.model

interface SManga {
    var url: String
    var title: String
    var thumbnail_url: String?
    var description: String?
    var author: String?
    var artist: String?
    var genre: String?
    var status: Int
    var initialized: Boolean

    companion object {
        const val UNKNOWN = 0
        const val ONGOING = 1
        const val COMPLETED = 2
        const val LICENSED = 3

        fun create(): SManga = SMangaImpl()
    }
}

class SMangaImpl : SManga {
    override var url: String = ""
    override var title: String = ""
    override var thumbnail_url: String? = null
    override var description: String? = null
    override var author: String? = null
    override var artist: String? = null
    override var genre: String? = null
    override var status: Int = SManga.UNKNOWN
    override var initialized: Boolean = false
}