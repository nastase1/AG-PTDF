import java.awt.Color;
import java.awt.Graphics;
import  java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import static java.lang.Math.sqrt;

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
    private Point offset=null;

    private JCheckBox orientedCheckBox;

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

}
