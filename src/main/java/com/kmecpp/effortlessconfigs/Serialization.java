package com.kmecpp.effortlessconfigs;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

import com.kmecpp.effortlessconfigs.api.Deserializer;
import com.kmecpp.effortlessconfigs.api.Serializer;

public class Serialization {

	private static final HashMap<Class<?>, SerializationData<?>> types = new HashMap<>();

	static {
		registerDefaultType(byte.class, Byte::parseByte);
		registerDefaultType(short.class, Short::parseShort);
		registerDefaultType(int.class, Integer::parseInt);
		registerDefaultType(long.class, Long::parseLong);
		registerDefaultType(float.class, Float::parseFloat);
		registerDefaultType(double.class, Double::parseDouble);
		registerDefaultType(boolean.class, Boolean::parseBoolean);

		registerDefaultType(Byte.class, Byte::parseByte);
		registerDefaultType(Short.class, Short::parseShort);
		registerDefaultType(Integer.class, Integer::parseInt);
		registerDefaultType(Long.class, Long::parseLong);
		registerDefaultType(Float.class, Float::parseFloat);
		registerDefaultType(Double.class, Double::parseDouble);
		registerDefaultType(Boolean.class, Boolean::parseBoolean);

		registerDefaultType(byte[].class, Arrays::toString, (s) -> get(byte.class, s, Byte::parseByte));
		registerDefaultType(short[].class, Arrays::toString, (s) -> get(short.class, s, Short::parseShort));
		registerDefaultType(int[].class, Arrays::toString, (s) -> get(int.class, s, Integer::parseInt));
		registerDefaultType(long[].class, Arrays::toString, (s) -> get(long.class, s, Long::parseLong));
		registerDefaultType(float[].class, Arrays::toString, (s) -> get(float.class, s, Float::parseFloat));
		registerDefaultType(double[].class, Arrays::toString, (s) -> get(double.class, s, Double::parseDouble));
		registerDefaultType(boolean[].class, Arrays::toString, (s) -> get(boolean.class, s, Boolean::parseBoolean));

		registerDefaultType(char.class, (s) -> s.charAt(0));
		registerDefaultType(char[].class, String::new, String::toCharArray);
		registerDefaultType(String.class, (obj) -> obj == null ? null : "\"" + obj + "\"", String::valueOf);

		//Not default type
		register(UUID.class, UUID::fromString);
	}

	@SuppressWarnings("unchecked")
	public static <T, C> T get(Class<C> componentType, String str, Function<String, C> deserializer) {
		String[] parts = str.split(",");
		Object[] result = (Object[]) Array.newInstance(componentType, parts.length);
		for (int i = 0; i < parts.length; i++) {
			result[i] = Integer.parseInt(parts[i]);
		}
		return (T) result;
	}

	@SuppressWarnings("unchecked")
	public static <T> SerializationData<T> getData(Class<T> cls) {
		return (SerializationData<T>) types.get(cls);
	}

	private static <T> void registerDefaultType(Class<T> cls, Deserializer<T> deserializer) {
		types.put(cls, new SerializationData<>(false, String::valueOf, deserializer));
	}

	private static <T> void registerDefaultType(Class<T> cls, Serializer<T> serializer, Deserializer<T> deserializer) {
		types.put(cls, new SerializationData<>(false, serializer, deserializer));
	}

	public static <T> void register(Class<T> cls, Deserializer<T> deserializer) {
		types.put(cls, new SerializationData<>(true, String::valueOf, deserializer));
	}

	public static <T> void register(Class<T> cls, Serializer<T> serializer, Deserializer<T> deserializer) {
		types.put(cls, new SerializationData<>(true, serializer, deserializer));
	}

	@SuppressWarnings("unchecked")
	public static <T> Deserializer<T> getDeserializer(Class<T> type) {
		SerializationData<T> data = (SerializationData<T>) types.get(type);
		return data != null ? data.getDeserializer() : null;
	}

	@SuppressWarnings("unchecked")
	public static <T> String serialize(T obj) {
		if (obj == null) {
			return "null";
		}

		SerializationData<T> data = (SerializationData<T>) types.get(obj.getClass());
		if (data != null) {
			return data.isCustomType() ? "\"" + data.serialize(obj) + "\"" : data.serialize(obj);
		}
		throw new IllegalArgumentException("Cannot serialize unregistered config type: " + obj.getClass());
		//		throw new IllegalArgumentException("Cannot serialize unknown class: " + obj.getClass().getName());
	}

	@SuppressWarnings("unchecked")
	public static <T> T deserialize(Class<T> type, String str) {
		initializeClass(type); //Make sure the class is loaded (they possibly registered it in a static initializer)

		if (str == null || str.equals("null")) {
			return null;
		}

		SerializationData<T> data = (SerializationData<T>) types.get(type);
		if (data != null) {
			return data.deserialize(str); //The parser will remove the quotations from custom types
			//			return data.deserialize(data.isCustomType() ? str.substring(1, str.length() - 1) : str);
		}
		throw new IllegalArgumentException("Cannot parse unregistered config type: " + type.getName());
	}

	private static void initializeClass(Class<?> cls) {
		//Make sure the class is loaded (they possibly registered it in a static initializer)
		if (cls.isPrimitive()) {
			return;
		}
		try {
			Class.forName(cls.getName(), true, cls.getClassLoader());
		} catch (ClassNotFoundException e) {
			throw new AssertionError(e);
		}
	}

}
