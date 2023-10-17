import java.util.*;
import java.awt.*;

public class Doodlebug extends OrganismHelperMethods implements Organism
{
    public int row;
    public int col;
    public int timeUntilReproduce;
    public int timeUntilStarve;
    // For debugging
    public String name;
    
    public Doodlebug(int row, int col, int number) 
    {
        this.row = row;
        this.col = col;
        this.timeUntilReproduce = 8;
        this.timeUntilStarve = 3;
        this.name = "Doodlebug" + number;
        Simulation.GridSpaces[row][col].contains = this;
        SpawnDoodlebugAnimation(0, 0);
    }

    private void SpawnDoodlebugAnimation(int xTranslation, int yTranslation) 
    {
        Simulation.graphics.setColor(new Color(112, 6, 6));
        double scale = 4.0;
        int[] metrics = GetDoodlebugDrawingMetrics(xTranslation, yTranslation, scale);
        int xNew = metrics[0], yNew = metrics[1], sizeNew = metrics[2];

        Simulation.graphics.fillOval(xNew + (sizeNew / 3), yNew + (sizeNew / 3), sizeNew / 3, sizeNew / 3);
        Simulation.Board.sleep(Simulation.animationSpeed);
        Simulation.graphics.fillOval(xNew + (sizeNew / 6), yNew + (sizeNew / 6), (int) (sizeNew / 1.5), (int) (sizeNew / 1.5));
        Simulation.Board.sleep(Simulation.animationSpeed);

        // Center body
        Simulation.graphics.fillOval(xNew, yNew, sizeNew, sizeNew);
        Simulation.Board.sleep(Simulation.animationSpeed);
        DrawDoodlebug(xTranslation, yTranslation);
    }

