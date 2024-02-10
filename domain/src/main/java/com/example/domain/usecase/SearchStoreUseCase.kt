package com.example.domain.usecase

import com.example.domain.model.map.StoreDetail
import com.example.domain.repository.SearchStoreRepository
import com.example.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class SearchStoreUseCase(
    private val repository: SearchStoreRepository
) {
    suspend operator fun invoke(
        currLong: Double,
        currLat: Double,
        searchKeyword: String
    ): Flow<Resource<List<StoreDetail>>> = flow {
        emit(Resource.Loading())
        repository.searchStore(currLong, currLat, searchKeyword).fold(
            onSuccess = { items ->
                if (items.isEmpty()) {
                    emit(Resource.Failure(ErrorMessage.ERROR_MESSAGE_STORE_IS_EMPTY))
                } else {
                    emit(Resource.Success(items.map { storeDetailModel ->
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
                    }))
                }
            },
            onFailure = { e ->
                when (e) {
                    is UnknownHostException -> {
                        emit(Resource.Failure(ErrorMessage.ERROR_MESSAGE_CHECK_INTERNET))
                    }

                    is SocketTimeoutException -> {
                        emit(Resource.Failure(ErrorMessage.ERROR_MESSAGE_SERVER_IS_NOT_WORKING))
                    }

                    else -> {
                        emit(Resource.Failure(ErrorMessage.ERROR_MESSAGE_UNKNOWN_ERROR))
                    }
                }
            }
        )
    }
}