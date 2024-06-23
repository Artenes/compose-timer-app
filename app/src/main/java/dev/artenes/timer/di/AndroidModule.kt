package dev.artenes.timer.di

import android.content.ComponentName
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.artenes.timer.android.AndroidApplicationsRepository
import dev.artenes.timer.android.AndroidNotifications
import dev.artenes.timer.android.AndroidServiceConnection
import dev.artenes.timer.android.AndroidSettingsRepository
import dev.artenes.timer.android.AndroidValuesRepository
import dev.artenes.timer.android.dataStore
import dev.artenes.timer.app.MainActivity
import dev.artenes.timer.core.interfaces.ApplicationsRepository
import dev.artenes.timer.core.interfaces.Notifications
import dev.artenes.timer.core.interfaces.SettingsRepository
import dev.artenes.timer.core.interfaces.ValuesRepository
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class AndroidModule {

    @Provides
    @Singleton
    fun providesSettingsRepository(
        @ApplicationContext context: Context,
        dispatcher: CoroutineContext
    ): SettingsRepository {
        return AndroidSettingsRepository(context.dataStore, dispatcher)
    }

    @Provides
    @Singleton
    fun providesValuesRepository(@ApplicationContext context: Context): ValuesRepository {
        return AndroidValuesRepository(context)
    }

    @Provides
    @Singleton
    fun providesApplicationsRepository(@ApplicationContext context: Context): ApplicationsRepository {
        return AndroidApplicationsRepository(
            context,
            ComponentName(context.packageName, MainActivity::class.qualifiedName!!)
        )
    }

    @Provides
    @Singleton
    fun providesNotifications(@ApplicationContext context: Context): Notifications {
        return AndroidNotifications(
            context,
            MainActivity::class.java
        )
    }

    @Provides
    fun providesServiceConnection(@ApplicationContext context: Context): AndroidServiceConnection {
        return AndroidServiceConnection(context)
    }

}