package de.kaleidox.test.discordemoji.model;

import java.util.Collection;

import de.kaleidox.discordemoji.DiscordEmoji;
import de.kaleidox.discordemoji.model.EmojiPack;

import org.junit.Test;

public class EmojiPackTest {
    @Test
    public void testRequestAndDeserialize() {
        Collection<EmojiPack> packs = DiscordEmoji.refreshEmojiPackCache().join();

        //System.out.println("packs = " + packs);
        System.out.println("packs.size() = " + packs.size());
    }
}
