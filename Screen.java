import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;


public class Screen extends JPanel {
    private static boolean isMousePressed = false;
    private static boolean clearScreen = false;
    private static int x;
    private static int y;
    private List<int[]> points = new ArrayList<>();
    private int startingY = 0;
    private double area = 0;
    private int heightW = 0;
    private int widthW = 0;
    
    public Screen(int heightW, int widthW){
        this.heightW = heightW;
        this.widthW = widthW;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isMousePressed = true;
                clearScreen = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isMousePressed = false;
                if(area != 0){
                    System.out.printf("Area: %.4f cm^2\n", area/(37.8*37.8) );
                    area = 0;
                }
            }
        });

        this.addMouseMotionListener(new MouseAdapter(){
            @Override
            public void mouseDragged(MouseEvent e){
                x = e.getX();
                y = e.getY();
                if(isMousePressed){
                    if(points.isEmpty() || (points.get(points.size()-1))[0] <= x){
                        points.add(new int[] {x,y});
                    }
                }
            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run(){
                if(isMousePressed){
                    //System.out.println(x + "\t" + y);
                    repaint();
                }
            }
        }, 0, 1); // Ejecuta cada 0,001 segundo
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.black);

        if(clearScreen){
            points.clear();
            clearScreen = false;
        } else{
            int localX = 0;
            int localY = 0;
            g.fillRect(points.get(0)[0], points.get(0)[1], widthW - points.get(0)[0], 3); // Eje horizontal
            g.fillRect(points.get(0)[0], 0, 3, points.get(0)[1]); // Eje vertical
            for(int[] point: points){
                if(localX == localY && localX == 0){
                    g.fillRect(point[0], point[1], 3, 3);
                    //startingX = point[0];
                    startingY = point[1];
                    area = 0;
                } else {
                    g.drawLine(localX, localY, point[0], point[1]);
                    //g.fillRect(point[0], point[1], 3, 3);
                    area += calculateArea(localX, localY, point[0], point[1]);
                }
                localX = point[0];
                localY = point[1];
            }
        }
    }

    public double calculateArea(int absoluteWidth1,int absoluteHeight1,int absoluteWidth2,int absoluteHeight2){
        int Area1 = (absoluteWidth2 - absoluteWidth1) * (startingY - absoluteHeight1);
        int Area2 = (absoluteWidth2 - absoluteWidth1) * (startingY - absoluteHeight2);
        double differentialOfArea = (Area1 + Area2)/2.0;
        return differentialOfArea;

    }

}
