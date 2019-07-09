package com.kmecpp.effortlessconfigs;

import com.kmecpp.effortlessconfigs.api.Deserializer;
import com.kmecpp.effortlessconfigs.api.Serializer;

public class SerializationData<T> {

	private boolean customType;
	private Serializer<T> serializer;
	private Deserializer<T> deserializer;

	public SerializationData(boolean customType, Serializer<T> serializer, Deserializer<T> deserializer) {
		this.customType = customType;
		this.serializer = serializer;
		this.deserializer = deserializer;

	}

	public Serializer<T> getSerializer() {
		return serializer;
	}

	public Deserializer<T> getDeserializer() {
		return deserializer;
	}

	public boolean isCustomType() {
		return customType;
	}

	public String serialize(T obj) {
		return serializer.serialize(obj);
	}

	public T deserialize(String str) {
		return deserializer.deserialize(str);
	}

}
