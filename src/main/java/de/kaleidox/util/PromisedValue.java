package de.kaleidox.util;

import java.util.Optional;
import java.util.function.Function;

import de.kaleidox.discordemoji.model.Emoji;

import org.jetbrains.annotations.Nullable;

/**
 * Wrapper class for a value that might not be available yet but might be available later.
 * <p>
 * A value of type {@code P} is always available (thus; promised).
 * <p>
 * Used by {@link Emoji#getCategory()}.
 *
 * @param <P> Type-variable for the promised value.
 * @param <T> Type-variable for the possibly existing value.
 */
public final class PromisedValue<P, T> {
    private final P promisedValue;
    private final NullableFunction<P, T> mapper;

    private @Nullable T possibleValue = null;

    /**
     * Constructor.
     *
     * @param promisedValue The promised value. This will be used to fetch the possible value later.
     * @param mapper        A function to determine the possible value based on the defined promised value.
     */
    public PromisedValue(P promisedValue, NullableFunction<P, T> mapper) {
        this.promisedValue = promisedValue;
        this.mapper = mapper;
    }

    /**
     * Returns the promised value.
     *
     * @return The promised value.
     */
    public P getPromisedValue() {
        return promisedValue;
    }

    /**
     * Tries to fetch the possible value using the provided {@code mapper}.
     * <p>
     * If the mapper also returns null, the returned Optional is empty.
     * Although, it might succeed on the next try.
     * Once the Optional returns non-empty, the possible value remains the same.
     *
     * @return The possible value.
     */
    public Optional<T> getPossibleValue() {
        return possibleValue == null
                ? Optional.ofNullable(possibleValue = (mapper.apply(promisedValue)))
                : Optional.of(possibleValue);
    }

    /**
     * Supertype of {@link Function} with a {@link Nullable} annotation for IntelliJ code inspection.
     *
     * @param <P> Type-variable for the promised value.
     * @param <T> Type-variable for the possibly existing value.
     */
    public interface NullableFunction<P, T> extends Function<P, T> {
        @Override
        @Nullable
        T apply(P promisedValue);
    }
}
