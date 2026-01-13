package com.samyak.repostore.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.samyak.repostore.R
import com.samyak.repostore.RepoStoreApp
import com.samyak.repostore.data.model.AppItem
import com.samyak.repostore.databinding.FragmentSearchBinding
import com.samyak.repostore.ui.activity.DetailActivity
import com.samyak.repostore.ui.activity.DeveloperActivity
import com.samyak.repostore.ui.adapter.RankedAppAdapter
import com.samyak.repostore.ui.viewmodel.SearchUiState
import com.samyak.repostore.ui.viewmodel.SearchViewModel
import com.samyak.repostore.ui.viewmodel.SearchViewModelFactory
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory((requireActivity().application as RepoStoreApp).repository)
    }

    private lateinit var appAdapter: RankedAppAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchBar()
        setupRecyclerView()
        observeViewModel()

        // Focus search field
        binding.etSearch.requestFocus()
    }

    private fun setupSearchBar() {
        binding.btnBack.setOnClickListener {
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack()
            } else {
                activity?.finish()
            }
        }

        binding.etSearch.doAfterTextChanged { text ->
            val query = text?.toString() ?: ""
            viewModel.search(query)
            binding.btnClear.visibility = if (query.isNotEmpty()) View.VISIBLE else View.GONE
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.search(binding.etSearch.text?.toString() ?: "")
                true
            } else false
        }

        binding.btnClear.setOnClickListener {
            binding.etSearch.text?.clear()
            viewModel.clearSearch()
        }
    }

    private fun setupRecyclerView() {
        appAdapter = RankedAppAdapter(
            onItemClick = { appItem ->
                navigateToDetail(appItem)
            },
            onDeveloperClick = { developer, avatarUrl ->
                val intent = DeveloperActivity.newIntent(requireContext(), developer, avatarUrl)
                startActivity(intent)
            }
        )

        binding.rvSearchResults.apply {
            adapter = appAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleUiState(state)
                }
            }
        }
    }

    private fun handleUiState(state: SearchUiState) {
        when (state) {
            is SearchUiState.Idle -> {
                binding.progressBar.visibility = View.GONE
                binding.rvSearchResults.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.tvMessage.text = getString(R.string.search_hint)
            }
            is SearchUiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.rvSearchResults.visibility = View.GONE
                binding.layoutEmpty.visibility = View.GONE
            }
            is SearchUiState.Empty -> {
                binding.progressBar.visibility = View.GONE
                binding.rvSearchResults.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.tvMessage.text = getString(R.string.no_results)
            }
            is SearchUiState.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.rvSearchResults.visibility = View.VISIBLE
                binding.layoutEmpty.visibility = View.GONE
                appAdapter.submitList(state.apps)
            }
            is SearchUiState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.rvSearchResults.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.tvMessage.text = state.message
            }
        }
    }

    private fun navigateToDetail(appItem: AppItem) {
        val intent = DetailActivity.newIntent(
            requireContext(),
            appItem.repo.owner.login,
            appItem.repo.name
        )
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
