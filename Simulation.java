import java.util.*;
import java.awt.*;

public class Simulation
{
    public static Scanner console;
    public static Organism[][] TwoDimensionalArray;
    public static GridSpace[][] GridSpaces;
    public static int m; public static int n;
    // These two variables help keep track of individual organisms on the board.
    // This is mostly for debugging. Helps to name organisms sequentially.
    public static int currentDoodlebugNumber = 1;
    public static int currentAntNumber = 1;
    // These lists make it so we do not have to iterate through the whole 2D array
    // multiple times per step. Saves time, but uses more space.
    public static ArrayList<Ant> Ants = new ArrayList<Ant>();
    public static ArrayList<Doodlebug> Doodlebugs = new ArrayList<Doodlebug>();
    // This will house the current organisms that are being reproduced in order to avoid
    // a ConcurrentModificationException for trying to modify the above lists while
    // iterating through them.
    public static ArrayList<Organism> currentOffspring = new ArrayList<Organism>();
    // Same idea here as above, but for Doodlebugs that starve
    public static ArrayList<Doodlebug> currentStarvedDoodlebugs = new ArrayList<Doodlebug>();
    // Drawing panel class variables
    public static DrawingPanel Board;
    public static Graphics graphics;
    public static int animationSpeed;

    public static void main(String[] args) throws Exception
    {
        console = new Scanner(System.in);
        // Determine grid size
        m = verifyGridSizeInput(0, "rows");
        n = verifyGridSizeInput(0, "columns");
        // Determine number of ants and doodlebugs
        int ants = verifyOrganismInput(-1, "ants", 0, m, n);
        int doodlebugs = verifyOrganismInput(-1, "doodlebugs", ants, m, n);
        // Create and populate the array
        TwoDimensionalArray = new Organism[m][n];
        // This creates a matching array of gridspots that are linked to what is
        // displayed on the drawing panel. Size, xPos, yPos, etc.
        GridSpaces = new GridSpace[m][n];
        // Start the simulation
        InitializeBoard(m, n);
        Simulation.Board.sleep(2700);
        // Scale animation speed based on grid size
        animationSpeed = Math.min(55, (int) Math.round((1.0 / (m * n)) * 700));
        // If you want no animation, uncomment the following line of code and
        // make sure both "console.nextLine();" lines are uncommented before and
        // within the while loop. Also make sure the line within the while loop:
        // "Simulation.Board.sleep(Simulation.animationSpeed);" is commented out.
        //animationSpeed = 0;
        populate2dArray(ants, doodlebugs, m, n);
        // Simulation will continue as long as there are doodlebugs on the board.
        // In the case that all doodlebugs die and there are only ants left, the 
        // simulation will stop once the board is filled with all ants.
        // Note: Increasingly large inputs will make clearing/filling the board
        // take significantly long times.
        //print2dArray(TwoDimensionalArray, m, n);

        System.out.println("Press the [SPACEBAR] key and then the [ENTER] key to speed up the animation.");
        String input;
        input = console.nextLine();
        if (input.equals(" ")) 
            {
                animationSpeed /= 2;
                System.out.println(String.format("Animation speed slowed to %d ms.", animationSpeed));
            }
        while (Doodlebugs.size() > 0 || (Ants.size() < m * n && Ants.size() > 0))
        { 
            input = console.nextLine();
            if (input.equals(" ")) 
            {
                animationSpeed /= 2;
                System.out.println(String.format("Animation speed slowed to %d ms.", animationSpeed));
            }
            // For an automatic simulation, comment out the two console.nextLine()
            // lines of code (the above line of code and the line before the while
            // loop. Then uncomment the following line of code. For semi-automatic
            // simulation, just hold down "enter".
            //Board.sleep(Math.max(4, (int) (Simulation.animationSpeed * 1.25)));
            simulateStep();
            //print2dArray(TwoDimensionalArray, m, n);
        }
        console.close();
        String winner; Color color;
        if (Ants.size() > 0) { winner = "Ants"; color = Color.BLACK; } else { winner = "Doodlebugs"; color = new Color(112, 6, 6); }
        DisplayWinningMessage(winner, color, n);
    }

