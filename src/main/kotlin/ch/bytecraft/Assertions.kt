package ch.bytecraft

object Assertions {
    var enabled = true
}

internal inline fun assert(condition: () -> Boolean) {
    if (Assertions.enabled && !condition()) {
        throw AssertionError("Assertion failed")
    }
}

internal inline fun assert(condition: () -> Boolean, lazyMessage: () -> Any) {
    if (Assertions.enabled && !condition()) {
        throw AssertionError(lazyMessage())
    }
}