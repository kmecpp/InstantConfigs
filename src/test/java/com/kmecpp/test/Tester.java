package com.kmecpp.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Tester {

	public static class TestConfig {

		public static byte x1;
		public static short x2;
		public static char x3;
		public static int x4;
		public static long x5;
		public static float z6;
		public static double z7;

		public static Byte y1;
		public static Short y2;
		public static Character y3;
		public static Integer y4;
		public static Long y5;
		public static Float y6;
		public static Double y7;
		public static String y8;
		public static UUID y9;

	}

	public static class TestConfig2 {

		public static ArrayList<String> x1;
		public static ArrayList<Integer> x2;
		public static ArrayList<UUID> x3;
		public static LinkedList<UUID> x4;
		public static HashSet<UUID> x5;
		public static Set<UUID> x6 = new HashSet<>();

	}

	public static class TestConfig3 {

		public static HashMap<String, Integer> x1;
		public static HashMap<Integer, UUID> x2;
		public static HashMap<String, Class<?>> x3;
		public static Map<String, Double> x4;

	}

}