    public static void InitializeBoard(int m, int n)
    {
        // This is based on computer screen size. These numbers work best for my screen.
        int maxWidth = 1100; int maxHeight = 550;
        int width = 0; int height = 0;
        // Determine height and width of the drawing board based on proportion of m to n.
        if (m >= n / 2.0) { height = maxHeight - (maxHeight % m); width = n * (height / m); }
        else { width = maxWidth - (maxWidth % n); height = m * (width / n); }
        Board = new DrawingPanel(width, height);
        graphics = Board.getGraphics();
        // Grid will be made up of squares
        int gridSpotSize = height / m;
        Boolean alt = true;
        int xPos = 0; int yPos = 0;
        // Populate the board with grid squares of alternating color
        for (int i = 0; i < TwoDimensionalArray.length; i++)
        {
            for (int j = 0; j < TwoDimensionalArray[0].length; j++)
            {
                if (alt) { graphics.setColor(Color.GRAY); alt = false; }
                else { graphics.setColor(Color.WHITE); alt = true; }
                GridSpaces[i][j] = new GridSpace(i, j, xPos, yPos, gridSpotSize, graphics.getColor());
                xPos += gridSpotSize;
            }
            yPos += gridSpotSize; xPos = 0;
            if (n % 2 == 0){ if (alt) { alt = false; } else { alt = true; } }
        }
    }

    public static void simulateStep()
    {
        // Get all movement done before checking if organisms should reproduce or starve
        Doodlebugs.forEach(bug -> bug.Move());
        Ants.forEach(ant -> ant.Move());
        // If reproduce and starve happen on same turn, the bug will reproduce, then starve
        Doodlebugs.forEach(bug -> {
            if (bug.timeUntilReproduce < 0) { bug.timeUntilReproduce = 8; }
            else { bug.timeUntilReproduce -= 1; }
            bug.timeUntilStarve -= 1;
            if (bug.timeUntilReproduce == 0) { bug.Reproduce(); }
            if (bug.timeUntilStarve == 0) { bug.Starve(); }
        });
        Ants.forEach(ant -> {
            if (ant.timeUntilReproduce < 0) { ant.timeUntilReproduce = 3; }
            else { ant.timeUntilReproduce -= 1; }
            if (ant.timeUntilReproduce == 0) { ant.Reproduce(); }
        });
        // Add all reproduced organisms to their respective lists
        currentOffspring.forEach(organism -> {
            if (organism instanceof Doodlebug) { Doodlebugs.add((Doodlebug) organism); }
            else { Ants.add((Ant) organism); }
        });
        currentOffspring = new ArrayList<Organism>();
        currentStarvedDoodlebugs.forEach(bug -> Doodlebugs.remove(bug));
        currentStarvedDoodlebugs = new ArrayList<Doodlebug>();
    }

    public static int verifyGridSizeInput(int dimension, String mn) 
    {
        while (dimension <= 0)
        {
            try 
            {
                System.out.print(String.format("Enter an integer for the number of %s in the grid (%s > 0): ", mn, mn));
                dimension = console.nextInt();
                if (dimension <= 0)
                {
                    System.out.println(String.format("The number of %s must be an integer greater than 0.", mn));                
                }
            } catch (Exception e) {
                console.next();
                System.out.println(String.format("The number of %s must be an integer greater than 0.", mn));
            }
        }
        return dimension;
    }
    
    public static int verifyOrganismInput(int organisms, String organism, int ants, int m, int n)
    {
        while (organisms > (m * n) - ants | organisms < 0)
        {
            try 
            {
                System.out.print(String.format("Enter an integer for the number of %s on the grid (0 >= %s <= %d): ", organism, organism, (m * n) - ants));
                organisms = console.nextInt();
                if (organisms > (m * n) - ants | organisms < 0) 
                {
                    System.out.println(String.format("The number of %s must be an integer between 0 and %d.", organism, (m * n) - ants));
                }
            } catch (Exception e) {
                console.next();
                System.out.println(String.format("The number of %s must be an integer between 0 and %d.", organism, (m * n) - ants));
            }
        }
        return organisms;
    }

