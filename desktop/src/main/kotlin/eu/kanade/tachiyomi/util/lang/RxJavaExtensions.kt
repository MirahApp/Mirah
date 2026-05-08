package eu.kanade.tachiyomi.util.lang

import rx.Observable
import rx.Single

fun <T> Observable<T>.awaitSingle(): T = toSingle().toBlocking().value()
fun <T> Single<T>.asObservable(): Observable<T> = toObservable()