package com.syahna.appdicodingevent.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.syahna.appdicodingevent.databinding.FragmentFavoriteBinding
import com.syahna.appdicodingevent.detail.DetailActivity
import kotlinx.coroutines.*

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by viewModels {
        FavoriteViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var favAdapter: FavoriteAdapter
    private var loadingJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        favAdapter = FavoriteAdapter(
            { event ->
                navigateToDetail(event.id)
            },
            { event ->
                favoriteViewModel.deleteFavoriteById(event.id)
            }
        )
        binding.rvFavorite.apply {
            adapter = favAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObservers() {
        favoriteViewModel.getIsLoading.observe(viewLifecycleOwner) { isLoading ->
            if (favAdapter.currentList.isEmpty()) {
                binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        favoriteViewModel.getAllFavEvent().observe(viewLifecycleOwner) { favoriteList ->
            Log.d("FavoriteFragment", "Number of Favorite Events: ${favoriteList.size}")
            favAdapter.submitList(favoriteList)
            toggleEmptyState(favoriteList.isEmpty())
            binding.progressBar2.visibility = View.GONE
        }
    }

    private fun controlLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar2.visibility = View.VISIBLE
            loadingJob?.cancel()
            loadingJob = CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                binding.progressBar2.visibility = View.GONE
            }
        } else {
            loadingJob?.cancel()
            binding.progressBar2.visibility = View.GONE
        }
    }

    private fun toggleEmptyState(isEmpty: Boolean) {
        binding.tvNoFavorite.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun navigateToDetail(eventId: String) {
        Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_EVENT_ID, eventId)
            startActivity(this)
        }
    }

    override fun onDestroyView() {
        Log.d("FavoriteFragment", "onDestroyView called, releasing binding.")
        super.onDestroyView()
        _binding = null
    }
}