package com.example.domain.usecase

import com.example.domain.model.map.StoreDetail
import com.example.domain.repository.StoreDetailRepository
import com.example.domain.usecase.ErrorMessage.ERROR_MESSAGE_CHECK_INTERNET
import com.example.domain.usecase.ErrorMessage.ERROR_MESSAGE_SERVER_IS_NOT_WORKING
import com.example.domain.usecase.ErrorMessage.ERROR_MESSAGE_STORE_IS_EMPTY
import com.example.domain.usecase.ErrorMessage.ERROR_MESSAGE_UNKNOWN_ERROR
import com.example.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class GetStoreDetailUseCase(
    private val repository: StoreDetailRepository
) {
    suspend operator fun invoke(
        nwLong: Double,
        nwLat: Double,
        swLong: Double,
        swLat: Double,
        seLong: Double,
        seLat: Double,
        neLong: Double,
        neLat: Double,
    ): Flow<Resource<List<List<StoreDetail>>>> = flow {
        emit(Resource.Loading())
        repository.getStoreDetail(
            nwLong,
            nwLat,
            swLong,
            swLat,
            seLong,
            seLat,
            neLong,
            neLat
        ).fold(onSuccess = { items ->
            if (items.isEmpty()) {
                emit(Resource.Failure(ERROR_MESSAGE_STORE_IS_EMPTY))
            } else {
                emit(Resource.Success(items.map {
                    it.map { storeDetailModel ->
                        val operatingType = getOperatingType(storeDetailModel.regularOpeningHours)
                        val operationTimeOfWeek = getOperationTimeOfWeek(storeDetailModel)
                        StoreDetail(
                            id = storeDetailModel.id,
                            displayName = storeDetailModel.displayName,
                            primaryTypeDisplayName = storeDetailModel.primaryTypeDisplayName,
                            formattedAddress = storeDetailModel.formattedAddress,
                            phoneNumber = storeDetailModel.phoneNumber,
                            location = storeDetailModel.location,
                            operatingType = operatingType.operatingType.description,
                            timeDescription = operatingType.timeDescription,
                            localPhotos = storeDetailModel.localPhotos,
                            certificationName = storeDetailModel.certificationName,
                            operationTimeOfWeek = operationTimeOfWeek
                        )
                    }
                }))
            }
        }, onFailure = { e ->
            when (e) {
                is UnknownHostException -> {
                    emit(Resource.Failure(ERROR_MESSAGE_CHECK_INTERNET))
                }

                is SocketTimeoutException -> {
                    emit(Resource.Failure(ERROR_MESSAGE_SERVER_IS_NOT_WORKING))
                }

                else -> {
                    emit(Resource.Failure(ERROR_MESSAGE_UNKNOWN_ERROR))
                }
            }
        })
    }
}