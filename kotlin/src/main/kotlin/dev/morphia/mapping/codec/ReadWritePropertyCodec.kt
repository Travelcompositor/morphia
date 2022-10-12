package dev.morphia.mapping.codec

import kotlin.properties.ReadWriteProperty
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext

@Suppress("UNCHECKED_CAST")
class ReadWritePropertyCodec<T>(private val codec: Codec<Any>) : Codec<Any> {
    override fun getEncoderClass(): Class<Any> = ReadWriteProperty::class.java as Class<Any>
    override fun encode(writer: BsonWriter, value: Any, context: EncoderContext) {
        context.encodeWithChildContext(codec, writer, value)
    }

    override fun decode(reader: BsonReader, context: DecoderContext): Any? {
        return codec.decode(reader, context)
    }
}
