/*;
 * (c) 2008, stepan rutz, stepan.rutz@gmx.de 
 */
package com.roots.swtmap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * @author stepan.rutz
 * @version $Revision$
 */
public class HeaderControl extends Canvas {
    
    private static final int TOP_SPACE = 6;
    private Font font;
    private Color foreground;
    private String text = "";
    private Point size = new Point(1, 1);
    
    public HeaderControl(Composite parent) {
        super(parent, SWT.DOUBLE_BUFFERED);
        addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                onPaint(e);
            }
        });
        addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                onDispose(e);
            }
        });
        
        font = new Font(getDisplay(), "Tahoma", 10, SWT.BOLD);
        foreground = new Color(getDisplay(), 87, 166, 212);
        measureString("M");
    }
    
    private void measureString(String s) {
        GC gc = new GC(this);
        try {
            gc.setFont(font);
            size = gc.stringExtent(s);
            size.y += TOP_SPACE;
        } finally {
            gc.dispose();
        }
    }

    private void onDispose(DisposeEvent e) {
        font.dispose();
        foreground.dispose();
    }

    private void onPaint(PaintEvent e) {
        GC gc = e.gc;
        gc.setFont(font);
        gc.setForeground(foreground);
        gc.drawString(text, 0, TOP_SPACE, true);
        Point size = getSize();
        int w = size.x;
        int h = size.y;
        gc.drawLine(0, h - 1, w, h - 1);
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
        measureString(text);
        redraw();
    }
    
    @Override
    public Point computeSize(int wHint, int hHint, boolean changed) {
        return new Point(size.x, size.y);
    }
    
    
}
