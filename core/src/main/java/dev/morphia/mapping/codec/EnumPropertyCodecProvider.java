/*
 * Copyright 2008-present MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.morphia.mapping.codec;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PropertyCodecProvider;
import org.bson.codecs.pojo.PropertyCodecRegistry;
import org.bson.codecs.pojo.TypeWithTypeParameters;

final class EnumPropertyCodecProvider implements PropertyCodecProvider {
    private final CodecRegistry codecRegistry;

    EnumPropertyCodecProvider(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T> Codec<T> get(TypeWithTypeParameters<T> type, PropertyCodecRegistry propertyCodecRegistry) {
        Class<T> clazz = type.getType();
        if (Enum.class.isAssignableFrom(clazz)) {
            try {
                return codecRegistry.get(clazz);
            } catch (CodecConfigurationException e) {
                return (Codec<T>) new EnumCodec(clazz);
            }
        }
        return null;
    }

}
