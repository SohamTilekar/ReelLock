package com.reellock.app

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class ReelBlockerService : AccessibilityService() {

    private var lastBlockTime = 0L

    // Tracks if user is viewing a reel that came from DM chat
    private var inChat = false
    private var watchingChatReel = false
    private var chatReelId: String? = null

    companion object {
        private const val TAG = "ReelBlockerService"
        var isServiceRunning = false
            private set

        const val PREFS_NAME = "reellock_prefs"
        const val KEY_BLOCKED_COUNT = "blocked_count"
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        isServiceRunning = true
        Log.d(TAG, "ReelBlockerService Connected")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        isServiceRunning = false
        return super.onUnbind(intent)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val packageName = event.packageName?.toString() ?: return
        if (packageName != "com.instagram.android" && packageName != "com.instagram.lite") {
            inChat = false
            watchingChatReel = false
            chatReelId = null
            return
        }

        val rootNode = rootInActiveWindow ?: return

        // 1. Detect Chat Thread screen
        if (checkIfInChat(rootNode, event)) {
            inChat = true
            watchingChatReel = false
            chatReelId = null
            return
        }

        // 2. Detect Reels Viewer state
        val isReels = isReelsViewerActive(rootNode, event)

        if (isReels) {
            val now = System.currentTimeMillis()
            val currentReelId = findReelIdentifier(rootNode)

            // If opened from a chat screen
            if (inChat || watchingChatReel) {
                watchingChatReel = true

                if (chatReelId == null && currentReelId != null) {
                    // Record the single shared reel ID and allow it to play
                    chatReelId = currentReelId
                    Log.d(TAG, "Allowing chat reel: $chatReelId")
                    return
                }

                // If user scrolls (TYPE_VIEW_SCROLLED) or the Reel ID changes -> Go to HOME screen!
                val isScrolled = event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED
                if (isScrolled || (chatReelId != null && currentReelId != null && chatReelId != currentReelId)) {
                    if (now - lastBlockTime > 800) {
                        lastBlockTime = now
                        incrementBlockedCount()
                        Log.d(TAG, "Chat Reel scrolled -> Go Home")
                        goToHomeScreen()
                    }
                }
            } else {
                // Opened from Reels Tab or Home Feed -> Go to HOME screen!
                if (now - lastBlockTime > 800) {
                    lastBlockTime = now
                    incrementBlockedCount()
                    Log.d(TAG, "Feed/Tab Reel detected -> Go Home")
                    goToHomeScreen()
                }
            }
        } else {
            // Reset chat tracking if user navigated away from Reels
            val className = event.className?.toString() ?: ""
            if (!className.contains("Clips", ignoreCase = true) && !className.contains("Reel", ignoreCase = true)) {
                watchingChatReel = false
                chatReelId = null
            }
        }
    }

    private fun checkIfInChat(node: AccessibilityNodeInfo, event: AccessibilityEvent): Boolean {
        val className = event.className?.toString() ?: ""
        if (className.contains("Direct", ignoreCase = true) ||
            className.contains("Thread", ignoreCase = true) ||
            className.contains("Message", ignoreCase = true)
        ) {
            return true
        }

        return inspectNodeForChat(node)
    }

    private fun inspectNodeForChat(node: AccessibilityNodeInfo): Boolean {
        val viewId = node.viewIdResourceName ?: ""
        if (viewId.contains("row_thread", ignoreCase = true) ||
            viewId.contains("message_composer", ignoreCase = true) ||
            viewId.contains("action_bar_title", ignoreCase = true) && viewId.contains("direct", ignoreCase = true)
        ) {
            return true
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            if (inspectNodeForChat(child)) return true
        }

        return false
    }

    private fun isReelsViewerActive(node: AccessibilityNodeInfo, event: AccessibilityEvent): Boolean {
        val className = event.className?.toString() ?: ""

        // Check if Reels tab on bottom navigation is selected
        if (isReelsTabSelected(node)) {
            return true
        }

        if (className.contains("Clips", ignoreCase = true) ||
            className.contains("Reel", ignoreCase = true) ||
            hasReelsContainer(node)
        ) {
            return true
        }

        return false
    }

    private fun isReelsTabSelected(node: AccessibilityNodeInfo): Boolean {
        val viewId = node.viewIdResourceName ?: ""
        val contentDesc = node.contentDescription?.toString() ?: ""

        if ((viewId.contains("reels_tab", ignoreCase = true) || contentDesc.equals("Reels", ignoreCase = true)) &&
            (node.isSelected || node.isFocused)
        ) {
            return true
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            if (isReelsTabSelected(child)) return true
        }

        return false
    }

    private fun hasReelsContainer(node: AccessibilityNodeInfo): Boolean {
        val viewId = node.viewIdResourceName ?: ""
        if (viewId.contains("clips_video_container", ignoreCase = true) ||
            viewId.contains("clips_viewer_container", ignoreCase = true) ||
            viewId.contains("clips_swipe_refresh_layout", ignoreCase = true) ||
            viewId.contains("reels_viewer_container", ignoreCase = true)
        ) {
            return true
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            if (hasReelsContainer(child)) return true
        }

        return false
    }

    private fun findReelIdentifier(node: AccessibilityNodeInfo): String? {
        val contentDesc = node.contentDescription?.toString()
        if (contentDesc != null && contentDesc.isNotBlank() &&
            (contentDesc.contains("Reel by") || contentDesc.contains("Audio"))
        ) {
            return contentDesc
        }

        val text = node.text?.toString()
        if (text != null && text.isNotBlank() && text.startsWith("@")) {
            return text
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            val found = findReelIdentifier(child)
            if (found != null) return found
        }

        return null
    }

    private fun goToHomeScreen() {
        // Send Android System HOME action (simply goes to home screen without app termination)
        performGlobalAction(GLOBAL_ACTION_HOME)
    }

    private fun incrementBlockedCount() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val count = prefs.getInt(KEY_BLOCKED_COUNT, 0) + 1
        prefs.edit().putInt(KEY_BLOCKED_COUNT, count).apply()
    }

    override fun onInterrupt() {}
}
