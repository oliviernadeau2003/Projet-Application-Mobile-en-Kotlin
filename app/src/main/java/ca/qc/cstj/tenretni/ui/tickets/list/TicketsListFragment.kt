package ca.qc.cstj.tenretni.ui.tickets.list

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ca.qc.cstj.tenretni.R
import ca.qc.cstj.tenretni.databinding.FragmentListTicketsBinding
import ca.qc.cstj.tenretni.models.Ticket
import ca.qc.cstj.tenretni.ui.tickets.details.TicketFragmentDirections
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TicketsListFragment : Fragment(R.layout.fragment_list_tickets) {

    private val _binding: FragmentListTicketsBinding by viewBinding()
    private val _viewModel: TicketsListViewModel by viewModels()

    private lateinit var _ticketsListRecyclerViewAdapter: TicketsListRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _ticketsListRecyclerViewAdapter = TicketsListRecyclerViewAdapter(listOf(), ::onRecyclerViewTicketClic)

        _binding.rcvTickets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = _ticketsListRecyclerViewAdapter
        }

        _viewModel.mainUiState.onEach {
            when(it){
                is TicketsListUiState.Error -> {
                    Toast.makeText(requireContext(), it.exception?.localizedMessage ?: getString(R.string.apiErrorMessage), Toast.LENGTH_LONG).show()
                }
                TicketsListUiState.Loading -> displayLoading()
                is TicketsListUiState.Success -> displayTickets(it.tickets)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onRecyclerViewTicketClic(ticket: Ticket) {
        val action = TicketsListFragmentDirections.actionNavTicketsToTicketFragment(ticket.href)
        findNavController().navigate(action)
    }

    private fun displayTickets(tickets: List<Ticket>) {
        _binding.pgbLoading.hide()
        _binding.rcvTickets.visibility = View.VISIBLE

        _ticketsListRecyclerViewAdapter.tickets = tickets
        _ticketsListRecyclerViewAdapter.notifyDataSetChanged()
    }

    private fun displayLoading() {
        _binding.rcvTickets.visibility = View.GONE
        _binding.pgbLoading.show()
    }
}