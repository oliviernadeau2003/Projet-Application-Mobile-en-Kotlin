package ca.qc.cstj.tenretni.core

object Constants {

    object BaseURL {
        //private const val BASE_API = "http://10.0.2.2:5000"
        private const val BASE_API = "https://api.andromia.science"
        const val TICKETS = "$BASE_API/tickets"
        const val CUSTOMERS = "$BASE_API/customers"
        const val GATEWAYS = "$BASE_API/gateways"
        const val NETWORK = "$BASE_API/network"
    }

    object RefreshDelay {
        const val TICKET_DELAY: Long = 30000L
        const val GATEWAY_DELAY: Long = 60000L
    }

    object LoadingTimer {
        const val TIMER_INTERVAL: Long = 1000L
        const val TIMER_TIME: Long = 10000L
    }

    object Map {
        const val ZOOM: Float = 15f
    }

    const val FLAG_API_URL = "https://flagcdn.com/h40/%s.png"

    enum class TicketPriority {
        Low, Normal, High, Critical
    }

    enum class TicketStatus {
        Open, Solved
    }

    enum class ConnectionStatus {
        Online, Offline
    }

}