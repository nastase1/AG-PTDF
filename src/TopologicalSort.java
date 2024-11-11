import java.util.*;

public class TopologicalSort {
    private static final int INF = Integer.MAX_VALUE;
    private static int time = 1;

    public static void topologicalSort(int N, int s, List<List<Integer>> adj) {
        System.out.println("Initializing topological sort for " + N + " nodes...");
        Set<Integer> U = new HashSet<>();
        Set<Integer> V = new HashSet<>();
        Set<Integer> W = new LinkedHashSet<>();
        int[] p = new int[N];
        int[] t1 = new int[N];
        int[] t2 = new int[N];
        Arrays.fill(t1, INF);
        Arrays.fill(t2, INF);


        for (int y = 0; y < N; y++) {
            U.add(y);
            p[y] = 0;
        }
        t1[s] = time++;

        while (W.size() != N) {
            while (!V.isEmpty()) {
                int x = Collections.max(V);
                V.remove(x);

                for (int y : adj.get(x)) {

                    if (U.contains(y)) {
                        U.remove(y);
                        V.add(y);
                        p[y] = x;
                        t1[y] = time++;

                    } else if (V.contains(y)) {
                        System.out.println("Cycle detected at node: " + y);
                    }
                }
                V.remove(x);
                W.add(x);
                t2[x] = time++;

            }


            if (!U.isEmpty()) {
                int y = U.iterator().next();
                U.remove(y);
                V.add(y);
                t1[y] = time++;

            }
        }

        System.out.println("Topological Order:");
        List<Integer> topologicalOrder = new ArrayList<>(W);
        Collections.reverse(topologicalOrder);

        for (int node : topologicalOrder) {
            System.out.print(node + " ");
        }
        System.out.println();
    }

}
