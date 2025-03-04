package kd.dhyani.wheretheyare.ui.theme.screens.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kd.dhyani.wheretheyare.utils.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val _isInternetAvailable = MutableStateFlow(true)
    val isInternetAvailable: StateFlow<Boolean> get() = _isInternetAvailable

    fun checkInternet() {
        viewModelScope.launch {
            _isInternetAvailable.value = NetworkUtils.isInternetAvailable(getApplication())
        }
    }
}
