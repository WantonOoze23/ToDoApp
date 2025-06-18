package com.tyshko.todoapp.di

import android.content.Context
import androidx.room.Room
import com.tyshko.data.local.ToDoDao
import com.tyshko.data.local.ToDoDataBase
import com.tyshko.data.network.NetworkApi
import com.tyshko.data.repository.ToDoRepositoryImpl
import com.tyshko.domain.repository.ToDoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : ToDoDataBase{
        return Room.databaseBuilder(
            context, ToDoDataBase::class.java, "todo_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideToDoDao(dataBase: ToDoDataBase): ToDoDao = dataBase.toDoDao()


    @Provides
    @Singleton
    fun provideClient() : HttpClient{
        return HttpClient(){
            install(ContentNegotiation){
                json(Json {
                    encodeDefaults = true
                    prettyPrint = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    @Provides
    @Singleton
    fun provideNetworkApi(httpClient: HttpClient): NetworkApi{
        return NetworkApi(httpClient)
    }

    @Provides
    @Singleton
    fun provideRepository( toDoDao: ToDoDao, networkApi: NetworkApi): ToDoRepository {
        return ToDoRepositoryImpl(toDoDao, networkApi)
    }

}