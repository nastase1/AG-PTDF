import java.util.*;

public class TopologicalSort {

    public static void topologicalSort(int nodeCount, List<List<Integer>> adj) {
        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[nodeCount];
        Stack<Integer> tempStack = new Stack<>();

        for (int i = 0; i < nodeCount; i++) {
            if (!visited[i]) {
                tempStack.push(i);

                while (!tempStack.isEmpty()) {
                    int v = tempStack.peek();

                    if (!visited[v]) {
                        visited[v] = true;
                    }

                    boolean allVisited = true;
                    for (int adjVertex : adj.get(v)) {
                        if (!visited[adjVertex]) {
                            tempStack.push(adjVertex);
                            allVisited = false;
                            break;
                        }
                    }

                    if (allVisited) {
                        tempStack.pop();
                        stack.push(v);
                    }
                }
            }
        }


        while (!stack.isEmpty()) {
            System.out.print((stack.pop()+1) + " ");
        }
        System.out.println();
    }


//    public static void topologicalSort(int N, int s, List<List<Integer>> adj) {
//        System.out.println("Initializing topological sort for " + N + " nodes...");
//        int time = 1;
//        final int INF = Integer.MAX_VALUE;
//
//        Set<Integer> U = new HashSet<>();
//        Set<Integer> V = new HashSet<>();
//        List<Integer> W = new ArrayList<>();
//        int[] p = new int[N];
//        int[] t1 = new int[N];
//        int[] t2 = new int[N];
//        Arrays.fill(t1, INF);
//        Arrays.fill(t2, INF);
//
//
//        for (int y = 0; y < N; y++) {
//            U.add(y);
//            p[y] = 0;
//        }
//
//        while (!U.isEmpty()) {
//            int y=U.iterator().next();
//            U.remove(y);
//            V.add(y);
//            t1[y] = time++;
//            while (!V.isEmpty()) {
//                int x = V.iterator().next();
//                V.remove(x);
//
//                for (int neighbor : adj.get(x)) {
//
//                    if (U.contains(neighbor)) {
//                        U.remove(neighbor);
//                        V.add(neighbor);
//                        p[neighbor] = x;
//                        t1[neighbor] = time++;
//
//                    } else if (V.contains(neighbor)) {
//                        System.out.println("Cycle detected at node: " + (y+1));
//                        return;
//                    }
//                }
//
//                W.add(x);
//                t2[x] = time++;
//            }
//
//
//            if (!U.isEmpty()) {
//                int x = U.iterator().next();
//                U.remove(x);
//                V.add(x);
//                t1[x] = time++;
//
//            }
//        }
//
//        System.out.println("Topological Order:");
//        List<Integer> topologicalOrder=new ArrayList<>(W);
//        Collections.reverse(topologicalOrder);
//        for (int node : topologicalOrder) {
//            System.out.print((node+1) + " ");
//        }
//        System.out.println();
//
//        for(int node:W){
//            System.out.print((node+1) + " ");
//        }
//        System.out.println();
//    }

//    public static boolean isAcyclic(List<List<Integer>> adj, int N) {
//        int[] indegree = new int[N];
//        for (List<Integer> neighbors : adj) {
//            for (int neighbor : neighbors) {
//                indegree[neighbor]++;
//            }
//        }
//
//        Queue<Integer> queue = new LinkedList<>();
//        for (int i = 0; i < N; i++) {
//            if (indegree[i] == 0) queue.add(i);
//        }
//
//        int count = 0;
//        while (!queue.isEmpty()) {
//            int node = queue.poll();
//            count++;
//            for (int neighbor : adj.get(node)) {
//                if (--indegree[neighbor] == 0) {
//                    queue.add(neighbor);
//                }
//            }
//        }
//        return count == N;  // Dacă am parcurs toate nodurile, graful este aciclic
//    }
}
