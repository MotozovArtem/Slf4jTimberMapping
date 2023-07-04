package io.rienel.slf4j

import android.app.Application
import android.util.Log
import timber.log.Timber

class Slf4jApplication: Application()  {
	private lateinit var timberTreeToFile: TimberTreeToFile

	override fun onCreate() {
		super.onCreate()
		timberTreeToFile = TimberTreeToFile.create(this, Log.INFO)
		Timber.plant(timberTreeToFile)
		Timber.plant(Timber.DebugTree())
	}
}