package com.volumetracker.app.security

object SecurityUtils {
    
    /**
     * Validates if a string is a valid Solana address
     * Base58 encoded, 32-44 characters
     */
    fun isValidSolanaAddress(address: String): Boolean {
        if (address.length !in 32..44) return false
        
        // Base58 alphabet (excluding 0, O, I, l)
        val base58Pattern = Regex("^[1-9A-HJ-NP-Za-km-z]+$")
        return base58Pattern.matches(address)
    }
    
    /**
     * Truncates a Solana address for display
     */
    fun truncateAddress(address: String, prefixLength: Int = 4, suffixLength: Int = 4): String {
        if (address.length <= prefixLength + suffixLength) return address
        return "${address.take(prefixLength)}...${address.takeLast(suffixLength)}"
    }
    
    /**
     * Sanitizes user input to prevent injection attacks
     */
    fun sanitizeInput(input: String): String {
        return input.trim()
            .replace(Regex("[<>\"'`]"), "")
            .take(100)
    }
}
