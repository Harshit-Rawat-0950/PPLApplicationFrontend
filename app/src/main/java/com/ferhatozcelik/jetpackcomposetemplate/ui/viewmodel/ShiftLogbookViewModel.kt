package com.ferhatozcelik.jetpackcomposetemplate.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferhatozcelik.jetpackcomposetemplate.data.dao.ShiftLogbookDao
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.AssetData
import com.ferhatozcelik.jetpackcomposetemplate.data.entity.ShiftLogbookEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShiftLogbookViewModel @Inject constructor(
    private val shiftLogbookDao: ShiftLogbookDao
) : ViewModel() {

    val allLogbooks: Flow<List<ShiftLogbookEntity>> = shiftLogbookDao.getAllLogbooks()

    fun insertLogbook(
        date: String,
        shift: String,
        area: String,
        submitterId: String,
        assets: List<AssetData>
    ) {
        val entity = ShiftLogbookEntity(
            date = date,
            shift = shift,
            area = area,
            submitterId = submitterId,
            assets = assets
        )
        viewModelScope.launch {
            shiftLogbookDao.insertLogbook(entity)
        }
    }
}
