package otus.homework.coroutines

import android.view.View

/**
 * Изменение visibility view, делаем view видимым (VISIBLE)
 * если текущее состояние == VISIBLE, ничего не делаем
 */
fun View.toVisible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

/**
 * Изменение visibility view, делаем view невидимым (INVISIBLE)
 * если текущее состояние == INVISIBLE, ничего не делаем
 */
fun View.toInvisible() {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
}