    private void DrawDoodlebug(int xTranslation, int yTranslation) 
    {
        Simulation.graphics.setColor(new Color(112, 6, 6));
        double scale = 4.0;
        int[] metrics = GetDoodlebugDrawingMetrics(xTranslation, yTranslation, scale);
        int xNew = metrics[0], yNew = metrics[1], sizeNew = metrics[2];

        // Center body
        Simulation.graphics.fillOval(xNew, yNew, sizeNew, sizeNew);

        // Create pinsirs
        int topSpace = (yNew - (Simulation.GridSpaces[this.row][this.col].yPos + yTranslation));
        int bottomSpace = ((Simulation.GridSpaces[this.row][this.col].yPos + yTranslation) + Simulation.GridSpaces[this.row][this.col].size) - (yNew + (metrics[3] * 2));
        int topYLevel = yNew - ((int) Math.round((topSpace - bottomSpace) * 0.9));
        int middleYLevel1 = topYLevel + ((int) (Math.round((topSpace - bottomSpace) / 2.0)));
        int middleYLevel2 = middleYLevel1 + ((int) (Math.round((topSpace - bottomSpace) / 2.0)));
        int bottomYLevel = middleYLevel2 + ((int) (Math.round((topSpace - bottomSpace) / 4.0)));
        int ratio = (int) ((Simulation.GridSpaces[this.row][this.col].size / 3.0) / 2.0);
        int middleLeftXLevel1 = xNew + ratio;
        int middleRightXLevel1 = middleLeftXLevel1 + ratio;
        int middleLeftXLevel2 = middleLeftXLevel1 - (ratio / 6);
        int middleRightXLevel2 = middleRightXLevel1 + (ratio / 6);
        int middleLeftXLevel3 = middleLeftXLevel1 - (ratio / 3);
        int middleRightXLevel3 = middleRightXLevel1 + (ratio / 3);
        int outertLeftXLevel = middleLeftXLevel1 - ((int) (ratio / 2));
        int outertRightXLevel = middleRightXLevel1 + ((int) (ratio / 2));
        int[] xPoints = {middleLeftXLevel1, middleLeftXLevel2, middleLeftXLevel1, middleRightXLevel1, middleRightXLevel2,
        middleRightXLevel1, outertRightXLevel, middleRightXLevel3, middleLeftXLevel3, outertLeftXLevel, middleLeftXLevel1};
        int[] yPoints = {topYLevel, middleYLevel1, middleYLevel2, middleYLevel2, middleYLevel1, topYLevel, middleYLevel1,
        bottomYLevel, bottomYLevel, middleYLevel1, topYLevel};
        Simulation.graphics.fillPolygon(xPoints, yPoints, 11);

        // Create legs
        int legsSpacingFormula = sizeNew / 4;
        ratio = (int) Math.round(sizeNew / 6.7);
        int commonRatio = sizeNew - ratio;
        // Top legs
        int[] xValues = {xNew + (sizeNew / 2), (xNew + (sizeNew / 2)) - (int) (sizeNew / 1.5), (xNew + (sizeNew / 2)) - ((int) (sizeNew / 1.5)) - ratio}; 
        int[] yValues = {yNew + legsSpacingFormula, yNew + (int) (ratio / 1.35), topYLevel};
        Simulation.graphics.drawPolyline(xValues, yValues, 3);
        
        int[] xValues2 = {xNew + (sizeNew / 2), (xNew + (sizeNew / 2)) + (int) (sizeNew / 1.5), (xNew + (sizeNew / 2)) + ((int) (sizeNew / 1.5)) + ratio}; 
        int[] yValues2 = {yNew + legsSpacingFormula, yNew + (int) (ratio / 1.35), topYLevel};
        Simulation.graphics.drawPolyline(xValues2, yValues2, 3);
        // Middle legs
        int[] xValues3 = {xNew + (sizeNew / 2), (xNew + (sizeNew / 2)) - (((int) (sizeNew / 1.7)) + ratio), (xNew + (sizeNew / 2)) - ((int) (sizeNew / 1.5)) - (ratio * 2)}; 
        int[] yValues3 = {yNew + (legsSpacingFormula * 2), yNew + legsSpacingFormula + (int) (ratio / 1.35), yNew + ((int) ((sizeNew / scale) * 2.5))};
        Simulation.graphics.drawPolyline(xValues3, yValues3, 3);

        int[] xValues4 = {xNew + (sizeNew / 2), (xNew + (sizeNew / 2)) + (((int) (sizeNew / 1.7)) + ratio), (xNew + (sizeNew / 2)) + ((int) (sizeNew / 1.5)) + (ratio * 2)}; 
        int[] yValues4 = {yNew + (legsSpacingFormula * 2), yNew + legsSpacingFormula + (int) (ratio / 1.35), yNew + ((int) ((sizeNew / scale) * 2.5))};
        Simulation.graphics.drawPolyline(xValues4, yValues4, 3);
        // Bottom legs
        int[] xValues5 = {xNew + (sizeNew / 2), (xNew + (sizeNew / 2)) - (int) (sizeNew / 1.4), (xNew + (sizeNew / 2)) - ((int) (sizeNew / 1.5)) - ratio};
        int[] yValues5 = {yNew + ((int) (legsSpacingFormula * 1.75)), yNew + commonRatio - (ratio / 2), yNew + sizeNew + ratio};
        Simulation.graphics.drawPolyline(xValues5, yValues5, 3);

        int[] xValues6 = {xNew + (sizeNew / 2), (xNew + (sizeNew / 2)) + (int) (sizeNew / 1.4), (xNew + (sizeNew / 2)) + ((int) (sizeNew / 1.5)) + ratio};
        int[] yValues6 = {yNew + ((int) (legsSpacingFormula * 1.75)), yNew + commonRatio - (ratio / 2), yNew + sizeNew + ratio};
        Simulation.graphics.drawPolyline(xValues6, yValues6, 3);
    }

