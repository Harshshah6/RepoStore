package com.samyak.repostore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.samyak.repostore.data.model.AppItem
import com.samyak.repostore.data.repository.GitHubRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: GitHubRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    fun search(query: String) {
        _searchQuery.value = query

        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.value = SearchUiState.Idle
            return
        }

        if (query.length < 2) {
            return // Don't search for single characters
        }

        searchJob = viewModelScope.launch {
            delay(600) // Debounce - increased to reduce API calls

            _uiState.value = SearchUiState.Loading

            // First try local cache
            val cachedResults = repository.searchCachedRepos(query).first()
            if (cachedResults.isNotEmpty()) {
                val appItems = cachedResults.map { repo ->
                    AppItem(repo, null, null)
                }
                _uiState.value = SearchUiState.Success(appItems)
            }

            // Then fetch from API
            val result = repository.searchApps(query)

            result.fold(
                onSuccess = { apps ->
                    _uiState.value = if (apps.isEmpty()) {
                        // If API returns empty but we had cached results, keep showing them
                        if (cachedResults.isNotEmpty()) {
                            val appItems = cachedResults.map { repo ->
                                AppItem(repo, null, null)
                            }
                            SearchUiState.Success(appItems)
                        } else {
                            SearchUiState.Empty
                        }
                    } else {
                        SearchUiState.Success(apps)
                    }
                },
                onFailure = { error ->
                    // If we have cached results, show them instead of error
                    if (cachedResults.isNotEmpty()) {
                        val appItems = cachedResults.map { repo ->
                            AppItem(repo, null, null)
                        }
                        _uiState.value = SearchUiState.Success(appItems)
                    } else {
                        _uiState.value = SearchUiState.Error(error.message ?: "Search failed")
                    }
                }
            )
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _uiState.value = SearchUiState.Idle
        searchJob?.cancel()
    }
}

sealed class SearchUiState {
    data object Idle : SearchUiState()
    data object Loading : SearchUiState()
    data object Empty : SearchUiState()
    data class Success(val apps: List<AppItem>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}

class SearchViewModelFactory(private val repository: GitHubRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
