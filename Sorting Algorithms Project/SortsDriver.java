import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;

public class SortsDriver {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    System.out.print("Enter Desired Sort Choice (i[nsertion], q[uick], m[erge], r[adix], a[ll]): ");
    String choice = input.next();
    System.out.print("Enter Deisred Array Size: ");
    int size = input.nextInt();
    int[] list = generalOutput(choice, size);
    runSort(choice, size, list);
    

    input.close();
  }

  //creates a random array and prints it if the size is 20 or less. Then it returns that array. 
  private static int[] generalOutput(String choice, int size) {
    int[] list = randomArray(size);
      if (size <= 20) {
        System.out.println("Unsorted Array: " + Arrays.toString(list));
      }
    return list;
  }

  //runs the correct sorting method as specified by the choice parameter
  private static void runSort(String choice, int size, int[] list) {
    Sorts s = new Sorts();
    if (choice.equalsIgnoreCase("a")) {
      allOutput(size, list);
      return;
    }
    if (choice.equalsIgnoreCase("i")) {
      s.insertionSort(list, 0, size);
      if (size <= 20) {
        System.out.println("Sorted Array: " + Arrays.toString(list));
      }
      System.out.println("Insertion Comparisons: " + s.getComparisonCount());
    }
    if (choice.equalsIgnoreCase("q")) {
      s.quickSort(list, 0, size);
      if (size <= 20) {
        System.out.println("Sorted Array: " + Arrays.toString(list));
      }
      System.out.println("Quick Comparisons: " + s.getComparisonCount());
    }
    if (choice.equalsIgnoreCase("m")) {   
      s.mergeSort(list, 0, size);
      if (size <= 20) {
        System.out.println("Sorted Array: " + Arrays.toString(list));
      }
      System.out.println("Merge Comparisons: " + s.getComparisonCount()); 
    }
    if (choice.equalsIgnoreCase("r")) {
      s.radixSort(list);
      if (size <= 20) {
        System.out.println("Sorted Array: " + Arrays.toString(list));
      }
      System.out.println("Radix Comparisons: " + s.getComparisonCount());
    }
  }

  //runs runSort for each sorting method
  private static void allOutput(int size, int[] list) {
    int[] copy1 = list.clone();
    int[] copy2 = list.clone();
    int[] copy3 = list.clone();
    runSort("i", size, list);
    runSort("q", size, copy1);
    runSort("m", size, copy2);
    runSort("r", size, copy3);
  }

  //returns a random array
  private static int[] randomArray(int size) {
    int[] list = new int[size];
    Random num = new Random();
    for (int i = 0; i < size; i++) {
      list[i] = (num.nextInt((size + 2) + size) - size);
    }
    return list;
  }
}
