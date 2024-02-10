package com.example.domain.usecase

import com.example.domain.model.map.StoreDetail
import com.example.domain.repository.SearchStoreRepository
import com.example.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

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
            },
            onFailure = { e ->
                if (e is IOException) {
                    emit(Resource.Failure("서버와의 통신이 원활하지 않습니다."))
                } else {
                    emit(Resource.Failure("데이터를 불러올 수 없습니다."))
                }
            }
        )
    }
}