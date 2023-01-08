package com.example.rpcClient

import kotlinx.coroutines.Deferred
import shared.matrix.MatrixImplListDto

object ResultsRepository {
    private val resultsInt : MutableMap<Int, Deferred<MatrixImplListDto<Int>>> = mutableMapOf()
    private var counterInt : Int = 0
    private val resultsFloat : MutableMap<Int, Deferred<MatrixImplListDto<Float>>> = mutableMapOf()
    private var counterFloat : Int = 0

    suspend fun getInt(id : Int) : MatrixImplListDto<Int>{
        if (id < 0 || id > counterInt - 1){
            throw IllegalArgumentException("ResultsRepository.getInt: id is out of resultsInt indices")
        }

        return resultsInt[id]!!.await()
    }

    suspend fun getFloat(id : Int) : MatrixImplListDto<Float>{
        if (id < 0 || id > counterFloat - 1){
            throw IllegalArgumentException("ResultsRepository.getFloat: id is out of resultsFloat indices")
        }

        return resultsFloat[id]!!.await()
    }

    fun putInt(future : Deferred<MatrixImplListDto<Int>>) : Int{
        val id = counterInt++
        resultsInt[id] = future

        return id
    }

    fun putFloat(future : Deferred<MatrixImplListDto<Float>>) : Int{
        val id = counterFloat++
        resultsFloat[id] = future

        return id
    }
}