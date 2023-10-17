// Class for storing grid checking parameters in a single object to store in array.
public class GridCheckParameters 
{
    public Boolean condition;
    public int rowChange;
    public int colChange;

    public GridCheckParameters(Boolean condition, int rowChange, int colChange)
    {
        this.condition = condition;
        this.rowChange = rowChange;
        this.colChange = colChange;
    }
}
