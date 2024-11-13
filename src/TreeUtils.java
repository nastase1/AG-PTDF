import java.util.*;

public class TreeUtils {
    private final int nodeCount;
    private final List<List<Integer>> adj;

    public TreeUtils(int V, List<List<Integer>> adj) {
        this.nodeCount = V;
        this.adj = adj;
    }


    public Integer findRoot() {
        int[] inDegree = new int[nodeCount];

        for (int u = 0; u < nodeCount; u++) {
            for (int v : adj.get(u)) {
                inDegree[v]++;
            }
        }

        Integer root = null;
        for (int i = 0; i < nodeCount; i++) {
            if (inDegree[i] == 0) {
                if (root != null) {
                    return null;
                }
                root = i;
            }
        }

        if (root != null && isAcyclic(root) && isWeaklyConnected(root)) {
            return root;
        }
        return null;
    }

    private boolean isAcyclic(int root) {
        boolean[] visited = new boolean[nodeCount];
        return !hasCycle(root, visited);
    }

    private boolean hasCycle(int startNode, boolean[] visited) {
        // Stack to hold pairs of (node, index of next neighbor to visit)
        Stack<int[]> stack = new Stack<>();
        boolean[] recursionStack = new boolean[nodeCount];

        // Start with the initial node
        stack.push(new int[]{startNode, 0});
        recursionStack[startNode] = true;

        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int node = current[0];
            int index = current[1];

            // Mark node as visited
            visited[node] = true;

            if (index < adj.get(node).size()) {
                int neighbor = adj.get(node).get(index);
                current[1]++; // Move to the next neighbor for the current node

                // If the neighbor is in the recursion stack, we found a cycle
                if (recursionStack[neighbor]) {
                    return true;
                }

                // If the neighbor hasn't been visited, push it onto the stack
                if (!visited[neighbor]) {
                    stack.push(new int[]{neighbor, 0});
                    recursionStack[neighbor] = true;
                }
            } else {
                // All neighbors of the current node have been processed
                recursionStack[node] = false;
                stack.pop();
            }
        }

        return false;
    }



    private boolean isWeaklyConnected(int root) {
        // Convert to undirected and perform BFS to check connectivity
        boolean[] visited = new boolean[nodeCount];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(root);
        visited[root] = true;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            for (int neighbor : adj.get(node)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
            // Also add reverse edges to ensure weak connectivity
            for (int i = 0; i < nodeCount; i++) {
                if (adj.get(i).contains(node) && !visited[i]) {
                    visited[i] = true;
                    queue.add(i);
                }
            }
        }

        // Check if all nodes are visited
        for (boolean v : visited) {
            if (!v) return false;
        }
        return true;
    }

}
