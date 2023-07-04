package io.rienel.slf4j.timber

import org.slf4j.ILoggerFactory
import org.slf4j.Logger

class TimberLoggerFactory: ILoggerFactory{
	override fun getLogger(name: String): Logger {
		return TimberLogger(name)
	}
}