package de.kaleidox.test.discordemoji.model;

import java.util.Collection;

import de.kaleidox.discordemoji.DiscordEmoji;
import de.kaleidox.discordemoji.model.EmojiCategory;

import org.junit.Test;

public class EmojiCategoryTest {
    @Test
    public void testRequestAndDeserialize() {
        Collection<EmojiCategory> categories = DiscordEmoji.refreshEmojiCategoryCache().join();

        //System.out.println("categories = " + categories);
        System.out.println("categories.size() = " + categories.size());
    }
}
