package ru.hummel.testidea

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import ru.hummel.testidea.di.AppModule

class TestIdeaApplication : Application() {

  companion object {
    lateinit var appModule: AppModule
  }

  override fun onCreate() {
    super.onCreate()
    AndroidThreeTen.init(this)
    appModule = AppModule(applicationContext)
  }

}