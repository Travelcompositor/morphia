package dev.morphia.mapping.codec;

import java.util.HashMap;
import java.util.Map;

import dev.morphia.annotations.internal.MorphiaInternal;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * A provider of codecs for primitive types
 *
 * @morphia.internal
 */
@MorphiaInternal
@SuppressWarnings("unchecked")
public class PrimitiveCodecRegistry implements CodecRegistry {
    private final Map<Class, Codec> primitiveCodecs = new HashMap<>();

    /**
     * Creates the provider
     *
     * @param codecRegistry the registry
     */
    public PrimitiveCodecRegistry(CodecRegistry codecRegistry) {
        primitiveCodecs.put(byte.class, codecRegistry.get(Byte.class));
        primitiveCodecs.put(char.class, codecRegistry.get(Character.class));
        primitiveCodecs.put(short.class, codecRegistry.get(Short.class));
        primitiveCodecs.put(int.class, codecRegistry.get(Integer.class));
        primitiveCodecs.put(long.class, codecRegistry.get(Long.class));
        primitiveCodecs.put(float.class, codecRegistry.get(Float.class));
        primitiveCodecs.put(double.class, codecRegistry.get(Double.class));
        primitiveCodecs.put(boolean.class, codecRegistry.get(Boolean.class));
    }

    @Override
    public <T> Codec<T> get(Class<T> clazz) {
        return primitiveCodecs.get(clazz);
    }

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        return primitiveCodecs.get(clazz);
    }
}
