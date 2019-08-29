package de.kaleidox.discordemoji.rest;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Helper class for REST-Requests.
 */
public final class RestRequestHelper {
    private static final OkHttpClient client;

    static {
        client = new OkHttpClient.Builder()
                .build();
    }

    /**
     * Invokes a {@code GET}-Request on the provided {@link Endpoint}.
     *
     * @param endpoint The endpoint to {@code GET} from.
     *
     * @return A future completing with the {@linkplain ResponseBody#string() response body}.
     */
    public static CompletableFuture<String> get(Endpoint endpoint) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Request getRequest = new Request.Builder()
                        .method("GET", null)
                        .url(endpoint.url)
                        .build();

                Call call = client.newCall(getRequest);

                Response response = call.execute();

                ResponseBody body = response.body();

                return body == null ? "{}" : body.string();
            } catch (IOException e) {
                throw new RuntimeException("RequestException", e);
            }
        });
    }
}
