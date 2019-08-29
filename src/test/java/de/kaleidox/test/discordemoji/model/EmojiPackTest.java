package de.kaleidox.test.discordemoji.model;

import java.util.Collection;

import de.kaleidox.discordemoji.DiscordEmoji;
import de.kaleidox.discordemoji.model.EmojiPack;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class EmojiPackTest {
    @Test
    public void testRequestAndDeserialize() {
        Collection<EmojiPack> packs = DiscordEmoji.refreshEmojiPackCache().join();

        assertTrue(packs.parallelStream().noneMatch(pack -> pack.getImageUrl() == null));
        assertTrue(packs.parallelStream().noneMatch(pack -> pack.getDownloadUrl() == null));

        System.out.println("packs.size() = " + packs.size());
    }
}
