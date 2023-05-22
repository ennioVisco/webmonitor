package com.enniovisco.tracking.commands

abstract class BrowserCommand {
    abstract fun dump(target: MutableMap<String, String>)
}
