package ca.qc.cstj.tenretni.ui.gateways.details

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import ca.qc.cstj.tenretni.R
import ca.qc.cstj.tenretni.core.ColorHelper
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tenretni.core.loadFromResource
import ca.qc.cstj.tenretni.databinding.FragmentGatewayBinding
import ca.qc.cstj.tenretni.models.Gateway
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GatewayFragment : Fragment(R.layout.fragment_gateway) {
    private val _args: GatewayFragmentArgs by navArgs();
    private val _binding: FragmentGatewayBinding by viewBinding()
    private val _viewModel: GatewayViewModel by viewModels {
        GatewayViewModel.Factory(_args.href)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _viewModel.gatewayUiState.onEach {
            when (it) {
                GatewayUiState.Empty -> Unit
                is GatewayUiState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        it.exception.localizedMessage ?: getString(R.string.apiErrorMessage),
                        Toast.LENGTH_LONG
                    ).show()
                }

                is GatewayUiState.Success -> {
                    (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.gateway_number, it.gateway.serialNumber)
                    displayInformations(it.gateway)
                    displayConnection(it.gateway)
                    displayHash(it.gateway)
                    displayShell(it.gateway)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)


        _binding.btnReboot.setOnClickListener {
            _viewModel.rebootGateway(_args.href)
        }

        _binding.btnUpdate.setOnClickListener {
            _viewModel.updateGateway(_args.href)
        }

    }

    override fun onResume() {
        super.onResume()
        _viewModel.getGateway()
    }

    private fun displayShell(gateway: Gateway) {
        setElementImage(gateway)
        _binding.txvKernelRevision.text = _binding.root.context.getString(
            R.string.kernel_revision,
            gateway.config.kernelRevision
        )
        _binding.txvVersion.text = _binding.root.context.getString(
            R.string.kernel_version,
            gateway.config.version
        )
    }

    private fun displayHash(gateway: Gateway) {
        _binding.txvHash.text = gateway.hash
        setHashCodeColor(gateway)
    }

    private fun displayConnection(gateway: Gateway) {
        if (gateway.connection.status == Constants.ConnectionStatus.Online.toString()) {
            setSignalQualityColor(gateway)
            _binding.txvIpAddress.text = gateway.connection.ip
            _binding.txvLatency.text = _binding.root.context.getString(
                R.string.gateway_latency,
                gateway.connection.ping
            )
            _binding.txvDownload.text = _binding.root.context.getString(
                R.string.gateway_speed,
                gateway.connection.download
            )
            _binding.txvUpload.text = _binding.root.context.getString(
                R.string.gateway_speed,
                gateway.connection.upload
            )
            _binding.txvSignalQuality.text = _binding.root.context.getString(
                R.string.signal_quality,
                gateway.connection.signal
            )
        } else {
            _binding.grpInfo.visibility = View.GONE
            _binding.btnUpdate.visibility = View.GONE
            _binding.btnReboot.visibility = View.GONE
            _binding.txvNotAvailable.visibility = View.VISIBLE
        }
    }

    private fun displayInformations(gateway: Gateway) {
        _binding.chipGatewayStatus.text = gateway.connection.status
        _binding.chipGatewayStatus.chipBackgroundColor =
            ColorHelper.connectionStatusColor(
                _binding.root.context,
                gateway.connection.status
            )
        _binding.txvSerialNumber.text = gateway.serialNumber
        _binding.txvMACAddress.text = gateway.config.mac
        _binding.txvSSID.text = _binding.root.context.getString(
            R.string.gateway_ssid,
            gateway.config.SSID
        )
        _binding.txvPIN.text = _binding.root.context.getString(R.string.gateway_pin, gateway.pin)
    }

    private fun setSignalQualityColor(gateway: Gateway) {
        if (gateway.connection.signal >= -20)
            _binding.txvSignalQuality.setTextColor(resources.getColor(R.color.gatewaySignalQuality_hight))
        else if (gateway.connection.signal in -21 downTo -79)
            _binding.txvSignalQuality.setTextColor(resources.getColor(R.color.gatewaySignalQuality_medium))
        else if (gateway.connection.signal >= -80)
            _binding.txvSignalQuality.setTextColor(resources.getColor(R.color.gatewaySignalQuality_low))
    }

    private fun setElementImage(gateway: Gateway) {
        _binding.imvElement01.loadFromResource(
            _binding.root.context,
            "element_" + gateway.config.kernel[0].lowercase()
        )
        _binding.imvElement02.loadFromResource(
            _binding.root.context,
            "element_" + gateway.config.kernel[1].lowercase()
        )
        _binding.imvElement03.loadFromResource(
            _binding.root.context,
            "element_" + gateway.config.kernel[2].lowercase()
        )
        _binding.imvElement04.loadFromResource(
            _binding.root.context,
            "element_" + gateway.config.kernel[3].lowercase()
        )
        _binding.imvElement05.loadFromResource(
            _binding.root.context,
            "element_" + gateway.config.kernel[4].lowercase()
        )
    }

    // TODO: Bonus, hash
    private fun setHashCodeColor(gateway: Gateway) {
        var twoFirst = gateway.hash.substring(0, 2)
        var middleHash = gateway.hash.substring(2, 62)
        var twoEnd = gateway.hash.substring(62)
        // split hash 10*

        // foreach create shape and add to text

    }

}