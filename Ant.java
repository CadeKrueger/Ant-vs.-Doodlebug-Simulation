import java.awt.*;
import java.util.*;

public class Ant extends OrganismHelperMethods implements Organism
{
    public int row;
    public int col;
    public int timeUntilReproduce;
    // For debugging
    public String name;

    public Ant(int row, int col, int number) 
    {
        this.row = row;
        this.col = col;
        this.timeUntilReproduce = 3;
        this.name = "Ant" + number;
        Simulation.GridSpaces[row][col].contains = this;
        SpawnAntAnimation(0, 0);
    }

    public void SpawnAntAnimation(int xTranslation, int yTranslation)
    {
        Simulation.graphics.setColor(Color.BLACK);
        double scale = 5.0;
        int[] metrics = GetAntDrawingMetrics(xTranslation, yTranslation, scale);
        int xNew = metrics[0], yNew = metrics[1], sizeNew = metrics[2];

        Simulation.graphics.fillOval(xNew + (sizeNew / 4), yNew + (sizeNew / 4), sizeNew / 2, sizeNew / 2);
        Simulation.Board.sleep(Simulation.animationSpeed);

        // Center body
        Simulation.graphics.fillOval(xNew, yNew, sizeNew, sizeNew);
        Simulation.Board.sleep(Simulation.animationSpeed);
        
        int ratio = (int) Math.round(sizeNew / 6.7);
        int commonRatio = sizeNew - ratio;
        // Upper body
        Simulation.graphics.fillOval(xNew, yNew-commonRatio, sizeNew, sizeNew);
        // Lower body
        Simulation.graphics.fillOval(xNew-(ratio / 2), yNew+commonRatio+(ratio / 4), sizeNew+ratio, sizeNew+ratio);
        Simulation.Board.sleep(Simulation.animationSpeed);
        DrawAnt(xTranslation, yTranslation);
    }

    public void DrawAnt(int xTranslation, int yTranslation) 
    {
        Simulation.graphics.setColor(Color.BLACK);
        double scale = 5.0;
        int[] metrics = GetAntDrawingMetrics(xTranslation, yTranslation, scale);
        int xNew = metrics[0], yNew = metrics[1], sizeNew = metrics[2];

        // Center body
        Simulation.graphics.fillOval(xNew, yNew, sizeNew, sizeNew);
        // Everything will be centered around the center body
        int ratio = (int) Math.round(sizeNew / 6.7);
        int commonRatio = sizeNew - ratio;
        // Upper body
        Simulation.graphics.fillOval(xNew, yNew-commonRatio, sizeNew, sizeNew);
        // Lower body
        Simulation.graphics.fillOval(xNew-(ratio / 2), yNew+commonRatio+(ratio / 4), sizeNew+ratio, sizeNew+ratio);
        Simulation.Board.sleep(Simulation.animationSpeed);
        // Legs
        int legsSpacingFormula = sizeNew / 4;
        // Top legs
        int[] xValues = {xNew + (sizeNew / 2), (xNew + (sizeNew / 2)) - sizeNew, (xNew + (sizeNew / 2)) - sizeNew - (ratio * 2)}; 
        int[] yValues = {yNew + legsSpacingFormula, yNew, yNew - ((int) ((sizeNew / scale) * 3.5))};
        Simulation.graphics.drawPolyline(xValues, yValues, 3);
        
        int[] xValues2 = {xNew + (sizeNew / 2), (xNew + (sizeNew / 2)) + sizeNew, (xNew + (sizeNew / 2)) + sizeNew + (ratio * 2)}; 
        int[] yValues2 = {yNew + legsSpacingFormula, yNew, yNew - ((int) ((sizeNew / scale) * 3.5))};
        Simulation.graphics.drawPolyline(xValues2, yValues2, 3);
        // Middle legs
        int[] xValues3 = {xNew + (sizeNew / 2), (xNew + (sizeNew / 2)) - (sizeNew + ratio), xNew - sizeNew}; 
        int[] yValues3 = {yNew + (legsSpacingFormula * 2), yNew + legsSpacingFormula, yNew + ((int) ((sizeNew / scale) * 4.0))};
        Simulation.graphics.drawPolyline(xValues3, yValues3, 3);

        int[] xValues4 = {xNew + (sizeNew / 2), (xNew + (sizeNew / 2)) + (sizeNew + ratio), xNew + sizeNew * 2}; 
        int[] yValues4 = {yNew + (legsSpacingFormula * 2), yNew + legsSpacingFormula, yNew + ((int) ((sizeNew / scale) * 4.0))};
        Simulation.graphics.drawPolyline(xValues4, yValues4, 3);
        // Bottom legs
        int[] xValues5 = {xNew + (sizeNew / 2), (xNew + (sizeNew / 2)) - sizeNew, (xNew + (sizeNew / 2)) - sizeNew - (ratio * 2)};
        int[] yValues5 = {yNew + ((int) (legsSpacingFormula * 2.5)), yNew + commonRatio + (ratio / 4), (yNew + commonRatio + (ratio / 4)) + sizeNew + ratio};
        Simulation.graphics.drawPolyline(xValues5, yValues5, 3);

        int[] xValues6 = {xNew + (sizeNew / 2), (xNew + (sizeNew / 2)) + sizeNew, (xNew + (sizeNew / 2)) + sizeNew + (ratio * 2)};
        int[] yValues6 = {yNew + ((int) (legsSpacingFormula * 2.5)), yNew + commonRatio + (ratio / 4), (yNew + commonRatio + (ratio / 4)) + sizeNew + ratio};
        Simulation.graphics.drawPolyline(xValues6, yValues6, 3);
    }

