package de.kaleidox.discordemoji.model;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import de.kaleidox.discordemoji.DiscordEmoji;
import de.kaleidox.util.PromisedValue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Contract;

/**
 * Representation of an Emoji
 */
public class Emoji {
    private static final Map<Integer, Emoji> cache = new HashMap<>();

    @JsonProperty(required = true) private int id;
    @JsonProperty(required = true) private String title;
    @JsonProperty private String slug;
    @JsonProperty(value = "image", required = true) private URL imageUrl;
    @JsonProperty private String description;
    @JsonProperty private int category;
    @JsonProperty private String license;
    @JsonProperty private String source;
    @JsonProperty private int faves;
    @JsonProperty("submitted_by") private String submittedBy;
    @JsonProperty private int width;
    @JsonProperty private int height;
    @JsonProperty private int filesize;

    private Emoji(JsonNode data) {
        cache.put(id, update(data));
    }

    /**
     * Gets the ID of the emoji.
     *
     * @return The ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the title of the emoji.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the URL slug of the emoji.
     *
     * @return The URL-Slug.
     */
    public String getSlug() {
        return slug;
    }

    /**
     * Gets the image URL of the emoji.
     *
     * @return The image URL.
     */
    public URL getImageURL() {
        return imageUrl;
    }

    /**
     * Gets the description of the emoji.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the category of the emoji.
     * <p>
     * To receive the {@linkplain PromisedValue#getPossibleValue() possible value}, you
     * must first {@linkplain DiscordEmoji#refreshEmojiCategoryCache() refresh the EmojiCategory-Cache}.
     *
     * @return The category.
     */
    public PromisedValue<Integer, EmojiCategory> getCategory() {
        return new PromisedValue<>(category, EmojiCategory::getByIndex);
    }

    /**
     * Gets the license of the emoji.
     *
     * @return The license.
     */
    public Optional<String> getLicense() {
        return license.isEmpty() || license.equals("0") ? Optional.empty() : Optional.of(license);
    }

    /**
     * Gets the source of the emoji.
     *
     * @return The source of the emoji.
     */
    public Optional<String> getSource() {
        return source.isEmpty() ? Optional.empty() : Optional.of(source);
    }

    /**
     * Gets the latest amount of faves of the emoji.
     * To update this value, you must {@linkplain DiscordEmoji#refreshEmojiCache() refresh the Emoji-Cache}.
     *
     * @return The amount of faves.
     */
    public int getFaves() {
        return faves;
    }

    /**
     * Gets the name of the person who submitted the emoji.
     *
     * @return The submitter name.
     */
    public String getSubmittedBy() {
        return submittedBy;
    }

    /**
     * Gets the width of the emoji image.
     *
     * @return The width of the image.
     */
    public OptionalInt getWidth() {
        return width == 0 ? OptionalInt.empty() : OptionalInt.of(width);
    }

    /**
     * Gets the height of the emoji image.
     *
     * @return The height of the image.
     */
    public OptionalInt getHeight() {
        return height == 0 ? OptionalInt.empty() : OptionalInt.of(height);
    }

    /**
     * Gets the file size of the emoji image.
     *
     * @return The file size of the emoji image.
     */
    public OptionalInt getFilesize() {
        return filesize == 0 ? OptionalInt.empty() : OptionalInt.of(filesize);
    }

    @Contract("!null -> this; null -> fail")
    private Emoji update(JsonNode data) {
        String str;

        try {
            this.id = data.path("id").asInt(id);
            this.title = data.path("title").asText(title);
            this.slug = data.path("slug").asText(slug);
            this.imageUrl = (str = data.path("image").asText())
                    .equals(imageUrl == null ? null : imageUrl.toExternalForm())
                    ? imageUrl
                    : new URL(str);
            this.description = data.path("description").asText(description);
            this.category = data.path("category").asInt(category);
            this.license = data.path("license").asText(license);
            this.source = data.path("source").asText(source);
            this.faves = data.path("faves").asInt(faves);
            this.submittedBy = data.path("submitted_by").asText(submittedBy);
            this.width = data.path("width").asInt(width);
            this.height = data.path("height").asInt(height);
            this.filesize = data.path("filesize").asInt(filesize);
        } catch (Throwable e) {
            throw new RuntimeException(toString() + " update exception", e);
        }

        return this;
    }

    /**
     * Gets any Emoji from the cache by its ID.
     * Before ever calling this method, you must {@linkplain DiscordEmoji#refreshEmojiCache() refresh the Emoji-Cache}.
     *
     * @param id The ID of the Emoji to get.
     *
     * @return The emoji.
     */
    public static Optional<Emoji> getByID(int id) {
        return (cache.size() > 2000
                ? cache.entrySet().parallelStream()
                : cache.entrySet().stream())
                .filter(entry -> entry.getKey() == id)
                .findFirst()
                .map(Map.Entry::getValue);
    }

    /**
     * Refreshes the emoji cache and tries to return the emoji with the given ID.
     * If no such emoji could be found, the returned future completes
     * {@linkplain CompletableFuture#exceptionally(Function) exceptionally} with a {@link NoSuchElementException}.
     *
     * @param id The ID of the Emoji to get.
     *
     * @return The emoji.
     */
    public static CompletableFuture<Emoji> requestByID(int id) {
        return DiscordEmoji.refreshEmojiCache()
                .thenApply(emojis -> getByID(id))
                .thenApply(emoji -> emoji.orElseThrow(() ->
                        new NoSuchElementException("No emoji with ID [" + id + "] was found!")));
    }

    /**
     * Internal method.
     * You should never call this method yourself.
     */
    public static Emoji getOrCreate(JsonNode data) {
        return cache.compute(data.get("id").asInt(), (id, emoji) -> emoji == null ? new Emoji(data) : emoji.update(data));
    }
}
