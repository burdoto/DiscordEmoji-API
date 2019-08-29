package de.kaleidox.test.discordemoji.model;

import de.kaleidox.discordemoji.DiscordEmoji;
import de.kaleidox.discordemoji.model.PageStats;

import org.junit.Test;

public class PageStatsTest {
    @Test
    public void testRequestAndDeserialize() {
        PageStats stats = DiscordEmoji.requestPageStats().join();

        System.out.println("stats.getEmojiCount() = " + stats.getEmojiCount());
        System.out.println("stats.getTotalFaves() = " + stats.getTotalFaves());
        System.out.println("stats.getTotalPendingApprovals() = " + stats.getTotalPendingApprovals());
        System.out.println("stats.getUserCount() = " + stats.getUserCount());
    }
}
