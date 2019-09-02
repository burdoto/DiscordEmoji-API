package de.kaleidox.discordemoji;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import de.kaleidox.discordemoji.model.Emoji;
import de.kaleidox.discordemoji.model.EmojiCategory;
import de.kaleidox.discordemoji.model.EmojiPack;
import de.kaleidox.discordemoji.model.PageStats;
import de.kaleidox.discordemoji.rest.Endpoint;
import de.kaleidox.discordemoji.rest.RestRequestHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Facade class for communicating with https://discordemoji.com/
 */
public final class DiscordEmoji {
    private static final ObjectMapper objectMapper;
    private static final Map<Class, Method> getOrCreateMethodMap;

    static {
        objectMapper = new ObjectMapper();
        getOrCreateMethodMap = new ConcurrentHashMap<>();

        try {
            getOrCreateMethodMap.put(Emoji.class,
                    Emoji.class.getDeclaredMethod("getOrCreate", JsonNode.class));
            getOrCreateMethodMap.put(EmojiPack.class,
                    EmojiPack.class.getDeclaredMethod("getOrCreate", JsonNode.class));
            getOrCreateMethodMap.put(EmojiCategory.class,
                    EmojiCategory.class.getDeclaredMethod("getOrCreate", JsonNode.class, int.class));

            getOrCreateMethodMap.values().forEach(method -> method.setAccessible(true));
        } catch (Throwable t) {
            throw new RuntimeException("Initialization Exception", t);
        }
    }

    private DiscordEmoji() {
        // nope
    }

    /**
     * Refreshes the internal cache of Emojis and returns a collection of all cached emojis when done.
     *
     * @return A future completing with all cached Emojis.
     */
    public static CompletableFuture<Collection<Emoji>> refreshEmojiCache() {
        return RestRequestHelper.get(Endpoint.LIST_ALL_EMOJIS)
                .thenApply(DiscordEmoji::mapNode_rethrow)
                .thenApply(node -> {
                    Collection<Emoji> yields = new ArrayList<>();

                    for (JsonNode emoji : node)
                        yields.add(accessCache_rethrow(Emoji.class, emoji));

                    return yields;
                });
    }

    /**
     * Refreshes the internal cache of EmojiPacks and returns a collection of all cached emoji packs when done.
     *
     * @return A future completing with all cached EmojiPacks.
     */
    public static CompletableFuture<Collection<EmojiPack>> refreshEmojiPackCache() {
        return RestRequestHelper.get(Endpoint.LIST_ALL_PACKS)
                .thenApply(DiscordEmoji::mapNode_rethrow)
                .thenApply(node -> {
                    Collection<EmojiPack> yields = new ArrayList<>();

                    for (JsonNode emojiPack : node)
                        yields.add(accessCache_rethrow(EmojiPack.class, emojiPack));

                    return yields;
                });
    }

    /**
     * Refreshes the internal cache of EmojiCategories and returns a collection of all cached emoji categories when
     * done.
     *
     * @return A future completing with all cached EmojiCategories.
     */
    public static CompletableFuture<Collection<EmojiCategory>> refreshEmojiCategoryCache() {
        return RestRequestHelper.get(Endpoint.LIST_ALL_CATEGORIES)
                .thenApply(DiscordEmoji::mapNode_rethrow)
                .thenApply(node -> {
                    Collection<EmojiCategory> yields = new ArrayList<>();

                    for (int i = 0; i < node.size(); i++)
                        yields.add(accessCache_rethrow(EmojiCategory.class, node, i));

                    return yields;
                });
    }

    /**
     * Requests the current stats of https://discordemoji.com/.
     *
     * @return A future completing with a new PageStats object.
     */
    public static CompletableFuture<PageStats> requestPageStats() {
        return RestRequestHelper.get(Endpoint.LIST_WEBSITE_STATS)
                .thenApply(data -> mapObject_rethrow(data, PageStats.class));
    }

    private static <T> T mapObject_rethrow(String data, Class<T> type) throws RuntimeException {
        try {
            return objectMapper.readValue(data, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON Deserialization Exception", e);
        }
    }

    private static JsonNode mapNode_rethrow(String data) throws RuntimeException {
        try {
            return objectMapper.readTree(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON Deserialization Exception", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T accessCache_rethrow(Class<T> type, Object... args) {
        try {
            return (T) getOrCreateMethodMap.get(type)
                    .invoke(null, args);
        } catch (Throwable t) {
            throw new RuntimeException("ReflectiveOperationException", t);
        }
    }
}
