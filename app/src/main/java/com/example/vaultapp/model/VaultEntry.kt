package com.example.vaultapp.model

import com.example.vaultapp.model.EntryType

data class VaultEntry(
    val id: String,
    val title: String,
    val type: EntryType,
    val username: String,
    val secret: String,
    val note: String,
    val timestamp: Long,
    val favorite: Boolean
)
