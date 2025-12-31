package com.masterproject.englishapp.di

import android.content.Context
import com.masterproject.englishapp.data.loader.TokenLoader
import com.masterproject.englishapp.data.loader.AndroidAssetLoader
import com.masterproject.englishapp.data.loader.PhraseLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoaderModule {

    @Provides
    @Singleton
    fun provideAssetLoader(@ApplicationContext context: Context): AndroidAssetLoader =
        AndroidAssetLoader(context)

    @Provides
    @Singleton
    fun provideTokenLoader(assetLoader: AndroidAssetLoader): TokenLoader =
        TokenLoader(assetLoader)

    @Provides
    @Singleton
    fun providePhraseLoader(assetLoader: AndroidAssetLoader): PhraseLoader =
        PhraseLoader(assetLoader)
}