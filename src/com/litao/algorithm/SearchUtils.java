package com.litao.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tao Li on 4/17/14.
 */
public class SearchUtils {
	public static int binarySearch(List<Integer> list, int element) {
		if (list == null) {
			throw new NullPointerException("Param list is null!");
		}

		int left = 0;
		int right = list.size() - 1;
		int middle;

		while (left <= right) {
			middle = (right + left) / 2;
			int middleElement = list.get(middle);
			if (middleElement == element) {
				return middle;
			} else if (middleElement < element) {
				left = middle + 1;
			} else if (middleElement > element) {
				right = middle - 1;
			}
		}

		return -1;
	}

	public static void main(String[] args) {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
		System.out.println(binarySearch(list, 10));
	}
}
