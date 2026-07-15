package com.example.vaultapp.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.security.SecureRandom
import java.util.UUID
import com.example.vaultapp.model.VaultEntry
import com.example.vaultapp.model.EntryType

/**
 * Generiert ein kryptographisch sicheres 16-stelliges Passwort mit einer
 * Mischung aus Groß- und Kleinbuchstaben, Zahlen und Sonderzeichen.
 */
fun generateSecurePassword(length: Int = 16): String {
    val upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val lower = "abcdefghijklmnopqrstuvwxyz"
    val digits = "0123456789"
    val symbols = "!@#$%^&*()_+-=[]{}|;:,.<>?"
    val allChars = upper + lower + digits + symbols
    val random = SecureRandom()

    val password = StringBuilder()
    // Garantiert mindestens ein Zeichen aus jeder Kategorie
    password.append(upper[random.nextInt(upper.length)])
    password.append(lower[random.nextInt(lower.length)])
    password.append(digits[random.nextInt(digits.length)])
    password.append(symbols[random.nextInt(symbols.length)])

    for (i in 4 until length) {
        password.append(allChars[random.nextInt(allChars.length)])
    }

    return password.toString().toList().shuffled(random).joinToString("")
}

@Composable
fun AddEntryScreen(
    onSave: (VaultEntry) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    // Formular-Zustände
    var type by remember { mutableStateOf(EntryType.PASSWORD) }
    var title by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var secret by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }

    // Validierungsfehler-Zustände
    var titleError by remember { mutableStateOf(false) }
    var secretError by remember { mutableStateOf(false) }

    // Passwort-Sichtbarkeits-Zustand
    var isSecretVisible by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState)
    ) {
        // Kopfzeile
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Neuer Eintrag",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            )

            // Abbrechen Button (X)
            IconButton(
                onClick = onCancel,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF1E1E1E))
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Abbrechen",
                    tint = Color.White
                )
            }
        }

        // Untertitel
        Text(
            text = "Füge ein neues verschlüsseltes Element zu deinem hardware-geschützten Tresor hinzu.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF64748B),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // 1. Typ-Auswahl (PASSWORD vs. NOTE)
        Text(
            text = "Eintragstyp",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF94A3B8)
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val types = listOf(
                EntryType.PASSWORD to "Passwort",
                EntryType.NOTE to "Sichere Notiz"
            )
            types.forEach { (itemType, label) ->
                val selected = type == itemType
                val icon = if (itemType == EntryType.PASSWORD) Icons.Default.Lock else Icons.Default.Edit

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selected) Color(0xFF00E676) else Color(0xFF1E1E1E))
                        .clickable { type = itemType }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (selected) Color(0xFF121212) else Color(0xFF00E676),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (selected) Color(0xFF121212) else Color.White
                            )
                        )
                    }
                }
            }
        }

        // 2. Titel Eingabefeld
        Text(
            text = "Titel *",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF94A3B8)
            ),
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                if (it.isNotBlank()) titleError = false
            },
            placeholder = { Text("z.B. Google Account, Router Login", color = Color(0xFF64748B)) },
            isError = titleError,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1E1E1E),
                unfocusedContainerColor = Color(0xFF1E1E1E),
                focusedBorderColor = Color(0xFF00E676),
                unfocusedBorderColor = Color(0xFF2E2E2E),
                errorBorderColor = Color(0xFFEF4444),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            supportingText = {
                if (titleError) {
                    Text("Titel darf nicht leer sein", color = Color(0xFFEF4444))
                }
            }
        )

        // 3. Benutzername Eingabefeld
        Text(
            text = "Benutzername / E-Mail",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF94A3B8)
            ),
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text("admin@company.com (optional)", color = Color(0xFF64748B)) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1E1E1E),
                unfocusedContainerColor = Color(0xFF1E1E1E),
                focusedBorderColor = Color(0xFF00E676),
                unfocusedBorderColor = Color(0xFF2E2E2E),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // 4. Geheimnis/Passwort Eingabefeld (inkl. Generator und Augen-Icon)
        val secretLabel = if (type == EntryType.PASSWORD) "Passwort *" else "Geheimnis / Seed Phrase *"
        Text(
            text = secretLabel,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF94A3B8)
            ),
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = secret,
            onValueChange = {
                secret = it
                if (it.isNotBlank()) secretError = false
            },
            placeholder = { Text("Sicheres Geheimnis eintragen", color = Color(0xFF64748B)) },
            isError = secretError,
            singleLine = (type == EntryType.PASSWORD),
            maxLines = if (type == EntryType.PASSWORD) 1 else 5,
            visualTransformation = if (isSecretVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1E1E1E),
                unfocusedContainerColor = Color(0xFF1E1E1E),
                focusedBorderColor = Color(0xFF00E676),
                unfocusedBorderColor = Color(0xFF2E2E2E),
                errorBorderColor = Color(0xFFEF4444),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Passwort Sichtbarkeit umschalten
                    IconButton(onClick = { isSecretVisible = !isSecretVisible }) {
                        Icon(
                            imageVector = if (isSecretVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isSecretVisible) "Ausblenden" else "Anzeigen",
                            tint = Color(0xFF94A3B8)
                        )
                    }

                    // Sicheres Passwort generieren (Würfel/Refresh Icon)
                    IconButton(onClick = {
                        secret = generateSecurePassword()
                        secretError = false
                        Toast.makeText(context, "Sicheres Passwort generiert!", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Passwort generieren",
                            tint = Color(0xFF00E676)
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            supportingText = {
                if (secretError) {
                    Text("Das Geheimfeld darf nicht leer sein", color = Color(0xFFEF4444))
                }
            }
        )

        // 5. Notiz Eingabefeld (mehrzeilig)
        Text(
            text = "Notizen",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF94A3B8)
            ),
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            placeholder = { Text("Zusätzliche Notizen (optional)", color = Color(0xFF64748B)) },
            minLines = 3,
            maxLines = 6,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1E1E1E),
                unfocusedContainerColor = Color(0xFF1E1E1E),
                focusedBorderColor = Color(0xFF00E676),
                unfocusedBorderColor = Color(0xFF2E2E2E),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )

        // 6. Favorit Switch Zeile
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1E1E1E))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Zu Favoriten hinzufügen",
                    tint = if (isFavorite) Color(0xFFF59E0B) else Color(0xFF64748B),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Favorit",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Schneller Zugriff in der Favoritenliste",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Switch(
                checked = isFavorite,
                onCheckedChange = { isFavorite = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF121212),
                    checkedTrackColor = Color(0xFF00E676),
                    uncheckedThumbColor = Color(0xFF64748B),
                    uncheckedTrackColor = Color(0xFF2E2E2E)
                )
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 7. Aktionsknöpfe
        Button(
            onClick = {
                val isTitleBlank = title.isBlank()
                val isSecretBlank = secret.isBlank()

                titleError = isTitleBlank
                secretError = isSecretBlank

                if (!isTitleBlank && !isSecretBlank) {
                    val entry = VaultEntry(
                        id = UUID.randomUUID().toString(),
                        title = title.trim(),
                        type = type,
                        username = username.trim(),
                        secret = secret.trim(),
                        note = note.trim(),
                        timestamp = System.currentTimeMillis(),
                        favorite = isFavorite
                    )
                    onSave(entry)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00E676),
                contentColor = Color(0xFF121212)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Icon(
                imageVector = Icons.Default.VerifiedUser,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Eintrag verschlüsseln & speichern",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onCancel,
            border = BorderStroke(1.dp, Color(0xFF2E2E2E)),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = "Abbrechen",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
