import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class WorkFrame extends Frame {

    public WorkFrame() {

        super("Working Window");

        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton backBtn = makeBtn(gridBag, c, "Back");
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toSettingsWindow();
                dispose();
            }
        });
        add(backBtn);

        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 2;
        c.fill =  GridBagConstraints.BOTH;
        JTextField output = makeTextBox(gridBag, c, false);
        output.setBackground(Color.WHITE);
        add(output);

        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        JTable table = makeTable(gridBag, c, rows, cols, 90);
        JScrollPane tableWrapped = wrapScrl(table);
        add(tableWrapped);

        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton calcBtn = makeBtn(gridBag, c, "Calculate");
        calcBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!checkValidInput(table)) {
                    output.setText("The table has invalid or empty characters.");
                } else {
                    int[] ans = markovianMagic(table);
                    output.setText(Arrays.toString(ans));
                }
            }
        });
        add(calcBtn);

        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton fillZeroBtn = makeBtn(gridBag, c, "Fill 0s");
        fillZeroBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = 0; i < table.getRowCount(); i++) {
                    for(int k = 0; k < table.getColumnCount(); k++) {
                        if(table.getValueAt(i, k) == null) {
                            table.setValueAt("0", i , k);
                        }
                    }
                }
            }
        });
        add(fillZeroBtn);

    }

    private int[] markovianMagic(JTable table) {
        MarkovMaths markov = new MarkovMaths();
        markov.buildInputMatrix(table);
        markov.buildResult();
        return markov.getResult();
    }

    private boolean checkValidInput(JTable table) {
        if(table.getCellEditor()!= null) {
            table.getCellEditor().stopCellEditing();
        }
        for(int i = 0; i < table.getRowCount(); i++) {
            for(int k = 0; k < table.getColumnCount(); k++) {
                if(table.getValueAt(i, k) == null || !table.getValueAt(i, k).toString().matches("\\d+")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void toSettingsWindow() {
        SettingsFrame sw = new SettingsFrame();
        sw.setVisible(true);
    }
}
