package com.lovelycatv.arkcache.related

import com.lovelycatv.arkcache.CacheTemplate


class CacheTemplateContainer {
    private val templates = mutableMapOf<String, Any>()

    fun <T> getTemplate(clazz: Class<T>) = templates[clazz.name] ?: throw NullPointerException("Template not found: ${clazz.name}")

    fun <T> registerTemplate(clazz: Class<T>, action: (CacheTemplateFactory.Builder<T>) -> CacheTemplateFactory.Builder<T>) {
        customRegisterTemplate(clazz, false) {
            action(CacheTemplateFactory.getBuilder(it)).build()
        }
    }

    fun <T> registerMultiTemplate(clazz: Class<T>, action: (CacheTemplateFactory.MultiBuilder<T>) -> CacheTemplateFactory.MultiBuilder<T>) {
        customRegisterTemplate(clazz, false) {
            action(CacheTemplateFactory.getMultiBuilder(it)).buildToMulti()
        }
    }

    fun <T> customRegisterTemplate(
        clazz: Class<T>,
        override: Boolean = false,
        templateAlias: (Class<T>) -> String = {clazz.name},
        action: (Class<T>) -> Any) {
        if (isTemplateRegistered(clazz) && !override) {
            throw IllegalStateException("Template [${clazz.name}] is already registered, if you want to override it, please try 'override = true'")
        }
        templates[templateAlias(clazz)] = action(clazz).run { if (this is CacheTemplate<*>) this else throw IllegalArgumentException("Provided object is not CacheTemplate<*>") }
    }

    private fun <T> isTemplateRegistered(clazz: Class<T>): Boolean = templates.keys.contains(clazz.name)

    @Suppress("UNCHECKED_CAST")
    fun getAllTemplates(): List<CacheTemplate<*>>
        = templates.map {it.value} as List<CacheTemplate<*>>

}