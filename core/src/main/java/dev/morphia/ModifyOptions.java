package dev.morphia;

import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.lang.Nullable;
import dev.morphia.internal.SessionConfigurable;
import dev.morphia.internal.WriteConfigurable;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Defines the options for a "find and modify" operation.
 *
 * @since 2.0
 */
public class ModifyOptions extends FindOneAndUpdateOptions implements SessionConfigurable<ModifyOptions>,
                                                                          WriteConfigurable<ModifyOptions>,
                                                                          AlternateCollection<ModifyOptions> {
    private WriteConcern writeConcern;
    private ClientSession clientSession;
    private String collection;

    @Override
    public ModifyOptions clientSession(ClientSession clientSession) {
        this.clientSession = clientSession;
        return this;
    }

    @Override
    public ClientSession clientSession() {
        return clientSession;
    }

    @Override
    public ModifyOptions collection(String collection) {
        this.collection = collection;
        return this;
    }

    @Override
    public String collection() {
        return collection;
    }

    /**
     * @param hint the hint to apply
     * @return this
     * @see #hint(Bson)
     * @since 2.2
     */
    public ModifyOptions hint(Document hint) {
        super.hint(hint);
        return this;
    }

    @Override
    public ModifyOptions projection(@Nullable Bson projection) {
        super.projection(projection);
        return this;
    }

    @Override
    public ModifyOptions sort(@Nullable Bson sort) {
        super.sort(sort);
        return this;
    }

    @Override
    public ModifyOptions upsert(boolean upsert) {
        super.upsert(upsert);
        return this;
    }

    @Override
    public ModifyOptions returnDocument(ReturnDocument returnDocument) {
        super.returnDocument(returnDocument);
        return this;
    }

    @Override
    public ModifyOptions maxTime(long maxTime, TimeUnit timeUnit) {
        super.maxTime(maxTime, timeUnit);
        return this;
    }

    @Override
    public ModifyOptions bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
        super.bypassDocumentValidation(bypassDocumentValidation);
        return this;
    }

    @Override
    public ModifyOptions collation(@Nullable Collation collation) {
        super.collation(collation);
        return this;
    }

    @Override
    public ModifyOptions arrayFilters(@Nullable List<? extends Bson> arrayFilters) {
        super.arrayFilters(arrayFilters);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return this
     * @since 2.2
     */
    public ModifyOptions hint(@Nullable Bson hint) {
        super.hint(hint);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return this
     * @since 2.2
     */
    public ModifyOptions hintString(@Nullable String hint) {
        super.hintString(hint);
        return this;
    }

    /**
     * @param writeConcern the write concern
     * @return this
     */
    public ModifyOptions writeConcern(@Nullable WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
        return this;
    }

    /**
     * @return the write concern to use
     */
    @Nullable
    public WriteConcern writeConcern() {
        return writeConcern;
    }
}
