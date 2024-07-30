package ru.hummel.testidea

import android.app.Application
import ru.hummel.testidea.di.AppModule

class TestIdeaApplication : Application() {

  companion object {
    lateinit var appModule: AppModule
  }

  override fun onCreate() {
    super.onCreate()
    appModule = AppModule(applicationContext)
  }

}