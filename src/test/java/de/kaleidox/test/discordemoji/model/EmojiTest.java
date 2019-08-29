package de.kaleidox.test.discordemoji.model;

import java.util.Collection;

import de.kaleidox.discordemoji.DiscordEmoji;
import de.kaleidox.discordemoji.model.Emoji;

import org.junit.Test;

public class EmojiTest {
    @Test
    public void testRequestAndDeserialize() {
        Collection<Emoji> emojis = DiscordEmoji.refreshEmojiCache().join();

        //System.out.println("emojis = " + emojis);
        System.out.println("emojis.size() = " + emojis.size());
    }
}
