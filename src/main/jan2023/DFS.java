package main.jan2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DFS {
  int globalDepth =0;
  int globalSum =0;
  String globalMaxKey;
  int y = 10000;
  private final Map<String, Integer> countMap = new HashMap<>();
  int checkDepth=0;
  List<Double> dbls = new ArrayList<>();

  public static void main(String[] args) {
    DFS scratch = new DFS();
    scratch.produce(new int[]{0,0,0,0}, 0);
    System.out.println("KEY "+ scratch.globalMaxKey);
    System.out.println("DEPTH " + scratch.globalDepth);
    System.out.println("SUM " + scratch.globalSum);
    scratch.check(new int[]{10223028, 4664880, 1642976, 0});
    System.out.println(scratch.checkDepth);
  }

  private void check(int[] c) {
    checkDepth++;
    if (c[0]==0 && c[1]==0 && c[2]==0 && c[3]==0) return;
    System.out.println(Arrays.toString(c));
    int abs = Math.abs(c[0] - c[1]);
    int abs1 = Math.abs(c[1] - c[2]);
    int abs2 = Math.abs(c[2] - c[3]);
    int abs3 = Math.abs(c[3] - c[0]);
    int max = Arrays.stream(c).max().getAsInt();
    int x = Math.max(Math.max(Math.max(abs,abs1),abs2),abs3);
    if (x!=0) dbls.add((double)max/(double)x);
    check(new int[]{abs,
        abs1,
        abs2,
        abs3});
  }

  private int produce(int[] c, int depth) {
    if (c[0]>y||c[1]>y||c[2]>y||c[3]>y) return 0;
    depth++;
    if (depth == 3) return  0;
    int maxAt = 0;
    for (int i = 0; i < c.length; i++) {
      maxAt = c[i] > c[maxAt] ? i : maxAt;
    }
    rotate(c, maxAt);
    if (c[3] > c[1]) {
      int temp = c[1];
      c[1]=c[3];
      c[3]=temp;
    }
    String key = Arrays.toString(c);
    if (countMap.containsKey(key)) {
      return countMap.get(key);
    }
    if(depth>globalDepth) {
      globalDepth = depth;
      globalSum = Arrays.stream(c).sum();
      globalMaxKey = key;
    } else if (globalDepth == depth) {
      int s =  Arrays.stream(c).sum();
      if (s<globalSum) {
        globalSum =s;
        globalMaxKey = key;
      }
    }

    boolean b = c[0] != c[2] || c[1] != c[3];
    boolean b1 = c[1] != c[2];
    boolean b2 = c[0] - c[1] - c[2] - c[3] != 0;
    if (b && b1 && b2) {
      countMap.put(key,1);
      return 1;
    }
    int start = Math.max(1,c[0]);
    int max=0;
    for (int i =start; i <= Math.min(y,start*50); i++) {
      int[] attempt;
      attempt = new int[]{i, i-c[1], i-c[1]-c[2], i-c[0]};
      if (isValid(attempt, c)) max = Math.max(max,produce(attempt,depth));
      attempt = new int[]{i, i-c[1], i-c[1]+c[2], i-c[0]};
      if (isValid(attempt, c)) max = Math.max(max,produce(attempt,depth));
      attempt = new int[]{i, i+c[1], i+c[1]+c[2], i-c[0]};
      if (isValid(attempt, c)) max = Math.max(max,produce(attempt,depth));
      attempt = new int[]{i, i+c[1], i+c[1]-c[2], i-c[0]};
      if (isValid(attempt, c)) max = Math.max(max,produce(attempt,depth));

      attempt = new int[]{i, i-c[1], i-c[1]-c[2], i+c[0]};
      if (isValid(attempt, c)) max = Math.max(max,produce(attempt,depth));
      attempt = new int[]{i, i-c[1], i-c[1]+c[2], i+c[0]};
      if (isValid(attempt, c)) max = Math.max(max,produce(attempt,depth));
      attempt = new int[]{i, i+c[1], i+c[1]+c[2], i+c[0]};
      if (isValid(attempt, c)) max = Math.max(max,produce(attempt,depth));
      attempt = new int[]{i, i+c[1], i+c[1]-c[2], i+c[0]};
      if (isValid(attempt, c)) max = Math.max(max,produce(attempt,depth));


      attempt = new int[]{i-c[0], i-c[0]-c[1], i-c[0]-c[1]-c[2], i};
      if (isValid(attempt, c)) max = Math.max(max,produce(attempt,depth));
      attempt = new int[]{i-c[0], i-c[0]-c[1], i-c[0]-c[1]+c[2], i};
      if (isValid(attempt, c)) max = Math.max(max,produce(attempt,depth));
      attempt = new int[]{i-c[0], i-c[0]+c[1], i-c[0]+c[1]+c[2], i};
      if (isValid(attempt, c)) max = Math.max(max,produce(attempt,depth));
      attempt = new int[]{i-c[0], i-c[0]+c[1], i-c[0]+c[1]-c[2], i};
      if (isValid(attempt, c)) max = Math.max(max,produce(attempt,depth));

      attempt = new int[]{i+c[0], i-c[0]-c[1], i-c[0]-c[1]-c[2], i};
      if (isValid(attempt, c)) max = Math.max(max,produce(attempt,depth));
      attempt = new int[]{i+c[0], i-c[0]-c[1], i-c[0]-c[1]+c[2], i};
      if (isValid(attempt, c)) max = Math.max(max,produce(attempt,depth));
      attempt = new int[]{i+c[0], i-c[0]+c[1], i-c[0]+c[1]+c[2], i};
      if (isValid(attempt, c)) max = Math.max(max,produce(attempt,depth));
      attempt = new int[]{i+c[0], i-c[0]+c[1], i-c[0]+c[1]-c[2], i};
      if (isValid(attempt, c)) max = Math.max(max,produce(attempt,depth));
    }
    countMap.put(key, max+1);
    return max+1;
  }

  private static boolean isValid(int[] attempt, int[] c) {
    if (attempt[0]<0 || attempt[1]<0 ||attempt[2]<0 ||attempt[3]<0) return false;
    return (Math.abs(attempt[0]-attempt[1]) == c[1] &&
        Math.abs(attempt[1]-attempt[2]) == c[2] &&
        Math.abs(attempt[2]-attempt[3]) == c[3] &&
        Math.abs(attempt[3]-attempt[0]) == c[0]);
  }

  private static void rotate(int[] c, int n) {
    for(int i = 0; i < n; i++){
      int j, first;
      //Stores the first element of the array
      first = c[0];

      for(j = 0; j < c.length-1; j++){
        //Shift element of array by one
        c[j] = c[j+1];
      }
      //First element of array will be added to the end
      c[j] = first;
    }
  }
}