    public static void populate2dArray(int ants, int doodlebugs, int m, int n)
    {
        Random r = new Random();
        while (ants > 0 | doodlebugs > 0)
        {
            int i = r.nextInt(0, m); int j = r.nextInt(0, n);
            if (!(TwoDimensionalArray[i][j] instanceof Ant) & !(TwoDimensionalArray[i][j] instanceof Doodlebug))
            {
                Boolean choice = r.nextBoolean();
                if (choice & ants == 0) { choice = false; }
                else if (choice == false & doodlebugs == 0) { choice = true; }
                if (choice) 
                { 
                    TwoDimensionalArray[i][j] = new Ant(i, j, currentAntNumber); 
                    currentAntNumber++;
                    ants -= 1; 
                    Ants.add((Ant) TwoDimensionalArray[i][j]); 
                } else 
                { 
                    TwoDimensionalArray[i][j] = new Doodlebug(i, j, currentDoodlebugNumber); 
                    currentDoodlebugNumber++;
                    doodlebugs -= 1; 
                    Doodlebugs.add((Doodlebug) TwoDimensionalArray[i][j]); 
                }
            }
        }
    }

    public static void DisplayWinningMessage(String text, Color color, int n) 
    {
        int fontSize; int gridSize = (GridSpaces[0][0].size * n);
        if (text.equals("Ants")) { fontSize = (gridSize / 11); }
        else { fontSize = ((GridSpaces[0][0].size * n) / 16); }
        Font font = new Font("monospaced", Font.BOLD, fontSize); 
        FontMetrics metrics = graphics.getFontMetrics(font);
        // Determine the X and Y coordinates for the text
        int h = metrics.getAscent();
        int w = metrics.stringWidth(text + " Win!");
        int x = ((GridSpaces[0][0].size * n) - w) / 2;
        int y = ((Board.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        w += 20; h += 20;
        int x2 = (Board.getWidth() - w) / 2;
        int y2 = ((Board.getHeight() - h) / 2);
        
        graphics.setFont(font);
        
        graphics.setColor(Color.DARK_GRAY);
        double factor = 5.5;
        graphics.fillRect(x2 - (int) (Math.max(1, ((GridSpaces[0][0].size)) / factor) / 2), y2 - (int) (Math.max(1, ((GridSpaces[0][0].size)) / factor) / 2), w + (int) (Math.max(1, ((GridSpaces[0][0].size)) / factor)), h + (int) (Math.max(1, ((GridSpaces[0][0].size)) / factor)));
        graphics.setColor(color);
        graphics.fillRect(x2, y2, w, h);
        
        graphics.setColor(Color.WHITE);
        graphics.drawString(text + " Win!", x, y);
    }

    // For debugging
    public static void print2dArray(Organism[][] arr, int m, int n) 
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++)
        {
            sb.append("[ ");
            for (int j = 0; j < n; j++)
            {
                if (j != n - 1)
                { 
                    if (arr[i][j] != null) 
                    { 
                        if (arr[i][j] instanceof Ant) { sb.append(String.format("   %s   , ",((Ant) arr[i][j]).name)); }
                        else { sb.append(String.format("%s, ", ((Doodlebug) arr[i][j]).name)); }
                    }
                    else { sb.append("**********, "); }
                }
                else 
                { 
                    if (arr[i][j] != null) 
                    {
                        if (arr[i][j] instanceof Ant) { sb.append(String.format("   %s    ]",((Ant) arr[i][j]).name)); }
                        else { sb.append(String.format("%s ]", ((Doodlebug) arr[i][j]).name)); }
                    }
                    else { sb.append("********** ]"); }
                }
            }
            sb.append('\n');
        }
        System.out.println(sb.toString());
    }
}
