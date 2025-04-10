import java.util.Arrays;

public class ProblemSolutions {

    /******************************************************************
     *
     *   Alexander  / 001
     *
     *   This java file contains the problem solutions for the methods
     *   selectionSort, mergeSortDivisibleByKFirst, asteroidsDestroyed, and
     *   numRescueSleds methods.
     *
     ********************************************************************/

    /**
     * Method selectionSort
     *
     * This method performs a selection sort. Two function signatures are provided:
     * if the optional boolean is omitted, the sort defaults to ascending order.
     *
     * @param values    - int[] array to be sorted.
     */
    public void selectionSort(int[] values) {
        selectionSort(values, true);
    }

    /**
     * Method selectionSort
     *
     * Performs a selection sort in-place. For an ascending order sort, the algorithm
     * finds the smallest element in the unsorted portion, and for a descending order sort,
     * finds the largest element.
     *
     * @param values     - int[] array to be sorted.
     * @param ascending  - if true, performs ascending sort; otherwise, descending.
     */
    public static void selectionSort(int[] values, boolean ascending) {
        int n = values.length;
        // Loop over each position where a correct element should be placed
        for (int i = 0; i < n - 1; i++) {
            int targetIndex = i;
            // Find the smallest (or largest if descending) value in the unsorted section.
            for (int j = i + 1; j < n; j++) {
                if (ascending) {
                    if (values[j] < values[targetIndex]) {
                        targetIndex = j;
                    }
                } else { // descending order: choose the larger element.
                    if (values[j] > values[targetIndex]) {
                        targetIndex = j;
                    }
                }
            }
            // Swap the found element with the element at position i.
            int temp = values[i];
            values[i] = values[targetIndex];
            values[targetIndex] = temp;
        }
    } // End of selectionSort

    /**
     * Method mergeSortDivisibleByKFirst
     *
     * This method will perform a merge sort algorithm. However, all numbers that are divisible
     * by the argument 'k' are placed first in the sorted list. For numbers divisible by k, their
     * relative order (as in the original array) is maintained. The rest of the numbers are sorted
     * in ascending order.
     *
     * Example:
     *   values = { 10, 3, 25, 8, 6 }, k = 5  -->  {10, 25, 3, 6, 8}
     *   values = { 30, 45, 22, 9, 18, 39, 6, 12 }, k = 6  -->  {30, 18, 6, 12, 9, 22, 39, 45}
     *
     * @param values    - input array to sort per definition above
     * @param k         - value k, such that all numbers divisible by k are placed first
     */
    public void mergeSortDivisibleByKFirst(int[] values, int k) {
        // Protect against bad input values
        if (k == 0) return;
        if (values.length <= 1) return;
        mergeSortDivisibleByKFirst(values, k, 0, values.length - 1);
    }

    private void mergeSortDivisibleByKFirst(int[] values, int k, int left, int right) {
        if (left >= right) return; // base case: one element is already sorted
        int mid = left + (right - left) / 2;
        mergeSortDivisibleByKFirst(values, k, left, mid);
        mergeSortDivisibleByKFirst(values, k, mid + 1, right);
        mergeDivisibleByKFirst(values, k, left, mid, right);
    }

    /**
     * Merge routine for mergeSortDivisibleByKFirst.
     * This is like a standard merge operation but uses a custom comparator:
     *
     * - If one element is divisible by k and the other is not, the divisible one comes first.
     * - If both are divisible by k, they are treated as equal (preserving their original order).
     * - If neither is divisible by k, the smaller element (ascending order) comes first.
     *
     * @param arr   - the array being sorted
     * @param k     - the value used for the divisibility check
     * @param left  - starting index of the merge segment
     * @param mid   - midpoint index of the merge segment
     * @param right - ending index of the merge segment
     */
    private void mergeDivisibleByKFirst(int arr[], int k, int left, int mid, int right) {
        int n = right - left + 1;
        int[] temp = new int[n];
        int i = left;
        int j = mid + 1;
        int index = 0;

        // Merge the two sorted subarrays using the custom logic.
        while (i <= mid && j <= right) {
            if (compareDivK(arr[i], arr[j], k) <= 0) {
                temp[index++] = arr[i++];
            } else {
                temp[index++] = arr[j++];
            }
        }
        // Copy any remaining elements from the left subarray.
        while (i <= mid) {
            temp[index++] = arr[i++];
        }
        // Copy any remaining elements from the right subarray.
        while (j <= right) {
            temp[index++] = arr[j++];
        }
        // Copy the merged temporary array back into the original array segment.
        for (int p = 0; p < n; p++) {
            arr[left + p] = temp[p];
        }
    }

