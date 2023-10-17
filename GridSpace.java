import java.awt.*;

// Objects of the GridSpace class are directly linked to those drawn 
// on the drawing panel.
public class GridSpace 
{
    public int row;
    public int col;
    public int size;
    public int xPos;
    public int yPos;
    public Color color;
    public Organism contains;

    public GridSpace(int row, int col, int xPos, int yPos, int size, Color color) 
    {
        this.row = row;
        this.col = col;
        this.size = size;
        this.contains = null;
        this.xPos = xPos;
        this.yPos = yPos;
        this.color = color;
        Simulation.graphics.drawRect(xPos, yPos, size, size);
        Simulation.graphics.fillRect(xPos, yPos, size, size);
    }
}
