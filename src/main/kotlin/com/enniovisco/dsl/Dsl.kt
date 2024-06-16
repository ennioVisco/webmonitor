package com.enniovisco.dsl

import com.enniovisco.Spec
import com.enniovisco.WebSource


fun webSource(init: WebSource.() -> Unit): WebSource {
    WebSource.init()
    return WebSource
}

fun spec(init: Spec.() -> Unit): Spec {
    Spec.init()
    return Spec
}