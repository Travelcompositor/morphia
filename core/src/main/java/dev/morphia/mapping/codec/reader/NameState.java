package dev.morphia.mapping.codec.reader;

import dev.morphia.annotations.internal.MorphiaInternal;

import org.bson.BsonType;

/**
 * @morphia.internal
 */
@MorphiaInternal
public class NameState extends ReaderState {

    public static final String NAME = "NAME";
    private final String name;

    NameState(DocumentReader reader, String name) {
        super(reader);
        this.name = name;
    }

    @Override
    void skipName() {
        advance();
    }

    @Override
    BsonType getCurrentBsonType() {
        ReaderState readerState = nextState();
        return readerState != null
                ? readerState.getCurrentBsonType()
                : BsonType.UNDEFINED;
    }

    @Override
    String getStateName() {
        return NAME;
    }

    @Override
    String name() {
        advance();
        return name;
    }
}
