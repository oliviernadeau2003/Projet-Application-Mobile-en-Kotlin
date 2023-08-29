package ca.qc.cstj.tenretni.ui.tickets.details

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import ca.qc.cstj.tenretni.R
import com.google.android.gms.maps.model.LatLng
import ca.qc.cstj.tenretni.core.ColorHelper
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tenretni.core.DateHelper
import ca.qc.cstj.tenretni.databinding.FragmentTicketBinding
import ca.qc.cstj.tenretni.models.Customer
import ca.qc.cstj.tenretni.models.Gateway
import ca.qc.cstj.tenretni.models.Ticket
import ca.qc.cstj.tenretni.ui.gateways.list.GatewaysListRecyclerViewAdapter
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class TicketFragment : Fragment(R.layout.fragment_ticket) {
    private val _args: TicketFragmentArgs by navArgs()
    private val _binding : FragmentTicketBinding by viewBinding()
    private val _viewModel: TicketViewModel by viewModels {
        TicketViewModel.Factory(_args.href)
    }
    private lateinit var _gatewayRecyclerViewAdapter: GatewaysListRecyclerViewAdapter

    private var _position : LatLng? = null
    private var _customerName : String = ""
    private var _hrefCustomer : String = ""

    private val scanQRCode = registerForActivityResult(ScanQRCode(), ::handleQuickieResult)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _gatewayRecyclerViewAdapter = GatewaysListRecyclerViewAdapter(listOf(), ::onRecyclerViewGatewayClick)

        _binding.rcvGateways.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = _gatewayRecyclerViewAdapter
        }

        _viewModel.ticketUiState.onEach {
            when(it){
                TicketUiState.Empty -> Unit
                is TicketUiState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        it.exception.localizedMessage ?:
                        getString(R.string.apiErrorMessage),
                        Toast.LENGTH_LONG).show()
                }
                is TicketUiState.GatewaysLoaded -> {
                    _gatewayRecyclerViewAdapter.gateways = it.gateways
                    _gatewayRecyclerViewAdapter.notifyDataSetChanged()
                }
                is TicketUiState.GatewayInstalled -> {
                    _viewModel.getTicket(_args.href)
                    Toast.makeText(requireContext(), it.gateway.toString(), Toast.LENGTH_LONG).show()
                }

                is TicketUiState.Success -> {
                    displayTicket(it.ticket)
                    _hrefCustomer = it.ticket.customer.href
                    _viewModel.getGateways(it.ticket.customer.href)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        _binding.btnLocalisation.setOnClickListener {
            if (_position != null && _customerName != null) {
                val action = TicketFragmentDirections.actionTicketFragmentToMapsActivity(
                    _position!!,
                    _customerName!!
                )
                findNavController().navigate(action)
            }
        }

        _binding.btnSolve.setOnClickListener {
            _viewModel.solveTicket(_args.href)
        }

        _binding.btnOpen.setOnClickListener {
            _viewModel.openTicket(_args.href)
        }

        _binding.btnInstall.setOnClickListener {
            scanQRCode.launch(null)
        }
    }

    override fun onResume() {
        super.onResume()
        _viewModel.getTicket(_args.href)
    }

    private fun displayTicket(ticket :Ticket){
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.ticket_number, ticket.ticketNumber)
        _binding.layoutTicket.tvTicketNumber.text = _binding.root.context.getString(R.string.ticket_number,ticket.ticketNumber)
        _binding.layoutTicket.tvTicketDate.text = DateHelper.formatISODate(ticket.createdDate)
        _binding.layoutTicket.chipPriorite.text = ticket.priority
        _binding.layoutTicket.chipPriorite.chipBackgroundColor = ColorHelper.ticketPriorityColor(requireContext(),ticket.priority)
        _binding.layoutTicket.chipStatus.text = ticket.status
        _binding.layoutTicket.chipStatus.chipBackgroundColor = ColorHelper.ticketStatusColor(requireContext(), ticket.status)

        if (ticket.status == Constants.TicketStatus.Open.toString()) {
            _binding.btnOpen.visibility = View.INVISIBLE
            _binding.btnSolve.visibility = View.VISIBLE
            _binding.btnInstall.visibility = View.VISIBLE
        } else {
            _binding.btnSolve.visibility = View.INVISIBLE
            _binding.btnInstall.visibility = View.INVISIBLE
            _binding.btnOpen.visibility = View.VISIBLE
        }

        displayCustomer(ticket.customer)
    }

    private fun displayCustomer(customer : Customer) {
        _binding.tvClientName.text = getString(R.string.customer_name, customer.firstName, customer.lastName)
        _binding.tvClientAddress.text = customer.address
        _binding.tvClientTown.text = customer.city

        _position = LatLng(customer.coord.latitude.toDouble(), customer.coord.longitude.toDouble())
        _customerName = getString(R.string.customer_name, customer.lastName, customer.firstName)

        Glide.with(requireContext())
            .load(Constants.FLAG_API_URL.format(customer.country.lowercase()))
            .into(_binding.imvCountryFlag)
    }

    private fun onRecyclerViewGatewayClick(gateway: Gateway) {
        val action = TicketFragmentDirections.actionTicketFragmentToGatewayFragment(gateway.href)
        findNavController().navigate(action)
    }

    private fun handleQuickieResult(qrResult: QRResult) {
        when(qrResult) {
            is QRResult.QRSuccess -> {
                _viewModel.installGateway(_hrefCustomer ,qrResult.content.rawValue)
            }
            QRResult.QRUserCanceled -> Toast.makeText(requireContext(), "Gateway installation canceled", Toast.LENGTH_LONG).show()
            QRResult.QRMissingPermission -> Toast.makeText(requireContext(), "Missing permissions", Toast.LENGTH_LONG).show()
            is QRResult.QRError -> Toast.makeText(requireContext(), qrResult.exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}