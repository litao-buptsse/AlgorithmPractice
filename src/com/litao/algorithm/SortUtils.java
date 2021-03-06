package com.litao.algorithm;

import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

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

    static class MergeSortTask<T> extends RecursiveAction {
        private T[] tmpArray;
        private T[] array;
        private int left;
        private int right;
        private Comparator<T> comparator;

        public MergeSortTask(T[] tmpArray, T[] array, int left, int right, Comparator<T> comparator) {
            this.tmpArray = tmpArray;
            this.array = array;
            this.left = left;
            this.right = right;
            this.comparator = comparator;
        }

        @Override
        protected void compute() {
            if (left < right) {
                int middle = (left + right) / 2;
                MergeSortTask<T> leftTask = new MergeSortTask<T>(tmpArray, array, left, middle, comparator);
                leftTask.fork();
                MergeSortTask<T> rightTask = new MergeSortTask<T>(tmpArray, array, middle + 1, right, comparator);
                rightTask.fork();

                leftTask.join();
                rightTask.join();
                merge(tmpArray, array, left, middle, right, comparator);
            }
        }
    }

    public static <T> void mergeSort(T[] array, boolean parallel) {
        mergeSort(array, null, parallel);
    }

    public static <T> void mergeSort(T[] array, Comparator<T> comparator, boolean parallel) {
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
        if (parallel) {
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            MergeSortTask<T> task = new MergeSortTask<T>(tmpArray, array, 0, array.length - 1, c);
            forkJoinPool.invoke(task);
        } else {
            mergeSort(tmpArray, array, 0, array.length - 1, c);
        }
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

    static class QuickSortTask<T> extends RecursiveAction {
        private T[] array;
        private int left;
        private int right;
        private Comparator<T> comparator;

        public QuickSortTask(T[] array, int left, int right, Comparator<T> comparator) {
            this.array = array;
            this.left = left;
            this.right = right;
            this.comparator = comparator;
        }

        @Override
        protected void compute() {
            if (left < right) {
                int pivot = partitions(array, left, right, comparator);
                QuickSortTask<T> leftTask = new QuickSortTask<T>(array, left, pivot - 1, comparator);
                leftTask.fork();
                QuickSortTask<T> rightTask = new QuickSortTask<T>(array, pivot + 1, right, comparator);
                rightTask.fork();

                leftTask.join();
                rightTask.join();
            }
        }
    }

    public static <T> void quickSort(T[] array, boolean parallel) {
        quickSort(array, null, parallel);
    }

    public static <T> void quickSort(T[] array, Comparator<T> comparator, boolean parallel) {
        if (array == null) {
            return;
        }

        Comparator c;
        if (comparator == null) {
            c = InternalComparator.INSTANCE;
        } else {
            c = comparator;
        }

        if (parallel) {
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            QuickSortTask<T> task = new QuickSortTask<T>(array, 0, array.length - 1, c);
            forkJoinPool.invoke(task);
        } else {
            quickSort(array, 0, array.length - 1, c);
        }
    }

    private static <T> void quickSort(T[] array, int left, int right, Comparator<T> comparator) {
        if (left < right) {
            int pivot = partitions(array, left, right, comparator);
            quickSort(array, left, pivot - 1, comparator);
            quickSort(array, pivot + 1, right, comparator);
        }
    }

    private static <T> int partitions(T[] array, int left, int right, Comparator<T> comparator) {
        int i = left;
        int j = right;
        T tmp = array[i];

        while (i < j) {
            while (i < j && comparator.compare(array[j], tmp) >= 0) {
                j--;
            }
            if (i < j) {
                array[i] = array[j];
                i++;
            }
            while (i < j && comparator.compare(array[i], tmp) < 0) {
                i++;
            }
            if (i < j) {
                array[j] = array[i];
                j--;
            }
        }
        array[i] = tmp;

        return i;
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
        // SortUtils.mergeSort(users, false);
        // SortUtils.mergeSort(users, true);
        // SortUtils.quickSort(users, false);
        SortUtils.quickSort(users, true);
        for (User user : users) {
            System.out.println(user);
        }
    }
}
