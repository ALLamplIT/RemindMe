package com.lit.remindme.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.lit.remindme.Notifications.NotificationWorkerStarter
import com.lit.remindme.R
import com.lit.remindme.feature_events.data.data_sources.InternalEventDb
import com.lit.remindme.feature_events.data.data_sources.SettingsStore
import com.lit.remindme.feature_events.data.repository.EventRepositoryImpl
import com.lit.remindme.feature_events.domain.repository.EventRepository
import com.lit.remindme.feature_events.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideEventDatabase(app: Application): InternalEventDb {
     return Room.databaseBuilder(
         app,
         InternalEventDb::class.java,
         InternalEventDb.DATABASE_NAME
     ).build()
    }

    @Provides
    @Singleton
    fun provideEventRepository(db: InternalEventDb):EventRepository {
        return EventRepositoryImpl(db.internalDataDao)
    }

    @Provides
    @Singleton
    fun provideEventUseCases(repository: EventRepository):EventUseCases {
        return EventUseCases(
            getEvents = GetEvents(repository),
            getEventsByMonthAndDay = GetEventsByMonthAndDay(repository),
            deleteEvent = DeleteEvent(repository),
            disableEvent = DisableEvent(repository),
            addEvent = AddEvent(repository),
            getEventById = GetEventById(repository),
            getEventByLookupId = GetEventByLookupId(repository),
            deleteAllEvents = DeleteAllEvents(repository),
            resetEventsTable = ResetEventsTable(repository)
        )
    }

    @Provides
    @Singleton
    fun provideSettingsStore(@ApplicationContext context: Context): SettingsStore {
        return SettingsStore(context)
    }

    @Provides
    @Singleton
    fun provideNotificationWorkerStarter(@ApplicationContext context: Context): NotificationWorkerStarter {
        return NotificationWorkerStarter(context)
    }

    @Provides
    @Singleton
    @Named("StringFromResource")
    fun provideString2(
        @ApplicationContext context: Context
    ) = context.getString(R.string.string_from_resource)

    @Provides
    @Singleton
    fun provideContext(
        @ApplicationContext context: Context
    ) = context

}