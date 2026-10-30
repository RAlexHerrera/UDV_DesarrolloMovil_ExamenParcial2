package com.udv.notasapp.util

import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.Collections

/** Utilidad para el panel de diagnóstico de conexión: IP(s) real(es) del dispositivo. */
object DeviceNetworkInfo {

    fun obtenerIpsLocales(): List<String> {
        return try {
            Collections.list(NetworkInterface.getNetworkInterfaces())
                .flatMap { iface -> Collections.list(iface.inetAddresses) }
                .filter { !it.isLoopbackAddress && it is Inet4Address }
                .map { it.hostAddress ?: "" }
                .filter { it.isNotBlank() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
