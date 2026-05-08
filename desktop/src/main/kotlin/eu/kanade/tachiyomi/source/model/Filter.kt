package eu.kanade.tachiyomi.source.model

sealed class Filter<T>(val name: String, var state: T) {
    open class Text(name: String, state: String = "") : Filter<String>(name, state)
    open class CheckBox(name: String, state: Boolean = false) : Filter<Boolean>(name, state)
    open class TriState(name: String, state: Int = STATE_IGNORE) : Filter<Int>(name, state) {
        companion object {
            const val STATE_IGNORE = 0
            const val STATE_INCLUDE = 1
            const val STATE_EXCLUDE = 2
        }
    }
    open class Select<V>(name: String, val values: Array<V>, state: Int = 0) : Filter<Int>(name, state)
    open class Sort(name: String, val values: Array<String>, state: Selection? = null) : Filter<Sort.Selection?>(name, state) {
        data class Selection(val index: Int, val ascending: Boolean)
    }
    open class Group<V>(name: String, state: List<V>) : Filter<List<V>>(name, state)
    open class Header(name: String) : Filter<Any>(name, Unit)
    open class Separator(name: String = "") : Filter<Any>(name, Unit)
}
    class FilterList private constructor(val filters: List<Filter<*>>) : List<Filter<*>> by filters {
    constructor(vararg filters: Filter<*>) : this(filters.toList())
}