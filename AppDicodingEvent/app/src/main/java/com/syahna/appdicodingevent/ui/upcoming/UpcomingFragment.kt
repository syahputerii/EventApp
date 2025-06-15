package com.syahna.appdicodingevent.ui.upcoming

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
import com.syahna.appdicodingevent.databinding.FragmentUpcomingBinding
import com.syahna.appdicodingevent.detail.DetailActivity
import com.syahna.appdicodingevent.ui.EventAdapter

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private val upcomingViewModel: UpcomingViewModel by viewModels()

    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchBar()

        upcomingViewModel.listEvents.observe(viewLifecycleOwner) { events ->
            eventAdapter.updateEvents(events)
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        upcomingViewModel.fetchEvents()
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter(emptyList()) { event -> navigateToDetailEvent(event) }
        binding.rvEvent.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvent.adapter = eventAdapter
    }

    private fun setupSearchBar() {
        val searchEditText: TextInputEditText = binding.searchBar.findViewById(R.id.searchEditText)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterEvents(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun filterEvents(query: String) {
        val allEvents = upcomingViewModel.listEvents.value ?: emptyList()
        val filteredEvents = allEvents.filter { event ->
            event.name?.contains(query, ignoreCase = true) == true
        }

        if (filteredEvents.isEmpty()) {
            Toast.makeText(requireContext(), "No events found", Toast.LENGTH_SHORT).show()
        }

        eventAdapter.updateEvents(filteredEvents)
    }

    private fun navigateToDetailEvent(event: ListEventsItem) {
        Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_EVENT_ID, event.id.toString())
        }.also { startActivity(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}