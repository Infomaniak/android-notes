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
package com.infomaniak.core.crossapplogin.back.internal

import android.os.Handler
import android.os.Message
import kotlinx.coroutines.channels.SendChannel
import splitties.mainthread.mainLooper
import java.lang.ref.WeakReference

/**
 * A Handler that sends everything it receives to the passed [channel], as a copy.
 * The channel is held in a WeakReference to avoid Handler leak.
 *
 * Used for IPC (inter-process communication)
 */
internal class ChannelMessageHandler(channel: SendChannel<DisposableMessage>) : Handler(mainLooper) {

    private val weakChannelReference = WeakReference(channel)

    override fun handleMessage(msg: Message) {
        weakChannelReference.get()?.let { channel ->
            val disposableMessage = DisposableMessage.Companion.fromCopy(
                originalMessage = msg,
                recycleOriginalMessage = false // Or an IllegalStateException will be thrown,
                // with the message "IllegalStateException: This message cannot be recycled because it is still in use."
            )
            channel.trySend(disposableMessage)
        }
    }
}
