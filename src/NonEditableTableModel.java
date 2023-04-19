import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public class NonEditableTableModel extends DefaultTableModel {
    public NonEditableTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
