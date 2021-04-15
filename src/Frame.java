import acm.graphics.*;
import java.awt.*;

public class Frame extends GCompound {

    public Frame(int border, int x,  int width, int height){

        GRect frame1 = new GRect(x, x - 2, width, 4);
        frame1.setFillColor(Color.DARK_GRAY);
        frame1.setFilled(true);
        GRect frame2 = new GRect(x, x - 2 + height, width, 4);
        frame2.setFillColor(Color.DARK_GRAY);
        frame2.setFilled(true);
        GRect frame3 = new GRect(x - 2, x, 4, height);
        frame3.setFillColor(Color.DARK_GRAY);
        frame3.setFilled(true);
        GRect frame4 = new GRect(x - 2 + width, x, 4, height);
        frame4.setFillColor(Color.DARK_GRAY);
        frame4.setFilled(true);
        add(frame1); add(frame2);add(frame3);add(frame4);

        GOval circle1 = new GOval(x - border/2, border/2, border, border);
        circle1.setFillColor(Color.DARK_GRAY);
        circle1.setFilled(true);
        GOval circle2 = new GOval(x - border/2 + width, border/2, border, border);
        circle2.setFillColor(Color.DARK_GRAY);
        circle2.setFilled(true);
        GOval circle3 = new GOval(x - border/2, border/2 + height, border, border);
        circle3.setFillColor(Color.DARK_GRAY);
        circle3.setFilled(true);
        GOval circle4 = new GOval(x - border/2 + width, border/2 + height, border, border);
        circle4.setFillColor(Color.DARK_GRAY);
        circle4.setFilled(true);
        add(circle1);add(circle2);add(circle3);add(circle4);

    }
    
    
}