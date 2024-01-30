package com.d101.data.repository

import com.d101.data.datasource.fruitcreate.FruitCreateRemoteDataSource
import com.d101.data.mapper.FruitCreatedMapper.toFruitCreated
import com.d101.domain.model.FruitCreated
import com.d101.domain.repository.FruitCreateRepository
import java.io.File
import javax.inject.Inject

class FruitCreateRepositoryImpl @Inject constructor(
    private val fruitCreateRemoteDataSource: FruitCreateRemoteDataSource,
) : FruitCreateRepository {

    override suspend fun sendText(text: String): List<FruitCreated> {
        return fruitCreateRemoteDataSource.sendText(text).map {
            it.toFruitCreated()
        }
    }

    override suspend fun sendFile(file: File): List<FruitCreated> {
        return fruitCreateRemoteDataSource.sendFile(file).map {
            it.toFruitCreated()
        }
    }
}
