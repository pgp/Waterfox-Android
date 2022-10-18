/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package net.waterfox.android.home.recentbookmarks.view

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LifecycleOwner
import mozilla.components.lib.state.ext.observeAsComposableState
import net.waterfox.android.R
import net.waterfox.android.components.components
import net.waterfox.android.compose.ComposeViewHolder
import net.waterfox.android.home.recentbookmarks.interactor.RecentBookmarksInteractor

class RecentBookmarksViewHolder(
    composeView: ComposeView,
    viewLifecycleOwner: LifecycleOwner,
    val interactor: RecentBookmarksInteractor,
) : ComposeViewHolder(composeView, viewLifecycleOwner) {
    companion object {
        val LAYOUT_ID = View.generateViewId()
    }

    @Composable
    override fun Content() {
        val recentBookmarks = components.appStore
            .observeAsComposableState { state -> state.recentBookmarks }

        RecentBookmarks(
            bookmarks = recentBookmarks.value ?: emptyList(),
            onRecentBookmarkClick = interactor::onRecentBookmarkClicked,
            menuItems = listOf(
                RecentBookmarksMenuItem(
                    stringResource(id = R.string.recently_saved_menu_item_remove),
                    onClick = { bookmark -> interactor.onRecentBookmarkRemoved(bookmark) }
                )
            )
        )
    }
}
