/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package net.waterfox.android.tabstray

import androidx.annotation.VisibleForTesting
import mozilla.components.lib.state.Middleware
import mozilla.components.lib.state.MiddlewareContext

/**
 * [Middleware] that reacts to various [TabsTrayAction]s.
 */
class TabsTrayMiddleware : Middleware<TabsTrayState, TabsTrayAction> {

    private var shouldReportInactiveTabMetrics: Boolean = true

    override fun invoke(
        context: MiddlewareContext<TabsTrayState, TabsTrayAction>,
        next: (TabsTrayAction) -> Unit,
        action: TabsTrayAction
    ) {
        next(action)

        when (action) {
            is TabsTrayAction.UpdateInactiveTabs -> {
                if (shouldReportInactiveTabMetrics) {
                    shouldReportInactiveTabMetrics = false
                }
            }
            else -> {
                // no-op
            }
        }
    }

    @Suppress("MagicNumber")
    @VisibleForTesting
    /**
     * This follows the logic outlined in metrics.yaml for "search_terms.group_size_distribution"
     */
    internal fun generateTabGroupSizeMappedValue(size: Int): Long =
        when (size) {
            2 -> 1L
            in 3..5 -> 2L
            in 6..10 -> 3L
            else -> 4L
        }
}
