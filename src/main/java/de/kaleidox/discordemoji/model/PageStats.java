package de.kaleidox.discordemoji.model;

import de.kaleidox.discordemoji.DiscordEmoji;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents page statistics.
 * Obtained via {@link DiscordEmoji#requestPageStats()}.
 */
public class PageStats {
    @JsonProperty("emoji") private int emojiCount;
    @JsonProperty("users") private int userCount;
    @JsonProperty("faves") private int totalFaves;
    @JsonProperty("pending_approvals") private int totalPendingApprovals;

    /**
     * Gets the total amount of emojis featured on the page.
     *
     * @return The total amount of emojis.
     */
    public int getEmojiCount() {
        return emojiCount;
    }

    /**
     * Gets the total amount of users on the page.
     *
     * @return The total amount of users.
     */
    public int getUserCount() {
        return userCount;
    }

    /**
     * Gets the total amount of faves on the page.
     *
     * @return The total amount of faves.
     */
    public int getTotalFaves() {
        return totalFaves;
    }

    /**
     * Gets the total amount of pending approvals on the page.
     *
     * @return The total amount of pending approvals.
     */
    public int getTotalPendingApprovals() {
        return totalPendingApprovals;
    }
}