    /**
     * Custom comparator for mergeDivisibleByKFirst.
     *
     * @param a - first number
     * @param b - second number
     * @param k - divisor value
     * @return negative if a should come before b, positive if after, and zero if they are considered equal.
     */
    private int compareDivK(int a, int b, int k) {
        boolean aDiv = (a % k == 0);
        boolean bDiv = (b % k == 0);
        if (aDiv && !bDiv) return -1;  // a is divisible by k, b is not → a comes first
        if (!aDiv && bDiv) return 1;   // b is divisible by k, a is not → b comes first
        if (aDiv && bDiv) return 0;    // both divisible by k → maintain original order (stable merge)
        // If neither is divisible by k, sort in ascending order.
        return Integer.compare(a, b);
    }

    /**
     * Method asteroidsDestroyed
     *
     * Determines whether the planet can destroy all asteroids by colliding with them in an optimal order.
     * The planet can only collide with an asteroid if its current mass is at least as large as the asteroid's mass.
     * After each collision, the planet's mass increases by the mass of the destroyed asteroid.
     *
     * @param mass       - initial mass of the planet.
     * @param asteroids  - integer array of asteroid masses.
     * @return           - true if all asteroids can be destroyed, else false.
     */
    public static boolean asteroidsDestroyed(int mass, int[] asteroids) {
        // Sort asteroids in ascending order to try to destroy smaller ones first.
        Arrays.sort(asteroids);
        for (int a : asteroids) {
            if (mass < a)
                return false;
            mass += a;
        }
        return true;
    }

    /**
     * Method numRescueSleds
     *
     * Determines the minimum number of rescue sleds required such that each sled (which can
     * carry at most two people) does not exceed the weight limit.
     *
     * The approach is to sort the people by weight and then use a two-pointer strategy to
     * pair the lightest and heaviest persons together when possible.
     *
     * @param people - an array where people[i] is the weight of the ith person.
     * @param limit  - the maximum allowed weight per sled.
     * @return       - minimum number of sleds required.
     */
    public static int numRescueSleds(int[] people, int limit) {
        Arrays.sort(people);
        int i = 0;
        int j = people.length - 1;
        int count = 0;
        // Use two pointers: one at the lightest and one at the heaviest.
        while (i <= j) {
            // If the lightest and heaviest person can share a sled, pair them.
            if (i != j && people[i] + people[j] <= limit) {
                i++;
            }
            // Always place the heaviest person in a sled.
            j--;
            count++;
        }
        return count;
    }

    // Optionally, you can add a main method here to run some tests
    public static void main(String[] args) {
        // --- Test Modified Selection Sort ---
        int[] arr1 = {5, 2, 8, 1, 4};
        System.out.println("Original array for selection sort: " + Arrays.toString(arr1));
        selectionSort(arr1, true);
        System.out.println("Ascending sorted array: " + Arrays.toString(arr1));
        selectionSort(arr1, false);
        System.out.println("Descending sorted array: " + Arrays.toString(arr1));

        // --- Test Modified Merge Sort Divisible By k First ---
        int[] arr2 = {30, 45, 22, 9, 18, 39, 6, 12};
        int k = 6;
        System.out.println("\nOriginal array for merge sort divisible by " + k + ": " + Arrays.toString(arr2));
        ProblemSolutions ps = new ProblemSolutions();
        ps.mergeSortDivisibleByKFirst(arr2, k);
        System.out.println("Modified merge sorted array: " + Arrays.toString(arr2));

        // --- Test asteroidsDestroyed ---
        int mass = 10;
        int[] asteroids1 = {3, 9, 19, 5, 21};
        System.out.println("\nAsteroids destroyed test 1: " + asteroidsDestroyed(mass, asteroids1));

        mass = 5;
        int[] asteroids2 = {4, 9, 23, 4};
        System.out.println("Asteroids destroyed test 2: " + asteroidsDestroyed(mass, asteroids2));

        // --- Test numRescueSleds ---
        int[] people1 = {1, 2};
        int limit1 = 3;
        System.out.println("\nNumber of rescue sleds test 1: " + numRescueSleds(people1, limit1));

        int[] people2 = {3, 2, 2, 1};
        int limit2 = 3;
        System.out.println("Number of rescue sleds test 2: " + numRescueSleds(people2, limit2));

        int[] people3 = {3, 5, 3, 4};
        int limit3 = 5;
        System.out.println("Number of rescue sleds test 3: " + numRescueSleds(people3, limit3));
    }
} // End Class ProblemSolutions
