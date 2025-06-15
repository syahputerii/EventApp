package com.syahna.appdicodingevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.syahna.appdicodingevent.R
import com.syahna.appdicodingevent.data.response.ListEventsItem
import com.syahna.appdicodingevent.databinding.FragmentHomeBinding
import com.syahna.appdicodingevent.detail.DetailActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var eventHorizontal: EventHorizontal
    private lateinit var eventVertical: EventVertical

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerViews()
        initializeAdapters()
        setupSearchBar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        fetchEventData()
    }

    private fun setupRecyclerViews() {
        binding.rvUpcoming.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvEvent.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initializeAdapters() {
        eventHorizontal = EventHorizontal(emptyList()) { selectedEvent -> navigateToDetailEvent(selectedEvent) }
        binding.rvUpcoming.adapter = eventHorizontal

        eventVertical = EventVertical(emptyList()) { selectedEvent -> navigateToDetailEvent(selectedEvent) }
        binding.rvEvent.adapter = eventVertical
    }

    private fun observeViewModel() {
        homeViewModel.listFinishedEvents.observe(viewLifecycleOwner) { finishedEvents ->
            eventVertical.updateEvents(finishedEvents)
        }

        homeViewModel.listUpcomingEvents.observe(viewLifecycleOwner) { upcomingEvents ->
            eventHorizontal.updateEvents(upcomingEvents)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun fetchEventData() {
        homeViewModel.fetchFinishedEvents()
        homeViewModel.fetchUpcomingEvents()
    }

    private fun setupSearchBar() {
        val searchEditText: TextInputEditText = binding.searchBar.findViewById(R.id.searchEditText)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterEvents(s?.toString().orEmpty())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun filterEvents(query: String) {
        val filteredHorizontalList = homeViewModel.listUpcomingEvents.value?.filter { event ->
            event.name?.contains(query, ignoreCase = true) == true
        } ?: emptyList()

        val filteredVerticalList = homeViewModel.listFinishedEvents.value?.filter { event ->
            event.name?.contains(query, ignoreCase = true) == true
        } ?: emptyList()

        if (filteredHorizontalList.isEmpty() && filteredVerticalList.isEmpty()) {
            Toast.makeText(requireContext(), "No events found", Toast.LENGTH_SHORT).show()
        }

        eventHorizontal.updateEvents(filteredHorizontalList)
        eventVertical.updateEvents(filteredVerticalList)
    }

    private fun navigateToDetailEvent(event: ListEventsItem) {
        Intent(requireContext(), DetailActivity::class.java).also { intent ->
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id.toString())
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}