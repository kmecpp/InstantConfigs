package com.kmecpp.effortlessconfigs.api;

import java.beans.Transient;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import com.kmecpp.effortlessconfigs.Block;
import com.kmecpp.effortlessconfigs.ConfigData;
import com.kmecpp.effortlessconfigs.ConfigField;

public class Configs {

	private static final HashMap<Class<?>, ConfigData> configs = new HashMap<>();

	public static void load(Class<?> config) throws FileNotFoundException {
		//		ConfigData data = getConfigData(config);
		//		Path path = Paths.get(data.getProperties().path());
		//		Yaml yaml = new Yaml();
		//		Map<String, Object> obj = yaml.load(new FileInputStream(path.toFile()));
	}

	public static void save(Class<?> config) {

	}

	public static ConfigData getConfigData(Class<?> config) {
		ConfigData data = configs.get(config);
		if (data == null) {
			ConfigProperties properties = config.getAnnotation(ConfigProperties.class);
			if (properties == null) {
				throw new IllegalArgumentException("Configuration class must be annotated with @" + ConfigProperties.class.getSimpleName());
			}
			data = new ConfigData(config, properties);
			loadFields(data.getRoot(), config.getDeclaredFields(), config.getDeclaredClasses(), data.getFields());
			configs.put(config, data);
		}
		return data;
	}

	private static void loadFields(Block block, Field[] declaredFields, Class<?>[] declaredClasses, HashMap<String, ConfigField> fields) {
		for (Field field : declaredFields) {
			field.setAccessible(true);
			//			Setting setting = field.getAnnotation(Setting.class);
			//			if (setting == null) {
			//				continue;
			//			} else

			if (field.isAnnotationPresent(Transient.class) || Modifier.isFinal(field.getModifiers())) {
				continue;
			} else if (!Modifier.isStatic(field.getModifiers())) {
				System.err.println("Invalid configuration setting! Must be declared static: " + field);
				continue;
			}

			ConfigField configField = new ConfigField(field, field.getAnnotation(Setting.class));
			String rawKey = block.getPath().isEmpty() ? configField.getName() : block.getPath() + "." + configField.getName();
			fields.put(getKey(rawKey), configField);
			block.addField(configField);
		}

		for (Class<?> nested : declaredClasses) {
			if (nested.isAnnotationPresent(Ignore.class)) {
				continue;
			}
			Field[] nestedFields = nested.getDeclaredFields();
			Class<?>[] nestedClasses = nested.getDeclaredClasses();
			loadFields(block.createChild(getKey(nested.getSimpleName())), nestedFields, nestedClasses, fields);
		}
	}

	private static final StringBuilder sb = new StringBuilder();

	public static String getKey(String key) {
		sb.setLength(0);
		writeKey(sb, key);
		return sb.toString();
	}

	public static void writeKey(StringBuilder sb, String key) {
		for (int i = 0; i < key.length(); i++) {
			char c = key.charAt(i);
			if (Character.isUpperCase(c)) {
				if (i > 0) {
					sb.append('-');
				}
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
	}

}
