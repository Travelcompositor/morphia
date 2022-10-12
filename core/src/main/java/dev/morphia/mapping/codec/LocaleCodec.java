package dev.morphia.mapping.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.Locale;

/**
 * Converts a Locale to/from a valid database structure.
 */
public class LocaleCodec implements Codec<Locale> {
    @Override
    public Locale decode(BsonReader reader, DecoderContext decoderContext) {
        return parseLocale(reader.readString());
    }

    @Override
    public void encode(BsonWriter writer, Locale value, EncoderContext encoderContext) {
        if (value == null) {
            writer.writeNull();
        } else {
            writer.writeString(value.toString());
        }
    }

    @Override
    public Class<Locale> getEncoderClass() {
        return Locale.class;
    }

    Locale parseLocale(String localeString) {
        final int index = localeString.indexOf("_");
        final int index2 = localeString.indexOf("_", index + 1);
        Locale resultLocale;
        if (index == -1) {
            resultLocale = new Locale(localeString);
        } else if (index2 == -1) {
            resultLocale = new Locale(localeString.substring(0, index), localeString.substring(index + 1));
        } else {
            resultLocale = new Locale(
                    localeString.substring(0, index),
                    localeString.substring(index + 1, index2),
                    localeString.substring(index2 + 1));

        }
        return resultLocale;
    }
}
