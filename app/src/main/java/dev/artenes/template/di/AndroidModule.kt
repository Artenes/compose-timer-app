package dev.artenes.template.di

import android.content.ComponentName
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.artenes.template.android.AndroidApplicationsRepository
import dev.artenes.template.android.AndroidNotifications
import dev.artenes.template.android.AndroidServiceConnection
import dev.artenes.template.android.AndroidSettingsRepository
import dev.artenes.template.android.AndroidValuesRepository
import dev.artenes.template.android.dataStore
import dev.artenes.template.app.MainActivity
import dev.artenes.template.core.interfaces.ApplicationsRepository
import dev.artenes.template.core.interfaces.Notifications
import dev.artenes.template.core.interfaces.SettingsRepository
import dev.artenes.template.core.interfaces.ValuesRepository
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