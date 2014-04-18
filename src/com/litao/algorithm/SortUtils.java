package com.litao.algorithm;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Tao Li on 4/19/14.
 */
public class SortUtils {
	static class InternalComparator implements Comparator {
		public static InternalComparator INSTANCE = new InternalComparator();

		@Override
		public int compare(Object o1, Object o2) {
			return ((Comparable) o1).compareTo((Comparable) o2);
		}
	}

	private static <T> void swap(T[] array, int i, int j) {
		T tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}

	public static <T> void bubbleSort(T[] array) {
		bubbleSort(array, null);
	}

	public static <T> void bubbleSort(T[] array, Comparator<T> comparator) {
		if (array == null) {
			return;
		}

		Comparator c;
		if (comparator == null) {
			c = InternalComparator.INSTANCE;
		} else {
			c = comparator;
		}

		for (int i = array.length - 1; i >= 0; i--) {
			for (int j = 0; j + 1 <= i; j++) {
				if (c.compare(array[j], array[j + 1]) > 0) {
					swap(array, j, j + 1);
				}
			}
		}
	}

	static class User implements Comparable<User> {
		private int age;

		public User(int age) {
			this.age = age;
		}

		@Override
		public int compareTo(User user) {
			if (user == null) {
				throw new NullPointerException("compareTo param is null");
			}

			if (this.age > user.age) {
				return 1;
			} else if (this.age == user.age) {
				return 0;
			} else {
				return -1;
			}
		}

		@Override
		public String toString() {
			return "User{" +
					"age=" + age +
					'}';
		}

	}

	public static void main(String[] args) {
		User[] users = {new User(4), new User(1), new User(5), new User(3), new User(2)};
		// Arrays.sort(users);
		SortUtils.bubbleSort(users);
		for (User user : users) {
			System.out.println(user);
		}
	}
}
