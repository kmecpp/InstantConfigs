package com.kmecpp.effortlessconfigs.api;

@FunctionalInterface
public interface Deserializer<T> {

	T deserialize(String str);

}
