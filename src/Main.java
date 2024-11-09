import javax.swing.JFrame;
import javax.swing.SwingUtilities;
public class Main {
    private static void initUI(){
        JFrame f=new JFrame("PTDF");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new MyPanel());
        f.setSize(500,500);
        f.setVisible(true);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initUI();
            }
        });
    }
}