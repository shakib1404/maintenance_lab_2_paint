package edu.cmu.hcii.paint;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class PaintCanvas extends JPanel {

    Vector history;
    
    Vector paintObjects;

    private PaintObject temporaryObject;
    private PaintObject hoveringObject;
    
    public PaintCanvas(int initialWidth, int initialHeight) {
        
        setPreferredSize(new Dimension(initialWidth, initialHeight));
        
        paintObjects = new Vector();
        
        history = new Vector();
        
    }

    private void expandFor(PaintObject paintObject) {

        if(paintObject == null) return;

        Rectangle bounds = paintObject.getBoundingBox();
        Dimension preferredSize = getPreferredSize();
        int neededWidth = Math.max(preferredSize.width, bounds.x + bounds.width + 20);
        int neededHeight = Math.max(preferredSize.height, bounds.y + bounds.height + 20);
        if(neededWidth != preferredSize.width || neededHeight != preferredSize.height) {
            setPreferredSize(new Dimension(neededWidth, neededHeight));
            revalidate();
            if(getParent() != null) getParent().revalidate();
        }

    }
    
    public void paintComponent(Graphics g) {
        
		((Graphics2D) g).addRenderingHints(
			new java.awt.RenderingHints(
				java.awt.RenderingHints.KEY_ANTIALIASING,
				java.awt.RenderingHints.VALUE_ANTIALIAS_ON));
        
        Rectangle clipBounds = g.getClipBounds();
        g.setColor(Color.white);
        g.fillRect((int)clipBounds.getX(), (int)clipBounds.getY(), 
                    (int)clipBounds.getWidth(), (int)clipBounds.getHeight());
        
        Iterator paintObjectIterator = paintObjects.iterator();
        while(paintObjectIterator.hasNext())
			try {
		        ((PaintObject)paintObjectIterator.next()).paint((Graphics2D)g); 
			} catch(Exception e) { 
				System.err.println("The graphics context isn't a Graphics2D. No anti-aliasing!");
			}
        
        if(temporaryObject != null) temporaryObject.paint((Graphics2D)g);
        
		if(hoveringObject != null) {
			
			Rectangle rect = hoveringObject.getBoundingBox();
			g.setColor(Color.black);
			g.drawRect((int)rect.getX() - 1, (int)rect.getY() - 1, (int)rect.getWidth() + 2, (int)rect.getHeight() + 2);
			hoveringObject.paint((Graphics2D)g);
			
		}
        
    }
    
    public int sizeOfHistory() { return history.size(); }
    
    public void setTemporaryObject(PaintObject temporaryObject) {
        
        this.temporaryObject = temporaryObject;
		expandFor(temporaryObject);
        repaint();
        
    }
    
    public void setHoveringObject(PaintObject hoveringObject) {
    	
    	this.hoveringObject = hoveringObject;
    	repaint();
    	
    }
    
    public void addPaintObject(PaintObject newObject) {
        
        history.addElement(new Vector(paintObjects));
        paintObjects.addElement(newObject);
		expandFor(newObject);
        repaint();
        
    }
    
    public void clear() {
        
        history.addElement(new Vector(paintObjects));
        paintObjects.removeAllElements();
        repaint();

    }

    public void undo() { 
        
        if(history.isEmpty()) return;
        paintObjects = (Vector)history.lastElement();
        history.removeElement(history.lastElement());
        repaint();
        
    }

    public boolean canUndo() {

        return !history.isEmpty();

    }


}
