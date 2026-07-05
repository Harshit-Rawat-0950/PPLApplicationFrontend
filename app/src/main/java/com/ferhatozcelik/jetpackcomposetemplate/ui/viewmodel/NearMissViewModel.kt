package com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferhatozcelik.jetpackcomposetemplate.data.dao.NearMissDao
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.NearMissEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NearMissViewModel @Inject constructor(
    private val nearMissDao: NearMissDao
) : ViewModel() {

    val allNearMisses: Flow<List<NearMissEntity>> = nearMissDao.getAllNearMisses()

    fun insertNearMiss(
        title: String,
        description: String,
        plantArea: String,
        criticality: Int,
        probability: Int
    ) {
        val riskScore = criticality * probability
        val entity = NearMissEntity(
            title = title,
            description = description,
            plantArea = plantArea,
            criticality = criticality,
            probability = probability,
            riskScore = riskScore
        )
        viewModelScope.launch {
            nearMissDao.insertNearMiss(entity)
        }
    }
}
