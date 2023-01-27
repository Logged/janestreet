package main.jan2023;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BFS {
  private static final int maxValue = 10000000;

  public static void main(String[] args) {
    BFS scratch = new BFS();

    int depth = scratch.execute(1, Arrays.asList(8,8,8,8));
    System.out.println(depth);

    depth = scratch.execute(3, Arrays.asList(4096, 4096,0,0));
    System.out.println(depth);

    depth = scratch.execute(3, Arrays.asList(8192, 8192,0,0));
    System.out.println(depth);

//    depth = scratch.execute(3, Arrays.asList(16384,16384,0,0));
//    System.out.println(depth);
  }

  private int execute(int startingDepth, List<Integer> startingSquare) {
    Set<List<Integer>> s = new HashSet<>();
    s.add(startingSquare);
    int depth=startingDepth;
    while(depth < 1000) {
      Set<List<Integer>> tempSet = Collections.synchronizedSet(new HashSet<>());
      try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
        final int d = depth;
        for(List<Integer> l: s) {
          executorService.submit(()-> task(tempSet,l, d));
        }
      }
      s=tempSet;
      if (s.isEmpty()) return depth;
      depth++;
      System.out.println(s.size());
      List<Integer> lowest = s.stream()
          .min(Comparator.comparingInt(x -> x.stream().reduce(0, Integer::sum))).get();
      System.out.println(lowest);
    }
    return depth;
  }

  private void task(Set<List<Integer>> tempSet, List<Integer> curr, int depth) {
    if ((!curr.get(0).equals(curr.get(2)) || !curr.get(1).equals(curr.get(3))) && !curr.get(1).equals(curr.get(2)) &&
        curr.get(0) - curr.get(1) - curr.get(2) - curr.get(3) != 0) return;
    int start = Math.max(curr.get(0),1);
    double startRatio = 1;
    double maxRatio = 1.5;
    if (depth==3) {
      startRatio = 1.4;
    }
    if (depth <3) {
      maxRatio = 1.1;
    } else if (depth > 7) {
      maxRatio = 1.2;
    }
    for (int i = (int)startRatio * start; i < Math.min((start*maxRatio)+5,maxValue); i++) {
      validateAndAdd(i, i - curr.get(1), i - curr.get(1) - curr.get(2), i - curr.get(0), curr, tempSet);
//      validateAndAdd(i, i - curr.get(1), i - curr.get(1) - curr.get(2), i + curr.get(0), curr, tempSet);
      validateAndAdd(i, i - curr.get(1), i - curr.get(1) + curr.get(2), i - curr.get(0), curr, tempSet);
//      validateAndAdd(i, i - curr.get(1), i - curr.get(1) + curr.get(2), i + curr.get(0), curr, tempSet);
//      validateAndAdd(i, i + curr.get(1), i + curr.get(1) - curr.get(2), i - curr.get(0), curr, tempSet);
//      validateAndAdd(i, i + curr.get(1), i + curr.get(1) - curr.get(2), i + curr.get(0), curr, tempSet);
//      validateAndAdd(i, i + curr.get(1), i + curr.get(1) + curr.get(2), i - curr.get(0), curr, tempSet);
//      validateAndAdd(i, i + curr.get(1), i + curr.get(1) + curr.get(2), i + curr.get(0), curr, tempSet);
//
//      validateAndAdd(i+curr.get(0), i+curr.get(0) - curr.get(1), i+curr.get(0) - curr.get(1) - curr.get(2), i, curr, tempSet);
//      validateAndAdd(i+curr.get(0), i+curr.get(0) - curr.get(1), i+curr.get(0) - curr.get(1) + curr.get(2), i, curr, tempSet);
//      validateAndAdd(i+curr.get(0), i+curr.get(0) + curr.get(1), i+curr.get(0) + curr.get(1) + curr.get(2), i, curr, tempSet);
//      validateAndAdd(i+curr.get(0), i+curr.get(0) + curr.get(1), i+curr.get(0) + curr.get(1) - curr.get(2), i, curr, tempSet);
//      validateAndAdd(i-curr.get(0), i-curr.get(0) - curr.get(1), i-curr.get(0) - curr.get(1) - curr.get(2), i, curr, tempSet);
//      validateAndAdd(i-curr.get(0), i-curr.get(0) - curr.get(1), i-curr.get(0) - curr.get(1) + curr.get(2), i, curr, tempSet);
//      validateAndAdd(i-curr.get(0), i-curr.get(0) + curr.get(1), i-curr.get(0) + curr.get(1) + curr.get(2), i, curr, tempSet);
//      validateAndAdd(i-curr.get(0), i-curr.get(0) + curr.get(1), i-curr.get(0) + curr.get(1) - curr.get(2), i, curr, tempSet);
    }
  }

  private void validateAndAdd(int a, int b, int c, int d, List<Integer> cur, Set<List<Integer>> tempSet) {
    if (a>maxValue||b>maxValue||c>maxValue||d>maxValue||a<0 || b<0 ||c<0 ||d<0) return;
    if (Math.abs(a-b) == cur.get(1) &&
        Math.abs(b-c) == cur.get(2) &&
        Math.abs(c-d) == cur.get(3) &&
        Math.abs(d-a) == cur.get(0)) {
      tempSet.add(rotate(a,b,c,d));
    }
  }
  private List<Integer> rotate(int a, int b, int c, int d) {
    List<Integer> temp = Arrays.asList(a,b,c,d);
    int maxAt = 0;
    for (int i = 0; i < temp.size(); i++) {
      maxAt = temp.get(i) > temp.get(maxAt) ? i : maxAt;
    }

    Collections.rotate(temp,-maxAt);
    if (temp.get(3) > temp.get(1)) {
      int t = temp.get(1);
      temp.set(1,temp.get(3));
      temp.set(3,t);
    }
    return temp;
  }
}
