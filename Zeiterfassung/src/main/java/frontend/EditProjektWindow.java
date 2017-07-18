package frontend;

import backend.Aufgaben;
import backend.Bereich;
import backend.Projekt;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by sellmer on 18.07.17.
 */
public class EditProjektWindow {
    private JTextField textFieldName;
    private JComboBox comboBoxBereich;
    private JComboBox comboBoxAuftraggeber;
    private JTextField textFieldStundensatz;
    private JButton speichernButton;
    private JButton löschenButton;
    public JPanel mainPanel;
    private JComboBox comboBoxProjekte;

    public EditProjektWindow() {
        ArrayList<Projekt> projekt = new ArrayList<>();

        try {
            projekt = Projekt.getObjectsFromJson(Projekt.read(Projekt.getPath()), Projekt[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Hinzufügen der Bereiche in die CombosBox
        for (Projekt p : projekt) comboBoxProjekte.addItem(p);
        textFieldName.setText(comboBoxProjekte.getSelectedItem().toString());
        try {
            ArrayList<Projekt> projektArr = Projekt.getObjectsFromJson(Projekt.read(Projekt.getPath()), Projekt[].class);
            for (Projekt aProjektArr : projektArr) {
                if (Objects.equals(comboBoxProjekte.getSelectedItem().toString(), aProjektArr.getName())) {
                    textFieldStundensatz.setText(Float.toString(aProjektArr.getStundensatz()));
                    break;
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        //Wartet auf Auswahl und zeigt diese dann im Namensfenster an
        comboBoxProjekte.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    textFieldName.setText(comboBoxProjekte.getSelectedItem().toString());

                    try {
                        ArrayList<Projekt> projektArr = Projekt.getObjectsFromJson(Projekt.read(Projekt.getPath()), Projekt[].class);
                        for (Projekt aProjektArr : projektArr) {
                            if (Objects.equals(comboBoxProjekte.getSelectedItem().toString(), aProjektArr.getName())) {
                                textFieldStundensatz.setText(Float.toString(aProjektArr.getStundensatz()));
                                comboBoxBereich.setSelectedItem();
                                break;
                            }
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }





                }
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(7, 2, new Insets(0, 0, 0, 0), -1, -1));
        textFieldName = new JTextField();
        mainPanel.add(textFieldName, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        comboBoxBereich = new JComboBox();
        mainPanel.add(comboBoxBereich, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxAuftraggeber = new JComboBox();
        mainPanel.add(comboBoxAuftraggeber, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldStundensatz = new JTextField();
        mainPanel.add(textFieldStundensatz, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Bereich");
        mainPanel.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Auftraggeber");
        mainPanel.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Stundensatz");
        mainPanel.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        speichernButton = new JButton();
        speichernButton.setText("Speichern");
        mainPanel.add(speichernButton, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        löschenButton = new JButton();
        löschenButton.setText("Löschen");
        mainPanel.add(löschenButton, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Name");
        mainPanel.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxProjekte = new JComboBox();
        mainPanel.add(comboBoxProjekte, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Projekt auswählen");
        mainPanel.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
