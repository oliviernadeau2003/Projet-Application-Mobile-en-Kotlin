package ca.qc.cstj.tenretni.ui.gateways.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.qc.cstj.tenretni.R
import ca.qc.cstj.tenretni.core.ColorHelper
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tenretni.databinding.ItemGatewayBinding
import ca.qc.cstj.tenretni.models.Gateway

class GatewaysListRecyclerViewAdapter(
    var gateways: List<Gateway> = listOf(),
    private val _onGatewayClick: (Gateway) -> Unit
) : RecyclerView.Adapter<GatewaysListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemGatewayBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val gateway = gateways[position]
        holder.bind(gateway)

        holder.itemView.setOnClickListener {
            _onGatewayClick(gateway)
        }
    }

    override fun getItemCount() = gateways.size

    inner class ViewHolder(private val binding: ItemGatewayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(gateway: Gateway) {
            binding.chipGatewayStatus.text = gateway.connection.status
            binding.chipGatewayStatus.chipBackgroundColor =
                ColorHelper.connectionStatusColor(binding.root.context, gateway.connection.status)
            binding.txvSerialNumber.text = gateway.serialNumber
            if (gateway.connection.status == Constants.ConnectionStatus.Online.toString()) {
                binding.txvLatency.text = binding.root.context.getString(
                    R.string.gateway_latency,
                    gateway.connection.ping
                )
                binding.txvDownload.text = binding.root.context.getString(
                    R.string.gateway_speed,
                    gateway.connection.download
                )
                binding.txvUpload.text = binding.root.context.getString(
                    R.string.gateway_speed,
                    gateway.connection.upload
                )
            } else {
                binding.grpInfo.visibility = View.INVISIBLE
                binding.txvNotAvailable.visibility = View.VISIBLE
            }

        }
    }

}