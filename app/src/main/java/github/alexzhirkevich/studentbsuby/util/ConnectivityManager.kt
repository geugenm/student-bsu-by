package github.alexzhirkevich.studentbsuby.util

import android.content.Context
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import github.alexzhirkevich.studentbsuby.util.communication.MutableStateCommunication
import github.alexzhirkevich.studentbsuby.util.communication.Releasable
import github.alexzhirkevich.studentbsuby.util.communication.StateCommunication

interface ConnectivityManager : Releasable
{

    val isNetworkConnected: StateCommunication<Boolean>

    class Internet(
        context: Context,
        override val isNetworkConnected: MutableStateCommunication<Boolean>,
                  ) : ConnectivityManager
    {

        private var wifiAvailable = false
        private var cellAvailable = false

        private var wifiCallback: android.net.ConnectivityManager.NetworkCallback? = null
        private var cellCallback: android.net.ConnectivityManager.NetworkCallback? = null

        private val service =
            context.getSystemService(android.net.ConnectivityManager::class.java).also {
                    wifiCallback = it.register(NetworkCapabilities.TRANSPORT_WIFI) {
                        wifiAvailable = it
                        isNetworkConnected.map(wifiAvailable || cellAvailable)
                    }
                    cellCallback = it.register(NetworkCapabilities.TRANSPORT_CELLULAR) {
                        cellAvailable = it
                        isNetworkConnected.map(wifiAvailable || cellAvailable)
                    }
                }

        fun finalize()
        {
            release()
        }

        override fun release()
        {
            listOfNotNull(wifiCallback, cellCallback).forEach(service::unregisterNetworkCallback)
        }
    }
}

private fun android.net.ConnectivityManager.register(
    transport: Int, set: (Boolean) -> Unit
                                                    ): android.net.ConnectivityManager.NetworkCallback
{
    val callback = object : android.net.ConnectivityManager.NetworkCallback()
    {
        override fun onAvailable(network: Network)
        {
            super.onAvailable(network)
            set(true)
        }

        override fun onLost(network: Network)
        {
            super.onLost(network)
            set(false)
        }
    }
    registerNetworkCallback(
        NetworkRequest.Builder().addTransportType(transport)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build(), callback
                           )
    return callback
}