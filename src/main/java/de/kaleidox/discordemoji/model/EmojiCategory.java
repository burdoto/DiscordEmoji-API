package de.kaleidox.discordemoji.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

/**
 * Representation of an EmojiCategory.
 */
public class EmojiCategory {
    private static final Map<Integer, EmojiCategory> cache = new HashMap<>();

    private final String name;
    private final int index;

    private EmojiCategory(String name, int index) {
        this.name = name;
        this.index = index;

        cache.put(index, this);
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

    private EmojiCategory update(int newIndex) {
        ArrayList<Map.Entry<Integer, EmojiCategory>> entries = new ArrayList<>(cache.entrySet());

        Map.Entry<Integer, EmojiCategory> oldEntry = entries.get(newIndex);

        int oldIndex;
        for (oldIndex = 0; oldIndex < entries.size(); oldIndex++)
            if (entries.get(oldIndex).getValue().equals(oldEntry.getValue()))
                break;

        cache.put(newIndex, this);
        cache.put(oldIndex, oldEntry.getValue());

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
        return cache.getOrDefault(index, null);
    }

    /**
     * Internal method.
     * You should never call this method yourself.
     */
    public static EmojiCategory getOrCreate(JsonNode data, int index) {
        return cache.compute(index, (i, category) -> category == null
                ? new EmojiCategory(data.asText(), i)
                : category.update(i));
    }
}
