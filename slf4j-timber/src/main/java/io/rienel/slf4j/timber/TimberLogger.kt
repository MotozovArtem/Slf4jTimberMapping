package io.rienel.slf4j.timber

import android.util.Log
import org.slf4j.helpers.MarkerIgnoringBase
import timber.log.Timber

internal class TimberLogger(
	val className: String
): MarkerIgnoringBase() {

	override fun isTraceEnabled(): Boolean {
		return isLoggable(Log.VERBOSE)
	}

	override fun trace(msg: String) {
		setOneTimeTagForLog(className)
		Timber.v(msg)
	}

	override fun trace(msg: String, arg: Any?) {
		setOneTimeTagForLog(className)
		Timber.v(msg.withTimberPlaceholders(), arg)
	}

	override fun trace(msg: String, arg1: Any?, arg2: Any?) {
		setOneTimeTagForLog(className)
		Timber.v(msg.withTimberPlaceholders(), arg1, arg2)
	}

	override fun trace(msg: String, vararg arguments: Any?) {
		setOneTimeTagForLog(className)
		Timber.v(msg.withTimberPlaceholders(), *arguments)
	}

	override fun trace(msg: String, t: Throwable?) {
		setOneTimeTagForLog(className)
		Timber.v(t, msg)
	}

	override fun isDebugEnabled(): Boolean {
		return isLoggable(Log.DEBUG)
	}


	override fun debug(msg: String) {
		setOneTimeTagForLog(className)
		Timber.d(msg)
	}

	override fun debug(msg: String, arg: Any?) {
		setOneTimeTagForLog(className)
		Timber.d(msg.withTimberPlaceholders(), arg)
	}

	override fun debug(msg: String, arg1: Any?, arg2: Any?) {
		setOneTimeTagForLog(className)
		Timber.d(msg.withTimberPlaceholders(), arg1, arg2)
	}

	override fun debug(msg: String, vararg arguments: Any?) {
		setOneTimeTagForLog(className)
		Timber.d(msg.withTimberPlaceholders(), *arguments)
	}

	override fun debug(msg: String, t: Throwable?) {
		setOneTimeTagForLog(className)
		Timber.d(t, msg)
	}

	override fun isInfoEnabled(): Boolean {
		return isLoggable(Log.INFO)
	}

	override fun info(msg: String) {
		setOneTimeTagForLog(className)
		Timber.i(msg)
	}

	override fun info(msg: String, arg: Any?) {
		setOneTimeTagForLog(className)
		Timber.i(msg.withTimberPlaceholders(), arg)
	}

	override fun info(msg: String, arg1: Any?, arg2: Any?) {
		setOneTimeTagForLog(className)
		Timber.i(msg.withTimberPlaceholders(), arg1, arg2)
	}

	override fun info(msg: String, vararg arguments: Any?) {
		setOneTimeTagForLog(className)
		Timber.i(msg.withTimberPlaceholders(), *arguments)
	}

	override fun info(msg: String, t: Throwable?) {
		setOneTimeTagForLog(className)
		Timber.i(t, msg)
	}

	override fun isWarnEnabled(): Boolean {
		return isLoggable(Log.WARN)
	}

	override fun warn(msg: String) {
		setOneTimeTagForLog(className)
		Timber.w(msg)
	}

	override fun warn(msg: String, arg: Any?) {
		setOneTimeTagForLog(className)
		Timber.w(msg.withTimberPlaceholders(), arg)
	}

	override fun warn(msg: String, arg1: Any?, arg2: Any?) {
		setOneTimeTagForLog(className)
		Timber.w(msg.withTimberPlaceholders(), arg1, arg2)
	}

	override fun warn(msg: String, vararg arguments: Any?) {
		setOneTimeTagForLog(className)
		Timber.w(msg.withTimberPlaceholders(), *arguments)
	}

	override fun warn(msg: String, t: Throwable?) {
		setOneTimeTagForLog(className)
		Timber.w(t, msg)
	}

	override fun isErrorEnabled(): Boolean {
		return isLoggable(Log.ERROR)
	}

	override fun error(msg: String) {
		setOneTimeTagForLog(className)
		Timber.e(msg)
	}

	override fun error(msg: String, arg: Any?) {
		setOneTimeTagForLog(className)
		Timber.e(msg.withTimberPlaceholders(), arg)
	}

	override fun error(msg: String, arg1: Any?, arg2: Any?) {
		setOneTimeTagForLog(className)
		Timber.e(msg.withTimberPlaceholders(), arg1, arg2)
	}

	override fun error(msg: String, vararg arguments: Any?) {
		setOneTimeTagForLog(className)
		Timber.e(msg.withTimberPlaceholders(), *arguments)
	}

	override fun error(msg: String, t: Throwable?) {
		setOneTimeTagForLog(className)
		Timber.e(t, msg)
	}

	private fun setOneTimeTagForLog(tag: String) {
		Timber.tag(tag)
	}

	private fun isLoggable(priority: Int): Boolean {
		return Timber.treeCount > 0
	}

	private fun String.withTimberPlaceholders() = this.replace("{}", "%s")
}