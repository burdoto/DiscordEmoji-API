package de.kaleidox.discordemoji.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.discordemoji.DiscordEmoji;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Representation of an EmojiCategory.
 */
public class EmojiCategory {
    private static final List<EmojiCategory> cache = new ArrayList<>();

    private final String name;
    private final int index;

    private EmojiCategory(String name, int index) {
        this.name = name;
        this.index = index;

        update(index);
    }

    /**
     * Gets the name of the emoji category.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the index of the emoji category.
     *
     * @return The index.
     */
    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EmojiCategory
                && ((EmojiCategory) obj).name.equals(name);
    }

    @Override
    public String toString() {
        return String.format("EmojiCategory(name:%s)", name);
    }

    @Contract("_ -> this")
    private EmojiCategory update(int newIndex) {
        if (cache.size() > newIndex) {
            EmojiCategory oldValue = cache.get(newIndex);
            int oldIndex = cache.indexOf(this);

            cache.set(newIndex, this);

            if (oldIndex == -1)
                cache.add(oldValue);
            else
                cache.set(oldIndex, oldValue);
        } else cache.add(this);

        return this;
    }

    /**
     * Gets any EmojiCategory from the cache by its index, or null if the given index could not be found.
     *
     * @param index The index.
     *
     * @return The category, or null if the index could not be found.
     */
    public static @Nullable EmojiCategory getByIndex(int index) {
        return cache.get(index);
    }

    /**
     * Refreshes the emoji-category cache and tries to return the category with the given ID.
     * If no such category could be found, the returned future completes with {@code null}.
     *
     * @param index The ID of the EmojiCategory to get.
     *
     * @return The category, or null if the index could not be found.
     */
    public static CompletableFuture<EmojiCategory> requestByIndex(int index) {
        return DiscordEmoji.refreshEmojiCache()
                .thenApply(categories -> getByIndex(index));
    }

    private static EmojiCategory getOrCreate(JsonNode data, int index) {
        EmojiCategory cat;

        if (cache.size() > index && (cat = cache.get(index)) != null)
            return cat.update(index);
        else return new EmojiCategory(data.asText(), index);
    }
}
