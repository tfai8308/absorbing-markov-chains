import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsFrame extends Frame {

    public SettingsFrame() {

        super("Settings Window");

        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.fill = GridBagConstraints.CENTER;
        JLabel rowsLabel = makeLabel(gridBag, c, "Enter a row count: ");
        add(rowsLabel);

        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.CENTER;
        JLabel colsLabel = makeLabel(gridBag, c, "Enter a column count: ");
        add(colsLabel);

        c.gridx = 2;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        JTextField rowBox = makeTextBox(gridBag, c, true);
        rowBox.setBackground(Color.WHITE);
        add(rowBox);

        c.gridx = 2;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        JTextField colBox = makeTextBox(gridBag, c, true);
        colBox.setBackground(Color.WHITE);
        add(colBox);

        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        JTextField errorBox = makeTextBox(gridBag, c, false);
        errorBox.setBackground(Color.WHITE);
        add(errorBox);

        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton nextBtn = makeBtn(gridBag, c, "Next");
        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int r = Integer.parseInt(rowBox.getText());
                    int c = Integer.parseInt(colBox.getText());
                    //maximum size
                    if(r > 10 || c > 10) {
                        r = 10;
                        c = 10;
                    }
                    else if(r <= 0 || c <= 0) {
                        throw new NumberFormatException();
                    }
                    setRowsAndCols(r, c);
                    toWorkWindow();
                    close();
                } catch (NumberFormatException excpt) {
                    errorBox.setText("Invalid row/col input.");
                }
            }
        });
        add(nextBtn);

    }

    private void toWorkWindow() {
        WorkFrame wrk = new WorkFrame();
        wrk.setVisible(true);
    }

}
