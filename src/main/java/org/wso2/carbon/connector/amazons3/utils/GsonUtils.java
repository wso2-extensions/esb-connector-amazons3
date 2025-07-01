package org.wso2.carbon.connector.amazons3.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.Instant;

/**
 * Utility class for creating Gson instances with proper type adapters for Java time objects.
 */
public class GsonUtils {

    /**
     * Creates a Gson instance with custom type adapters for Java time objects.
     * This resolves the reflection access issues with java.time.Instant in newer Java versions.
     *
     * @return Configured Gson instance
     */
    public static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                .create();
    }

    /**
     * Custom type adapter for java.time.Instant to handle serialization/deserialization
     * without relying on reflection access to private fields.
     */
    private static class InstantTypeAdapter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

        @Override
        public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) {
                return null;
            }
            // Serialize as ISO-8601 string or as an object with seconds and nanos
            // Based on your schema, it seems you want the seconds/nanos format
            return context.serialize(new InstantWrapper(src.getEpochSecond(), src.getNano()));
        }

        @Override
        public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (json == null || json.isJsonNull()) {
                return null;
            }

            if (json.isJsonPrimitive()) {
                // Handle string representation (ISO-8601)
                return Instant.parse(json.getAsString());
            } else if (json.isJsonObject()) {
                // Handle object representation with seconds and nanos
                com.google.gson.JsonObject obj = json.getAsJsonObject();
                long seconds = obj.get("seconds").getAsLong();
                int nanos = obj.has("nanos") ? obj.get("nanos").getAsInt() : 0;
                return Instant.ofEpochSecond(seconds, nanos);
            }

            throw new JsonParseException("Unable to deserialize Instant from: " + json);
        }
    }

    /**
     * Wrapper class for Instant serialization with seconds and nanos structure
     * to match your schema format.
     */
    @SuppressWarnings("unused") // Methods are used by Gson for serialization
    private static class InstantWrapper {
        private final long seconds;
        private final int nanos;

        public InstantWrapper(long seconds, int nanos) {
            this.seconds = seconds;
            this.nanos = nanos;
        }

        public long getSeconds() {
            return seconds;
        }

        public int getNanos() {
            return nanos;
        }
    }
}
