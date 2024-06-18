package dev.artenes.template.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.artenes.template.core.interfaces.DataRepository
import dev.artenes.template.data.AppDatabase
import dev.artenes.template.data.AppRepository
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesDataRepository(
        appDatabase: AppDatabase,
        dispatcher: CoroutineContext
    ): DataRepository {
        return AppRepository(appDatabase.sampleDao(), dispatcher)
    }

}
