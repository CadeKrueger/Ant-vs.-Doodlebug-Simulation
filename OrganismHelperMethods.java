import java.util.*;

// Common methods between Ant and Doodlebug.
public class OrganismHelperMethods 
{
    public GridCheckParameters[] GetGridCheckParameters(int row, int col)
    {
        GridCheckParameters[] parameters = { new GridCheckParameters(row - 1 >= 0, -1, 0), 
        new GridCheckParameters(col + 1 < Simulation.TwoDimensionalArray[0].length, 0, 1),
        new GridCheckParameters(row + 1 < Simulation.TwoDimensionalArray.length, 1, 0),
        new GridCheckParameters(col - 1 >= 0, 0, -1) };
        return parameters;
    }

    public Integer[] RandomizeSpotChecks()
    {
        Integer[] arr = { 0, 1, 2, 3 };
        List<Integer> list = Arrays.asList(arr);
        Collections.shuffle(list);
        Object[] Final = list.toArray();
        for (int i = 0; i < 4; i++) {
            arr[i] = (Integer) Final[i];
        }
        return arr;
    }
}
