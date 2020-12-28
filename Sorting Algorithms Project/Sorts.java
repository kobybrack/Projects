import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Random;


public class Sorts {

   // maintains a count of comparisons performed by this Sorts object
  private int comparisonCount;

  public int getComparisonCount() {
    return comparisonCount;
  }

  public void resetComparisonCount() {
    comparisonCount = 0;
  }

  /** Sorts A[start..end] in place using insertion sort
    * Precondition: 0 <= start <= end <= A.length */
  public void insertionSort(int[] A, int start, int end) {
      int i = start + 1;  
      while (i < end) {
          int j = i;
          while ((j > start) && (A[j-1] > A[j])) {
            this.comparisonCount++;
            int temp = A[j-1];
            A[j-1] = A[j];
            A[j] = temp;
            j--;
          }
          i++;
        }
    }

  /** Partitions A[start..end] around the pivot A[pivIndex]; returns the
   *  pivot's new index.
   *  Precondition: start <= pivIndex < end
   *  Postcondition: If partition returns i, then
   *  A[start..i] <= A[i] <= A[i+1..end] 
   **/
  public int partition(int[] A, int start, int end, int pivIndex) {
    int i = start + 1;
    int j = end;
    int pivSwap = A[start];
    if (pivIndex == end) {
      pivIndex = (end - 1);
    }
    A[start] = A[pivIndex];
    A[pivIndex] = pivSwap;
    pivIndex = start;
    while (i != j) {
      if (A[i] <= A[pivIndex]) {
        this.comparisonCount++;
        int temp = A[pivIndex];
        A[pivIndex] = A[i];
        A[i] = temp;
        i++;
        pivIndex++;
      } else {
        this.comparisonCount++;
        int temp = A[j - 1];
        A[j - 1] = A[i];
        A[i] = temp;
        j--;
      }
    }
    return pivIndex;
  }

  /** use quicksort to sort the subarray A[start..end] */
  public void quickSort(int[] A, int start, int end) {
    Random pivIndex = new Random();
    if (end - start < 2) {
      return;
    }
    int mid = partition(A, start, end, (pivIndex.nextInt((end + 1) - start) + start));
    quickSort(A, start, mid);
    quickSort(A, mid + 1, end);
  }

  /** merge the sorted subarrays A[start..mid] and A[mid..end] into
   *  a single sorted array in A. */
  public void merge(int[] A, int start, int mid, int end) {
    int[] B = A.clone();
    int i = start;
    int j = mid;
    int k = start;
    while ((i != mid) && (j != end)) {
      if (B[i] > B[j]) {
        this.comparisonCount++;
        A[k] = B[j];
        j++;
        k++;
      } else {
        this.comparisonCount++;
        A[k] = B[i];
        i++;
        k++;
      }
    }
    while (i != mid) {
      A[k] = B[i];
      i++;
      k++;
    }
    while (j != end) {
      A[k] = B[j];
      j++;
      k++;
    }
  }

  /** use mergesort to sort the subarray A[start..end] */
  public void mergeSort(int[] A, int start, int end) {
    if (end - start < 2) {
      return;
    }
    int mid = ((end + start)/2);
    mergeSort(A, start, mid);
    mergeSort(A, mid, end);
    merge(A, start, mid, end);
  }

  /** Sort A using LSD radix sort. Uses counting sort to sort on each digit*/
  public void radixSort(int[] A) {
      ArrayList<LinkedList<Integer>> buckets = new ArrayList<LinkedList<Integer>>(10);
      for (int i = 0; i < 10; i++) {
        buckets.add(new LinkedList<Integer>());
      }
      int digitPlace = 0;
      boolean subtracted = false;
      int[] importantNumbers = findMinMax(A);
      for (int i = 0; i < findDigitCount(importantNumbers); i++) {
        for (int j = 0; j < A.length; j++) {
          /*the very first time we run through this loop, we subtract all 
          the numbers in our array by the smallest number (min given to us by findMinMax).
          This is so that we sort all positive numbers. */
          if (!subtracted) {
            A[j] = A[j] - importantNumbers[0];
          }
          buckets.get(getDigit(A[j], digitPlace)).add(A[j]);
        }
        int count = 0;
        for (int k = 0; k < 10; k++) {
          while (!buckets.get(k).isEmpty()) {
            A[count] = buckets.get(k).remove();
            count++;
          }
        }
        subtracted = true;
        digitPlace++;
      }
      /*this for loop is to revert all of the numbers we changed back to what they 
      were before we subtracted the minimum value*/
      for (int i = 0; i < A.length; i++) {
        A[i] = A[i] + importantNumbers[0];
      }
    }

    /*returns the the number of digits of the integer at index 1, after subtracting it from the index 0.
    The 0th index of the array passed in this method is the minimum value in the main array we are sorting.
    The 1st index of the array passed in this method is the value with the most digits.*/  
    private int findDigitCount(int[] A) {
      int digitCount = 0;
      /*the reason we subtract the absolute value of our max number by the minimum value is because
      in our radix sort method we subtract everything by the minmum value in the array, and our minimum value could also
      be the integer with the most digits. So to avoid getting 0 as our "biggestNumber" we use the absolute value. */ 
      int biggestNumber = Math.abs(A[1]) - A[0];
      while (biggestNumber != 0) {
        biggestNumber = biggestNumber/10;
        digitCount++;
      }
      return digitCount;
    }

    /*finds the minimum value and the integer with the largest amount of digits
    in the parameterized array and returns an array with those numbers at the 0th and 1st indexes.*/
    private int[] findMinMax(int[] A) {
      int[] list = new int[2];
      int max = A[0];
      int min = A[0];
      for (int i = 1; i < A.length; i++) {
        if (A[i] > max) {
          max = A[i];
        }
        if (A[i] < min) {
          min = A[i];
        }
      }
      if (Math.abs(min) > Math.abs(max)) {
        max = min; 
      }
      list[0] = min;
      list[1] = max;
      return list;
    }
  
    /* return the 10^d's place digit of n */
    private int getDigit(int n, int d) {
      return (n / ((int)Math.pow(10, d))) % 10;
    }

  /** swap a[i] and a[j]
   *  pre: 0 <= i, j < a.size
   *  post: values in a[i] and a[j] are swapped */
  public void swap(int[] a, int i, int j) {
    int tmp = a[i];
    a[i] = a[j];
    a[j] = tmp;
  }

}
