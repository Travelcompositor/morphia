package dev.morphia.mapping.codec.reader;

import java.util.List;

import org.bson.BsonType;

import static java.util.stream.Collectors.toList;

class ArrayState extends ReaderState {
    public static final String NAME = "ARRAY";
    private final List<?> values;
    private ArrayEndState endState;

    ArrayState(DocumentReader reader, List<?> values) {
        super(reader);
        this.values = values;
    }

    @Override
    void skipValue() {
        reader().state(endState != null ? endState.nextState() : nextState());
    }

    @Override
    BsonType getCurrentBsonType() {
        return BsonType.ARRAY;
    }

    @Override
    String getStateName() {
        return NAME;
    }

    void startArray() {
        if (endState == null) {
            List<ReaderState> states = values.stream()
                    .map(this::valueState)
                    .collect(toList());
            ReaderState docState = null;
            for (ReaderState state : states) {
                if (docState != null) {
                    docState.next(state);
                } else {
                    next(state);
                }
                docState = state;
            }

            endState = new ArrayEndState(reader());
            if (docState != null) {
                docState.next(endState);
            } else {
                next(endState);
            }

        }
        advance();
    }

}
