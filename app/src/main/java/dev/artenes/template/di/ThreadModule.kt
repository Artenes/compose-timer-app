package dev.artenes.template.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class ThreadModule {

    @Provides
    fun providesIODispatcher(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    fun providesBackgroundCoroutine(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    }

}