 1. Executive Summary (Kurze Vision der App)
VaultCore 16 ist ein kompromisslos offline-fokussierter, hardware-geschützter Datentresor für Android-Geräte. Ziel ist maximale Datensouveränität durch eine strikte Zero-Knowledge-Architektur. Es gibt keinerlei externe Serververbindungen oder Cloud-Synchronisationen. Alle Daten verbleiben verschlüsselt auf dem lokalen Gerät und sind kryptografisch an den Hardware-Sicherheitsbereich des Smartphones gekoppelt.
🛠️ 2. Tech Stack
Die App nutzt modernste Android-Standards:
Sprache: Kotlin (Coroutines, Flows)
UI Framework: Jetpack Compose (Edge-to-Edge, Material Design 3)
Navigation: Jetpack Compose Navigation (navigation-compose) für ein modulares, typsicheres Routing
Serialisierung: Google Gson (für die JSON-Strukturierung)
Sicherheit: Android Keystore System (TEE/StrongBox) & AES-256-GCM
🔑 3. Core Features (Hauptfunktionen)
Sicheres In-App Security Keyboard: Verhindert Keylogger auf Betriebssystemebene beim Eingeben der Master-PIN.
Dynamisches Passwort- & Safe-Notiz-Management: Erlaubt das Speichern von Passwörtern und verschlüsselten Fließtexten (z.B. Recovery Seeds).
Zufallsgenerator (SecureRandom): Generiert hochkomplexe Passwörter mit garantierten Zeichensätzen.
Komfort-Kopierfunktion: Zwischenablage-Integration mit automatischem visuellem Toast-Feedback.
🔒 4. Security & Privacy Architecture
Hardware-Backed AES-256-GCM: Die symmetrischen Verschlüsselungsschlüssel verbleiben physisch geschützt in der hardware-isolierten Umgebung (TEE / StrongBox).
Zero Klartext-Memory: Der Speicher (vaultEntries) wird beim Sperren der App sofort im Arbeitsspeicher auf emptyList() gesetzt.
Isolierter Speicher: Die Datei vault_secret.enc liegt verschlüsselt im geschützten App-Verzeichnis und ist für andere Apps unzugänglich.
📊 5. Data Model / JSON-Struktur
Das Datenmodell (VaultEntry) ist so strukturiert, dass es flexibel erweitert werden kann und alle Einträge vor dem Verschlüsseln in ein stabiles JSON-Array serialisiert.
