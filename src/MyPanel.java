import java.awt.Color;
import java.awt.Graphics;
import  java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;

import static java.lang.Math.sqrt;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MyPanel extends JPanel {
    private int nodeNr=1;
    private int nodeDiam=30;
    private Vector<Node> listaNoduri;
    private Vector<Arc> listaArce;
    Point pointStart=null;
    Point pointEnd=null;
    boolean isDragging=false;
    boolean isOriented=false;
    private Node selectedNode=null;

    private JCheckBox orientedCheckBox;

    private JButton topologicalSortButton;

    public MyPanel(){
        listaNoduri=new Vector<Node>();
        listaArce=new Vector<Arc>();

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        orientedCheckBox=new JCheckBox("Graf orientat");
        orientedCheckBox.setBounds(10,10,120,30);
        orientedCheckBox.addActionListener(e -> {
            isOriented=orientedCheckBox.isSelected();
            listaNoduri.clear();
            listaArce.clear();
            nodeNr=1;
            repaint();
        });
        this.add(orientedCheckBox);

        topologicalSortButton = new JButton("Sortare topologică");
        topologicalSortButton.setBounds(140, 10, 150, 30);
        topologicalSortButton.addActionListener(e -> performTopologicalSort());
        this.add(topologicalSortButton);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Node clickNode = getNodeAtPosition(e.getX(), e.getY());
                    if (clickNode != null)
                        pointStart = e.getPoint();
                }
            }

            public void mouseReleased(MouseEvent e) {
                if(selectedNode!=null) {
                    selectedNode = null;
                } else if (!isDragging) {
                    addNode(e.getX(),e.getY());
                } else if (pointStart!=null) {
                    Node startNode = getNodeAtPosition(pointStart.x,pointStart.y);
                    Node endNode = getNodeAtPosition(e.getX(),e.getY());
                    if (startNode != null && endNode != null && startNode != endNode) {
                        Arc arc = new Arc(new Point(startNode.getCoordX() + nodeDiam / 2, startNode.getCoordY() + nodeDiam / 2),
                                new Point(endNode.getCoordX() + nodeDiam / 2, endNode.getCoordY() + nodeDiam / 2), isOriented);
                        listaArce.add(arc);

                        saveAdjacencyList("lista_adiacenta.txt");
                        saveAdjacencyMatrix("matrice_adiacenta.txt");
                    }
                }
                pointStart=null;
                pointEnd=null;
                isDragging=false;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e){
                pointEnd=e.getPoint();
                isDragging=true;
                repaint();
            }
        });
    }

    // Metoda care verifică dacă două noduri se suprapun pe baza coordonatelor lor
    private boolean checkOverlap(int x, int y) {
        for (Node node : listaNoduri) {
            double distance = sqrt(Math.pow(node.getCoordX() - x, 2) + Math.pow(node.getCoordY() - y, 2));
            if (distance < 2 * nodeDiam) {
                return true; // Nodurile se suprapun
            }
        }
        return false;
    }

    private Node getNodeAtPosition(int x, int y) {
        for (Node node : listaNoduri) {
            double distance = sqrt(Math.pow(node.getCoordX() + nodeDiam / 2 - x, 2) + Math.pow(node.getCoordY() + nodeDiam / 2 - y, 2));
            if (distance < nodeDiam / 2) {
                return node; // Returnează nodul pe care se află punctul
            }
        }
        return null; // Dacă nu se află pe niciun nod
    }

    // Metoda de adăugare a nodului cu verificarea suprapunerii
    private void addNode(int x, int y) {
        if(checkOverlap(x,y)) {
            return;
        }
        Node node = new Node(x, y, nodeNr);
        listaNoduri.add(node);
        nodeNr++;
        repaint();

        saveAdjacencyList("lista_adiacenta.txt");
        saveAdjacencyMatrix("matrice_adiacenta.txt");

    }

    // se execută atunci când apelăm repaint()
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // apelez metoda paintComponent din clasa de bază
        g.drawString("This is my Graph!", 10, 20);
        // desenează arcele existente în listă
        for (Arc a : listaArce) {
            a.drawArc(g);
        }
        // desenează arcul curent; cel care e în curs de desenare
        if (pointStart != null) {
            g.setColor(Color.RED);
            g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
        }
        // desenează lista de noduri
        for (int i = 0; i < listaNoduri.size(); i++) {
            listaNoduri.elementAt(i).drawNode(g, nodeDiam);
        }
    }

    // Metoda pentru a obține matricea de adiacență
    public int[][] getAdjacencyMatrix() {
        int nrNodes = listaNoduri.size(); // Numărul de noduri
        int[][] matrix = new int[nrNodes][nrNodes];

        for (Arc arc : listaArce) {
            // Obține coordonatele start și end din obiectul arc
            Point startPoint = arc.getStart();
            Point endPoint = arc.getEnd();

            // Obține nodurile corespunzătoare pe baza coordonatelor
            Node startNode = getNodeAtPosition(startPoint.x, startPoint.y);
            Node endNode = getNodeAtPosition(endPoint.x, endPoint.y);

            if (startNode != null && endNode != null) {
                int startIndex = listaNoduri.indexOf(startNode);
                int endIndex = listaNoduri.indexOf(endNode);

                matrix[startIndex][endIndex] = 1; // Adaugă un arc în matrice
                if(!isOriented)
                    matrix[endIndex][startIndex] = 1; // Dacă graful este neorientat, adaugă și invers
            }
        }
        return matrix;
    }

    // Metoda pentru a salva matricea de adiacență într-un fișier
    public void saveAdjacencyMatrix(String filename) {
        int[][] matrix = getAdjacencyMatrix();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(String.valueOf(listaNoduri.size())); // Scrie numărul de noduri pe prima linie
            writer.newLine(); // Trecere la urmatoarea linie

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    writer.write(String.valueOf(matrix[i][j]) + " ");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Gestionează excepțiile
        }
    }

    // Method to generate the adjacency list
    public List<List<Integer>> getAdjacencyList() {
        int nodeCount = listaNoduri.size();
        List<List<Integer>> adjacencyList = new ArrayList<>();

        // Initialize the adjacency list
        for (int i = 0; i < nodeCount; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        // Populate the adjacency list based on arcs
        for (Arc arc : listaArce) {
            Node startNode = getNodeAtPosition(arc.getStart().x, arc.getStart().y);
            Node endNode = getNodeAtPosition(arc.getEnd().x, arc.getEnd().y);

            if (startNode != null && endNode != null) {
                int startIndex = listaNoduri.indexOf(startNode);
                int endIndex = listaNoduri.indexOf(endNode);

                adjacencyList.get(startIndex).add(endIndex);

                if (!isOriented) {
                    // For undirected graph, add reverse edge
                    adjacencyList.get(endIndex).add(startIndex);
                }
            }
        }

        return adjacencyList;
    }

    // Method to save the adjacency list to a file
    public void saveAdjacencyList(String filename) {
        List<List<Integer>> adjacencyList = getAdjacencyList();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write the number of nodes as the first line
            writer.write(String.valueOf(listaNoduri.size()));
            writer.newLine();

            // Write each node's adjacency list
            for (int i = 0; i < adjacencyList.size(); i++) {
                writer.write((i+1) + ": ");
                for (int neighbor : adjacencyList.get(i)) {
                    writer.write((neighbor+1) + " ");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void performTopologicalSort(){
        System.out.println("Starting topological sort...");
        List<List<Integer>> adjacencyList = getAdjacencyList();
        int startNode = 0;
        int size = listaNoduri.size();

        TopologicalSort.topologicalSort(size,startNode,adjacencyList);
    }

}

