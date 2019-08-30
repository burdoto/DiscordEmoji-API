package de.kaleidox.discordemoji.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
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
     * Internal method.
     * You should never call this method yourself.
     */
    public static EmojiCategory getOrCreate(JsonNode data, int index) {
        EmojiCategory cat;

        if (cache.size() > index && (cat = cache.get(index)) != null)
            return cat.update(index);
        else return new EmojiCategory(data.asText(), index);
    }
}
