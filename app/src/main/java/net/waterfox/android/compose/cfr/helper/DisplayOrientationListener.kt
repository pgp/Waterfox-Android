/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package net.waterfox.android.compose.cfr.helper

import android.content.Context
import android.hardware.display.DisplayManager
import android.hardware.display.DisplayManager.DisplayListener
import android.os.Build
import androidx.annotation.VisibleForTesting

/**
 * Inform when the rotation of the screen changes.
 * Since this is using a [DisplayManager] listener it's important to call [start] and [stop]
 * at the appropriate moments to register and unregister said listener.
 *
 * @param context Android context needed to interact with the [DisplayManager]
 * @param onDisplayRotationChanged Listener for when the display rotation changes.
 * This will be called when the display changes to any of the four main orientations:
 * [PORTRAIT, LANDSCAPE, REVERSE_PORTRAIT, REVERSE_LANDSCAPE].
 * No updates will be triggered if the "Auto-rotate" functionality is disabled for the device.
 */
internal class DisplayOrientationListener(
    private val context: Context,
    val onDisplayRotationChanged: () -> Unit,
) : DisplayListener {
    private val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager

    @VisibleForTesting
    internal var currentOrientation = getCurrentOrientation()

    /**
     * Start listening for display orientation changes.
     * It's important to also call [stop] when done listening to prevent leaking the listener.
     */
    fun start() {
        displayManager.registerDisplayListener(this, null)
    }

    /**
     * Stop listening for display orientation changes and cleanup the current [DisplayManager] listener.
     */
    fun stop() {
        displayManager.unregisterDisplayListener(this)
    }

    override fun onDisplayAdded(displayId: Int) = Unit

    override fun onDisplayRemoved(displayId: Int) = Unit

    override fun onDisplayChanged(displayId: Int) {
        val newOrientation = getCurrentOrientation(displayId)

        if (newOrientation != this.currentOrientation) {
            this.currentOrientation = newOrientation
            onDisplayRotationChanged()
        }
    }

    private fun getCurrentOrientation(displayId: Int = 0): Int = when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        true -> context.resources.configuration.orientation
        false -> displayManager.getDisplay(displayId)?.rotation ?: currentOrientation
    }
}
