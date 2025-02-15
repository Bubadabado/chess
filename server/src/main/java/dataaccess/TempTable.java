package dataaccess;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class TempTable {
    private Dictionary<String, ArrayList> table;
    public TempTable() {
        table = new Hashtable<>();
    }
    public <T> void addCol(String name) {
        table.put(name, new ArrayList<T>());
    }
}