    private int[] GetDoodlebugDrawingMetrics(int xTranslation, int yTranslation, double scale)
    {
        int bodySizingFormula = (int) (Simulation.GridSpaces[this.row][this.col].size / scale);
        int[] metrics = {(Simulation.GridSpaces[this.row][this.col].xPos + xTranslation) + bodySizingFormula,
        (Simulation.GridSpaces[this.row][this.col].yPos + yTranslation) + ((int) Math.round(bodySizingFormula * 1.25)),
        (int) (Simulation.GridSpaces[this.row][this.col].size / scale * 2), bodySizingFormula};
        return metrics;
    }

    public void Move() 
    {
        // These parameters help to determine available spots for organisms
        // Passed down from OrganismHelperMethods
        GridCheckParameters[] parameters = super.GetGridCheckParameters(this.row, this.col);
        // This makes the movement of the bugs random
        Integer[] randomOrder = super.RandomizeSpotChecks();
        int rowChange; int colChange;
        Boolean moved = false;
        for (Integer i : randomOrder)
        {
            rowChange = parameters[i].rowChange;
            colChange = parameters[i].colChange;
            if (CheckSpot(parameters[i].condition, rowChange, colChange, true))
            { 
                // For the Drawing board
                Simulation.GridSpaces[row][col].contains = null;
                Simulation.GridSpaces[row+rowChange][col+colChange].contains = this;
                MoveAnimation(rowChange, colChange);

                // Eat an ant
                Simulation.Ants.remove(Simulation.TwoDimensionalArray[row+rowChange][col+colChange]);
                Simulation.TwoDimensionalArray[row+rowChange][col+colChange] = this;
                Simulation.TwoDimensionalArray[row][col] = null;
                this.row = row+rowChange; this.col = col+colChange;
                this.timeUntilStarve = 3;
                moved = true;
                break;
            }
        }
        if (!moved)
        {
            int i = new Random().nextInt(0, 4);
            rowChange = parameters[i].rowChange;
            colChange = parameters[i].colChange;
            if (CheckSpot(parameters[i].condition, rowChange, colChange, false))
            {
                // For the Drawing board
                Simulation.GridSpaces[row][col].contains = null;
                Simulation.GridSpaces[row+rowChange][col+colChange].contains = this;
                MoveAnimation(rowChange, colChange);
                
                // Move spots
                Simulation.TwoDimensionalArray[row+rowChange][col+colChange] = this;
                Simulation.TwoDimensionalArray[row][col] = null;
                this.row = row+rowChange; this.col = col+colChange;
            }
        }
    }

    private void MoveAnimation(int rowChange, int colChange)
    {
        Simulation.graphics.setColor(Simulation.GridSpaces[row][col].color);
        Simulation.graphics.fillRect(Simulation.GridSpaces[row][col].xPos, Simulation.GridSpaces[row][col].yPos, Simulation.GridSpaces[row][col].size, Simulation.GridSpaces[row][col].size);
        Simulation.Board.sleep(Simulation.animationSpeed);
        DrawDoodlebug(colChange * (Simulation.GridSpaces[row][col].size / 2), rowChange * (Simulation.GridSpaces[row][col].size / 2));
        Simulation.Board.sleep(Simulation.animationSpeed);
        Simulation.graphics.setColor(Simulation.GridSpaces[row][col].color);
        Simulation.graphics.fillRect(Simulation.GridSpaces[row][col].xPos, Simulation.GridSpaces[row][col].yPos, Simulation.GridSpaces[row][col].size, Simulation.GridSpaces[row][col].size);
        Simulation.graphics.setColor(Simulation.GridSpaces[row+rowChange][col+colChange].color);
        Simulation.graphics.fillRect(Simulation.GridSpaces[row+rowChange][col+colChange].xPos, Simulation.GridSpaces[row+rowChange][col+colChange].yPos, Simulation.GridSpaces[row+rowChange][col+colChange].size, Simulation.GridSpaces[row+rowChange][col+colChange].size);
        DrawDoodlebug(colChange * Simulation.GridSpaces[row][col].size, rowChange * Simulation.GridSpaces[row][col].size);
    }

