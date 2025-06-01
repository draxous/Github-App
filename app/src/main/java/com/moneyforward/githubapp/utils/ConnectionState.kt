package com.moneyforward.githubapp.utils

/**
 * Sealed class representing the state of the network connection.
 * This class is used to observe changes in network availability.
 */
sealed class ConnectionState {
    /**
     * Represents the state where the network connection is available.
     */
    data object Available : ConnectionState()
    /**
     * Represents the state where the network connection is unavailable.
     */
    data object Unavailable : ConnectionState()
}