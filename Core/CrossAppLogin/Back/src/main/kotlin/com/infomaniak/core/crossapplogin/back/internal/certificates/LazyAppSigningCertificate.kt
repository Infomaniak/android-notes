/*
 * Infomaniak Notes - Android
 * Copyright (C) 2025-2026 Infomaniak Network SA
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.infomaniak.core.crossapplogin.back.internal.certificates

/**
 * The [getSha256Fingerprint] lambda return value can include the colon separator (`:`),
 * and can be uppercase (this is what the Play Store returns).
 */
internal class LazyAppSigningCertificate(getSha256Fingerprint: () -> String) {

    private val sha256FingerprintString by lazy { getSha256Fingerprint().replace(":", "").lowercase() }

    @OptIn(ExperimentalStdlibApi::class)
    private val sha256Fingerprint: ByteArray by lazy { sha256FingerprintString.hexToByteArray() }

    fun matches(expectedSha256: ByteArray): Boolean = sha256Fingerprint contentEquals expectedSha256
}
