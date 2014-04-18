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

	public static <T> void mergeSort(T[] array) {
		mergeSort(array, null);
	}

	public static <T> void mergeSort(T[] array, Comparator<T> comparator) {
		if (array == null) {
			return;
		}

		Comparator c;
		if (comparator == null) {
			c = InternalComparator.INSTANCE;
		} else {
			c = comparator;
		}

		T[] tmpArray = array.clone();
		mergeSort(tmpArray, array, 0, array.length - 1, c);
	}

	private static <T> void mergeSort(T[] tmpArray, T[] array, int left, int right, Comparator<T> comparator) {
		if (left < right) {
			int middle = (left + right) / 2;
			mergeSort(tmpArray, array, left, middle, comparator);
			mergeSort(tmpArray, array, middle + 1, right, comparator);
			merge(tmpArray, array, left, middle, right, comparator);
		}
	}

	private static <T> void merge(T[] tmpArray, T[] array, int left, int middle, int right, Comparator<T> comparator) {
		int i = left;
		int j = middle + 1;
		int k = left;

		while (i <= middle && j <= right) {
			if (comparator.compare(array[i], array[j]) <= 0) {
				tmpArray[k++] = array[i++];
			} else {
				tmpArray[k++] = array[j++];
			}
		}

		while (i <= middle) {
			tmpArray[k++] = array[i++];
		}

		while (j <= right) {
			tmpArray[k++] = array[j++];
		}

		for (int m = left; m <= right; m++) {
			array[m] = tmpArray[m];
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
		// SortUtils.bubbleSort(users);
		SortUtils.mergeSort(users);
		for (User user : users) {
			System.out.println(user);
		}
	}
}
