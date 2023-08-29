package ca.qc.cstj.tenretni.ui.tickets.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.qc.cstj.tenretni.R
import ca.qc.cstj.tenretni.core.ColorHelper
import ca.qc.cstj.tenretni.core.DateHelper
import ca.qc.cstj.tenretni.databinding.ItemTicketBinding
import ca.qc.cstj.tenretni.models.Ticket

class TicketsListRecyclerViewAdapter(
    var tickets: List<Ticket> = listOf(),
    private val _onTicketClick: (Ticket) -> Unit)
    : RecyclerView.Adapter<TicketsListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTicketBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = tickets[position]
        holder.bind(ticket)

        holder.itemView.setOnClickListener {
            _onTicketClick(ticket)
        }
    }

    override fun getItemCount() = tickets.size

    inner class ViewHolder(private val binding: ItemTicketBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: Ticket) {
            binding.tvTicketNumber.text = binding.root.context.getString(R.string.ticket_number,ticket.ticketNumber)
            binding.tvTicketDate.text = DateHelper.formatISODate(ticket.createdDate)
            binding.chipPriorite.text = ticket.priority
            binding.chipPriorite.chipBackgroundColor = ColorHelper.ticketPriorityColor(binding.root.context, ticket.priority)
            binding.chipStatus.text = ticket.status
            binding.chipStatus.chipBackgroundColor = ColorHelper.ticketStatusColor(binding.root.context, ticket.status)
        }
    }
}