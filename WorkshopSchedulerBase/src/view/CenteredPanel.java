package view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class CenteredPanel extends JPanel {

    private final static float FONT_MULTIPLIER = 0.8f;

    private final JPanel subPanel;
    private final JLabel mainLabel;
    private final JLabel subLabel1;
    private final JLabel subLabel2;

    public CenteredPanel(boolean detailed, float fontSize) {

        //Prepare aligned panel
        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setLayout(layout);

        //Create sub panel
        if(detailed) {
            subPanel = new JPanel(new GridLayout(3, 1));
        } else {
            subPanel = new JPanel(new GridLayout(1, 1));
        }
        this.add(subPanel);

        //Create main label
        mainLabel = new JLabel("", JLabel.CENTER);
        mainLabel.setFont(mainLabel.getFont().deriveFont(fontSize));
        subPanel.add(mainLabel);

        //Create sub labels
        subLabel1 = new JLabel("", JLabel.CENTER);
        subLabel1.setFont(subLabel1.getFont().deriveFont(fontSize * FONT_MULTIPLIER).deriveFont(Font.ITALIC));
        subLabel2 = new JLabel("", JLabel.CENTER);
        subLabel2.setFont(subLabel2.getFont().deriveFont(fontSize * FONT_MULTIPLIER).deriveFont(Font.ITALIC));
        if(detailed) {
            subPanel.add(subLabel1);
            subPanel.add(subLabel2);
        }

    }

    public void setPanelBorder(Border border) {
        subPanel.setBorder(border);
    }

    public void setPanelBackground(Color color) {
        subPanel.setBackground(color);
    }

    public void setPanelText(String text) {
        mainLabel.setText(text);
    }

    public void setPanelSubText1(String text) {
        subLabel1.setText(text);
    }

    public void setPanelSubText2(String text) {
        subLabel2.setText(text);
    }

}
