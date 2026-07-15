package com.example.vaultapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.vaultapp.model.VaultEntry
import com.example.vaultapp.model.EntryType
import com.example.vaultapp.ui.theme.VaultAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VaultAppTheme {
                // Simple in-memory store for demo purposes
                val entries = remember { mutableStateListOf<VaultEntry>() }
                AddEntryScreen(
                    onSave = { entry ->
                        entries.add(entry)
                        // For simplicity: show a toast or log; but we just finish activity
                        finish()
                    },
                    onCancel = { finish() }
                )
            }
        }
    }
}
