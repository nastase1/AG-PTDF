import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

public class Node {
    private int coordX;
    private int coordY;
    private int number;

    public Node(int coordX,int coordY, int number){
        this.coordX=coordX;
        this.coordY=coordY;
        this.number=number;
    }

    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public int getNumber() {
        return number;
    }

    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void drawNode(Graphics g, int nodeDiam){
        g.setFont(new Font("TimesRoman",Font.BOLD,15));

        g.fillOval(coordX,coordY,nodeDiam,nodeDiam);
        g.setColor(Color.WHITE);
        g.drawOval(coordX,coordY,nodeDiam,nodeDiam);
        if(number<10){
            g.drawString(((Integer)number).toString(),coordX+13,coordY+20);
        }else {
            g.drawString(((Integer)number).toString(),coordX+8,coordY+20);
        }
    }


}
