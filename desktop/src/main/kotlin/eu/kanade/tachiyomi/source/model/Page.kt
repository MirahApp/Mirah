package eu.kanade.tachiyomi.source.model

interface Page {
    val index: Int
    var url: String
    var imageUrl: String?
}