import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class Frame extends JFrame {

    //data for WorkingWindow
    protected static int rows;
    protected static int cols;

    protected JPanel contentPane;
    protected GridBagLayout gridBag = new GridBagLayout();
    protected GridBagConstraints c = new GridBagConstraints();

    public Frame(String name) {

        //temp fix because tiny font
        UIManager.put("Label.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 50)));
        UIManager.put("Button.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 50)));
        UIManager.put("TextField.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 50)));
        UIManager.put("Table.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 50)));

        setTitle(name);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(gridBag);

        setFont(new Font("SansSerif", Font.PLAIN, 21));

        c.insets = new Insets(5, 5, 5, 5);
    }

    protected JButton makeBtn(GridBagLayout gridBag, GridBagConstraints c, String name) {
        JButton btn = new JButton(name);
        gridBag.setConstraints(btn, c);
        return btn;
    }

    protected JTextField makeTextBox(GridBagLayout gridBag, GridBagConstraints c, boolean editable) {
        JTextField tf = new JTextField();
        tf.setEditable(editable);
        gridBag.setConstraints(tf, c);
        return tf;
    }

    protected JLabel makeLabel(GridBagLayout gridBag, GridBagConstraints c, String text) {
        JLabel label = new JLabel(text);
        gridBag.setConstraints(label, c);
        return label;
    }

    protected JTable makeTable(GridBagLayout gridBag, GridBagConstraints c, int rows, int cols, int rowHeight) {
        JTable table = new JTable(rows, cols);
        table.setRowHeight(rowHeight);
        gridBag.setConstraints(table, c);
        return table;
    }

    protected JScrollPane wrapScrl(JTable table) {
        JScrollPane scrlP = new JScrollPane(table);
        gridBag.setConstraints(scrlP, c);
        return scrlP;
    }

    protected void setRowsAndCols(int _rows, int _cols) {
        rows = _rows;
        cols = _cols;
    }

    protected void close() {
        dispose();
    }

}
