package dev.morphia.aggregation.codecs.stages;

import dev.morphia.Datastore;
import dev.morphia.aggregation.codecs.ExpressionHelper;
import dev.morphia.aggregation.stages.Lookup;
import dev.morphia.aggregation.stages.Stage;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;

import java.util.List;

import static dev.morphia.aggregation.codecs.ExpressionHelper.array;
import static dev.morphia.aggregation.codecs.ExpressionHelper.document;

public class LookupCodec extends StageCodec<Lookup> {

    public LookupCodec(Datastore datastore) {
        super(datastore);
    }

    @Override
    public Class<Lookup> getEncoderClass() {
        return Lookup.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void encodeStage(BsonWriter writer, Lookup value, EncoderContext encoderContext) {
        document(writer, () -> {
            if (value.getFrom() != null) {
                writer.writeString("from", value.getFrom());
            } else if (value.getFromType() != null) {
                writer.writeString("from", getDatastore().getMapper().getEntityModel(value.getFromType()).getCollectionName());
            }

            if (value.getLocalField() != null) {
                writer.writeString("localField", value.getLocalField());
            }
            if (value.getForeignField() != null) {
                writer.writeString("foreignField", value.getForeignField());
            }
            writer.writeString("as", value.getAs());
            List<Stage> pipeline = value.getPipeline();
            if (pipeline != null) {
                ExpressionHelper.expression(getDatastore(), writer, "let", value.getVariables(), encoderContext);
                array(writer, "pipeline", () -> {
                    for (Stage stage : pipeline) {
                        Codec<Stage> codec = (Codec<Stage>) getCodecRegistry().get(stage.getClass());
                        codec.encode(writer, stage, encoderContext);
                    }
                });
            }
        });
    }
}
