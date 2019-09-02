package de.kaleidox.discordemoji.model;

import java.net.URL;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import de.kaleidox.discordemoji.DiscordEmoji;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Contract;

/**
 * Representation of an EmojiPack.
 */
public class EmojiPack {
    private static final Map<Integer, EmojiPack> cache = new ConcurrentHashMap<>();

    @JsonProperty(required = true) private int id;
    @JsonProperty(required = true) private String name;
    @JsonProperty private String description;
    @JsonProperty private String slug;
    @JsonProperty("image") private URL imageUrl;
    @JsonProperty(value = "download", required = true) private URL downloadUrl;
    @JsonProperty("amount") private int size;

    private EmojiPack(JsonNode data) {
        cache.put(id, update(data));
    }

    /**
     * Gets the ID of the emoji pack.
     *
     * @return The ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the emoji pack.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the emoji pack.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the URL-Slug of the emoji pack.
     *
     * @return The URL-Slug.
     */
    public String getSlug() {
        return slug;
    }

    /**
     * Gets the URL of the icon image of the emoji pack.
     *
     * @return The icon image URL.
     */
    public URL getImageUrl() {
        return imageUrl;
    }

    /**
     * Gets the download URL of the emoji pack.
     *
     * @return The download URL.
     */
    public URL getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * Gets the amount of emojis in this emoji pack.
     *
     * @return The amount of emojis.
     */
    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EmojiPack
                && ((EmojiPack) obj).id == id;
    }

    @Override
    public String toString() {
        return String.format("EmojiPack(id:%d,name:%s)", id, name);
    }

    @Contract("!null -> this; null -> fail")
    private EmojiPack update(JsonNode data) {
        String str;

        try {
            this.id = data.path("id").asInt(id);
            this.name = data.path("name").asText(name);
            this.description = data.path("description").asText(description);
            this.slug = data.path("slug").asText(slug);
            this.imageUrl = (str = data.path("image").asText())
                    .equals(imageUrl == null ? null : imageUrl.toExternalForm())
                    ? imageUrl
                    : new URL(str);
            this.downloadUrl = (str = data.path("download").asText())
                    .equals(downloadUrl == null ? null : downloadUrl.toExternalForm())
                    ? downloadUrl
                    : new URL(str);
            this.size = data.path("amount").asInt(size);
        } catch (Throwable e) {
            throw new RuntimeException(toString() + " Update Exception", e);
        }

        return this;
    }

    /**
     * Gets any EmojiPack from the cache by its ID.
     * Before ever calling this method, you must
     * {@linkplain DiscordEmoji#refreshEmojiPackCache() refresh the EmojiPack-Cache}.
     *
     * @param id The ID of the EmojiPack to get.
     *
     * @return The emoji pack.
     */
    public static Optional<EmojiPack> getByID(int id) {
        return (cache.size() > 2000
                ? cache.entrySet().parallelStream()
                : cache.entrySet().stream())
                .filter(entry -> entry.getKey() == id)
                .findFirst()
                .map(Map.Entry::getValue);
    }

    /**
     * Refreshes the emoji-pack cache and tries to return the pack with the given ID.
     * If no such pack could be found, the returned future completes
     * {@linkplain CompletableFuture#exceptionally(Function) exceptionally} with a {@link NoSuchElementException}.
     *
     * @param id The ID of the EmojiPack to get.
     *
     * @return The EmojiPack.
     */
    public static CompletableFuture<EmojiPack> requestByID(int id) {
        return DiscordEmoji.refreshEmojiCache()
                .thenApply(packs -> getByID(id))
                .thenApply(pack -> pack.orElseThrow(() ->
                        new NoSuchElementException("No pack with ID [" + id + "] was found!")));
    }

    private static EmojiPack getOrCreate(JsonNode data) {
        return cache.compute(data.get("id").asInt(), (id, pack) -> pack == null
                ? new EmojiPack(data)
                : pack.update(data));
    }
}