    private Boolean CheckSpot(Boolean onGrid, int rowChange, int colChange, Boolean replaceAnt)
    {
        if (onGrid && !(Simulation.TwoDimensionalArray[row+rowChange][col+colChange] instanceof Doodlebug))
        {
            if (Simulation.TwoDimensionalArray[row+rowChange][col+colChange] instanceof Ant == replaceAnt) { return true; }
        }
        return false;
    }
    
    public void Reproduce() 
    {
        GridCheckParameters[] parameters = super.GetGridCheckParameters(this.row, this.col);
        Integer[] randomOrder = super.RandomizeSpotChecks();
        for (Integer i : randomOrder)
        {
            int rowChange = parameters[i].rowChange;
            int colChange = parameters[i].colChange;
            if (CheckSpot(parameters[i].condition, rowChange, colChange, false))
            {
                Doodlebug offspring = new Doodlebug(row+rowChange, col+colChange, Simulation.currentDoodlebugNumber);
                Simulation.currentDoodlebugNumber++;
                Simulation.TwoDimensionalArray[row+rowChange][col+colChange] = offspring;
                Simulation.currentOffspring.add(offspring);
                this.timeUntilReproduce = 8;
                break;
            }
        }
    }

    public void Starve() 
    {
        // For the Drawing board
        Simulation.GridSpaces[row][col].contains = null;
        StarveAnimation();

        Simulation.TwoDimensionalArray[row][col] = null;
        Simulation.currentStarvedDoodlebugs.add(this);
    }

    private void StarveAnimation() 
    {
        double scale = 4.0;
        int bodySizingFormula = (int) (Simulation.GridSpaces[this.row][this.col].size / scale);
        int xNew = Simulation.GridSpaces[this.row][this.col].xPos + bodySizingFormula;
        int yNew = Simulation.GridSpaces[this.row][this.col].yPos + ((int) Math.round(bodySizingFormula * 1.25));
        int sizeNew = (int) (Simulation.GridSpaces[this.row][this.col].size / scale * 2);
        
        Simulation.graphics.setColor(Simulation.GridSpaces[row][col].color);
        Simulation.graphics.fillRect(Simulation.GridSpaces[row][col].xPos, Simulation.GridSpaces[row][col].yPos, Simulation.GridSpaces[row][col].size, Simulation.GridSpaces[row][col].size);
        Simulation.graphics.setColor(new Color(112, 6, 6));
        Simulation.graphics.fillOval(xNew, yNew, sizeNew, sizeNew);
        Simulation.Board.sleep(Simulation.animationSpeed);

        Simulation.graphics.setColor(Simulation.GridSpaces[row][col].color);
        Simulation.graphics.fillRect(Simulation.GridSpaces[row][col].xPos, Simulation.GridSpaces[row][col].yPos, Simulation.GridSpaces[row][col].size, Simulation.GridSpaces[row][col].size);
        Simulation.graphics.setColor(new Color(112, 6, 6));
        Simulation.graphics.fillOval(xNew + (sizeNew / 6), yNew + (sizeNew / 6), (int) (sizeNew / 1.5), (int) (sizeNew / 1.5));
        Simulation.Board.sleep(Simulation.animationSpeed);

        Simulation.graphics.setColor(Simulation.GridSpaces[row][col].color);
        Simulation.graphics.fillRect(Simulation.GridSpaces[row][col].xPos, Simulation.GridSpaces[row][col].yPos, Simulation.GridSpaces[row][col].size, Simulation.GridSpaces[row][col].size);
        Simulation.graphics.setColor(new Color(112, 6, 6));
        Simulation.graphics.fillOval(xNew + (sizeNew / 3), yNew + (sizeNew / 3), sizeNew / 3, sizeNew / 3);
        Simulation.Board.sleep(Simulation.animationSpeed);

        Simulation.graphics.setColor(Simulation.GridSpaces[row][col].color);
        Simulation.graphics.fillRect(Simulation.GridSpaces[row][col].xPos, Simulation.GridSpaces[row][col].yPos, Simulation.GridSpaces[row][col].size, Simulation.GridSpaces[row][col].size);
    }
}