    private int[] GetAntDrawingMetrics(int xTranslation, int yTranslation, double scale)
    {
        int bodySizingFormula = (int) (2.0 * (Simulation.GridSpaces[this.row][this.col].size / scale));
        int[] metrics = {(Simulation.GridSpaces[this.row][this.col].xPos + xTranslation) + bodySizingFormula,
        (Simulation.GridSpaces[this.row][this.col].yPos + yTranslation) + bodySizingFormula,
        (int) (Simulation.GridSpaces[this.row][this.col].size / scale)};
        return metrics;
    }

    public void Move()
    {
        // These parameters help to determine available spots for organisms
        // Inherits from OrganismHelperMethods
        GridCheckParameters[] parameters = super.GetGridCheckParameters(this.row, this.col);
        // This makes the movement of the bugs random
        int i = new Random().nextInt(0, 4);
        int rowChange = parameters[i].rowChange;
        int colChange = parameters[i].colChange;
        if (CheckSpot(parameters[i].condition, rowChange, colChange))
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

    private void MoveAnimation(int rowChange, int colChange)
    {
        Simulation.graphics.setColor(Simulation.GridSpaces[row][col].color);
        Simulation.graphics.fillRect(Simulation.GridSpaces[row][col].xPos, Simulation.GridSpaces[row][col].yPos, Simulation.GridSpaces[row][col].size, Simulation.GridSpaces[row][col].size);
        Simulation.Board.sleep(Simulation.animationSpeed);
        DrawAnt(colChange * (Simulation.GridSpaces[row][col].size / 2), rowChange * (Simulation.GridSpaces[row][col].size / 2));
        Simulation.Board.sleep(Simulation.animationSpeed);
        Simulation.graphics.setColor(Simulation.GridSpaces[row][col].color);
        Simulation.graphics.fillRect(Simulation.GridSpaces[row][col].xPos, Simulation.GridSpaces[row][col].yPos, Simulation.GridSpaces[row][col].size, Simulation.GridSpaces[row][col].size);
        Simulation.graphics.setColor(Simulation.GridSpaces[row+rowChange][col+colChange].color);
        Simulation.graphics.fillRect(Simulation.GridSpaces[row+rowChange][col+colChange].xPos, Simulation.GridSpaces[row+rowChange][col+colChange].yPos, Simulation.GridSpaces[row+rowChange][col+colChange].size, Simulation.GridSpaces[row+rowChange][col+colChange].size);
        DrawAnt(colChange * Simulation.GridSpaces[row][col].size, rowChange * Simulation.GridSpaces[row][col].size);
    }

    private Boolean CheckSpot(Boolean onGrid, int rowChange, int colChange)
    {
        if (onGrid && !(Simulation.TwoDimensionalArray[row+rowChange][col+colChange] instanceof Doodlebug))
        {
            if (!(Simulation.TwoDimensionalArray[row+rowChange][col+colChange] instanceof Ant)) { return true; }
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
            if (CheckSpot(parameters[i].condition, rowChange, colChange))
            {
                // Create new ant
                Ant offspring = new Ant(row+rowChange, col+colChange, Simulation.currentAntNumber);
                Simulation.currentAntNumber++;
                Simulation.TwoDimensionalArray[row+rowChange][col+colChange] = offspring;
                Simulation.currentOffspring.add(offspring);
                this.timeUntilReproduce = 3;
                break;
            }
        }
    }
}
