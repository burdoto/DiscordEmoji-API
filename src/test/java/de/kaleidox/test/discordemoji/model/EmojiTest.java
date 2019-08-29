package de.kaleidox.test.discordemoji.model;

import java.util.Collection;

import de.kaleidox.discordemoji.DiscordEmoji;
import de.kaleidox.discordemoji.model.Emoji;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class EmojiTest {
    @Test
    public void testRequestAndDeserialize() {
        Collection<Emoji> emojis = DiscordEmoji.refreshEmojiCache().join();

        assertTrue(emojis.parallelStream().noneMatch(emoji -> emoji.getImageURL() == null));

        System.out.println("emojis.size() = " + emojis.size());
    }
}
