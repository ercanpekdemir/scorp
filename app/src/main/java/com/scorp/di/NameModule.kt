package com.scorp.di

import com.scorp.data.DataSource
import com.scorp.data.NameDataRepository
import com.scorp.data.NameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object NameModule {

    @ViewModelScoped
    @Provides
    fun provideNameRepository(dataSource: DataSource): NameRepository = NameDataRepository(dataSource)
}