package de.kaleidox.discordemoji.rest;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * An enumeration of all endpoints provided by https://discordemoji.com/.
 * <p>
 * All endpoints are used with the {@code GET}-Method.
 */
public enum Endpoint {
    /**
     * The endpoint to request all available Emojis.
     */
    LIST_ALL_EMOJIS("https://emoji.gg/api"),

    /**
     * The endpoint to request all available EmojiPacks.
     */
    LIST_ALL_PACKS("https://emoji.gg/api/packs"),

    /**
     * The endpoint to request all available EmojiCategories.
     */
    LIST_ALL_CATEGORIES("https://emoji.gg/api?request=categories"),

    /**
     * The endpoint to request the current website stats.
     */
    LIST_WEBSITE_STATS("https://emoji.gg/api?request=stats");

    /**
     * The URL for this endpoint.
     */
    public final URL url;

    Endpoint(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Initialization Exception", e);
        }
    }
}
