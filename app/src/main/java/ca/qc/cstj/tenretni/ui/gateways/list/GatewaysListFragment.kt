package ca.qc.cstj.tenretni.ui.gateways.list

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import ca.qc.cstj.tenretni.R
import ca.qc.cstj.tenretni.databinding.FragmentListGatewaysBinding
import ca.qc.cstj.tenretni.models.Gateway
import ca.qc.cstj.tenretni.ui.gateways.list.GatewaysListFragmentDirections
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class GatewaysListFragment : Fragment(R.layout.fragment_list_gateways) {

    private val _binding: FragmentListGatewaysBinding by viewBinding()
    private val _viewModel: GatewaysListViewModel by viewModels()

    private lateinit var _gatewaysListRecyclerViewAdapter: GatewaysListRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _gatewaysListRecyclerViewAdapter =
            GatewaysListRecyclerViewAdapter(listOf(), ::onRecyclerViewTicketClick)

        _binding.rcvGateway.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = _gatewaysListRecyclerViewAdapter
        }

        _viewModel.mainUiState.onEach {
            when (it) {
                is GatewaysListUiState.Error -> Toast.makeText(
                    requireContext(),
                    it.exception?.localizedMessage ?: getString(R.string.apiErrorMessage),
                    Toast.LENGTH_LONG
                ).show()

                GatewaysListUiState.Loading -> displayLoading()
                is GatewaysListUiState.Success -> displayGateways(it.gateways)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun displayGateways(gateways: List<Gateway>) {
        _binding.pgbLoading.hide()
        _binding.rcvGateway.visibility = View.VISIBLE

        _gatewaysListRecyclerViewAdapter.gateways = gateways
        _gatewaysListRecyclerViewAdapter.notifyDataSetChanged()
    }

    private fun onRecyclerViewTicketClick(gateway: Gateway) {
        val action = GatewaysListFragmentDirections.actionNavGatewaysToGatewayFragment(gateway.href)
        findNavController().navigate(action)
    }

    private fun displayLoading() {
        _binding.rcvGateway.visibility = View.GONE
        _binding.pgbLoading.show()
    }
}