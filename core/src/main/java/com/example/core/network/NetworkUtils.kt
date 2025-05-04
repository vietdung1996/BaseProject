package com.example.core.network

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Utility class for network operations
 * Requires ACCESS_NETWORK_STATE permission in AndroidManifest.xml
 */
object NetworkUtils {

    /**
     * Checks if network connection is available
     *
     * @param context Application context
     * @return true if network is available
     */
    fun isNetworkAvailable(context: Context): Boolean {
        // First check if permission is granted
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_NETWORK_STATE
            ) != PackageManager.PERMISSION_GRANTED) {
            return true // Return true as default when can't check to prevent app crash
        }

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // For API 23+
            checkNetworkCapabilities(connectivityManager)
        } else {
            // For API below 23
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo != null && networkInfo.isConnected
        }
    }

    /**
     * Gets the current connection type
     *
     * @param context Application context
     * @return String describing connection type or "None" if not connected
     */
    fun getConnectionType(context: Context): String {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_NETWORK_STATE
            ) != PackageManager.PERMISSION_GRANTED) {
            return "Unknown" // Can't determine type without permission
        }

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return "None"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return "None"
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return "None"

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> "Bluetooth"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "VPN"
                else -> "Other"
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            return when (networkInfo?.type) {
                ConnectivityManager.TYPE_WIFI -> "WiFi"
                ConnectivityManager.TYPE_MOBILE -> "Cellular"
                ConnectivityManager.TYPE_ETHERNET -> "Ethernet"
                ConnectivityManager.TYPE_BLUETOOTH -> "Bluetooth"
                ConnectivityManager.TYPE_VPN -> "VPN"
                else -> if (networkInfo?.isConnected == true) "Other" else "None"
            }
        }
    }

    /**
     * Checks network capabilities for API 23+
     *
     * @param connectivityManager ConnectivityManager instance
     * @return true if connection is available
     */
    private fun checkNetworkCapabilities(connectivityManager: ConnectivityManager): Boolean {
        try {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
        } catch (e: Exception) {
            return false
        }
    }
} 