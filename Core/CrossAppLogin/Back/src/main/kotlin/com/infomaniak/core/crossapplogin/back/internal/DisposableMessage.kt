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

@file:OptIn(ExperimentalContracts::class)

package com.infomaniak.core.crossapplogin.back.internal

import android.os.Message
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@JvmInline
internal value class DisposableMessage private constructor(private val message: Message) {

    companion object {
        fun fromCopy(
            originalMessage: Message,
            recycleOriginalMessage: Boolean
        ): DisposableMessage {
            val copiedMessage = Message.obtain(originalMessage)
            if (recycleOriginalMessage) originalMessage.recycle()
            return DisposableMessage(copiedMessage)
        }
    }

    inline fun <R> use(block: (msg: Message) -> R): R {
        contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
        return try {
            block(message)
        } finally {
            message.recycle()
        }
    }
}
