package io.rienel.slf4j.timber

import org.slf4j.ILoggerFactory
import org.slf4j.IMarkerFactory
import org.slf4j.helpers.BasicMarkerFactory
import org.slf4j.helpers.NOPMDCAdapter
import org.slf4j.spi.MDCAdapter
import org.slf4j.spi.SLF4JServiceProvider

class TimberSlf4jServiceProvider: SLF4JServiceProvider {
	override fun getLoggerFactory(): ILoggerFactory {
		return TimberLoggerFactory()
	}

	override fun getMarkerFactory(): IMarkerFactory {
		return BasicMarkerFactory()
	}

	override fun getMDCAdapter(): MDCAdapter {
		return NOPMDCAdapter()
	}

	override fun getRequestedApiVersion(): String {
		return "2.0.7"
	}

	override fun initialize() {
		// do nothing
	}
}