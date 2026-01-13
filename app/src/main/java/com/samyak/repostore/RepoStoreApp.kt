package com.samyak.repostore

import android.app.Application
import android.content.Context
import com.samyak.repostore.data.api.RetrofitClient
import com.samyak.repostore.data.db.AppDatabase
import com.samyak.repostore.data.repository.GitHubRepository

class RepoStoreApp : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { GitHubRepository(database.repoDao()) }

    override fun onCreate() {
        super.onCreate()

        // Initialize RetrofitClient with cache
        val token = getGitHubToken()
        RetrofitClient.init(this, token)
    }

    private fun getGitHubToken(): String? {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_GITHUB_TOKEN, null)
    }

    fun setGitHubToken(token: String?) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_GITHUB_TOKEN, token).apply()
        RetrofitClient.setToken(token)
    }

    fun getStoredToken(): String? {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_GITHUB_TOKEN, null)
    }

    companion object {
        private const val PREFS_NAME = "github_app_store_prefs"
        private const val KEY_GITHUB_TOKEN = "github_token"
    }
}
