package frontend;

import backend.Arbeitsblock;
import backend.Aufgaben;
import backend.Bereich;
import backend.Projekt;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sbruhse on 17.07.2017.
 */
public class NewMainWindow extends JFrame
{
    public JPanel mainPanel;
    public Arbeitsblock aktuellerBlock;
    private JButton auftraggeberHinzufuegen;
    private JButton identitätenNeuButton;
    private JPanel leftPanel;
    private JTree treeProjekte;
    private JPanel centerPanel;
    private JButton startButton;
    private JButton stopButton;
    private JTextArea textArea1;
    private JComboBox cBAufgabe;
    private JLabel labelStart;
    private JButton druckenButton;
    private JButton bearbeitenButton;
    private JButton löschenButton;
    private JButton auftraggeberBearbeitenButton;
    private JButton identitätenBearbeitenButton;
    private JButton projektNeuButton;
    private JButton bereichNeuButton;
    private JButton aufgabeNeuButton;
    private JButton aufgabeBearbeitenButton;
    public JButton reloadButton;

    Projekt selectedProjekt = null;
    Bereich selectedBereich = null;

    public static void main(String[] args)
    {
        NewMainWindow newMainWindow = new NewMainWindow();
        newMainWindow.setContentPane(new NewMainWindow().mainPanel);
        newMainWindow.pack();
        newMainWindow.setVisible(true);
    }

    public NewMainWindow()
    {

        aktuellerBlock = new Arbeitsblock();

        loadProjektTree();
        loadAufgaben();
        stopButton.setVisible(false);
        //Combobox füllen
        ArrayList<Aufgaben> aufgabenliste = new ArrayList<>();
        try
        {
            aufgabenliste = Aufgaben.getObjectsFromJson(Aufgaben.read(Aufgaben.path), Aufgaben[].class);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }

        for (Aufgaben a : aufgabenliste) cBAufgabe.addItem(a);


        //Eventhandler
        startButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                aktuellerBlock.arbeitsblockStarten();
                datenSetzen();
                stopButton.setVisible(true);
                startButton.setVisible(false);
                labelStart.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        });

        stopButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                aktuellerBlock.arbeitsblockStoppen();
                datenSetzen();

                try
                {
                    //Gespeicherte Blöcke laden und hinzufügen
                    ArrayList<Arbeitsblock> bloecke = Arbeitsblock.getObjectsFromJson(Arbeitsblock.read(Arbeitsblock.getPath()), Arbeitsblock[].class);
                    bloecke.add(aktuellerBlock);
                    Arbeitsblock.write(Arbeitsblock.getPath(), Arbeitsblock.getJsonFromObjects(bloecke));

                    //Block zurücksetzen
                    aktuellerBlock = new Arbeitsblock();
                    stopButton.setVisible(false);
                    startButton.setVisible(true);
                    labelStart.setText("");
                }
                catch (IOException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }
        });

        cBAufgabe.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

            }
        });


        treeProjekte.addTreeSelectionListener(new TreeSelectionListener()
        {
            @Override
            public void valueChanged(TreeSelectionEvent e)
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeProjekte.getLastSelectedPathComponent();
                if (node == null)
                {
                    selectedBereich = null;
                    selectedProjekt = null;
                }
                else if (node.getUserObject().getClass() == Bereich.class)
                {
                    selectedBereich = (Bereich) node.getUserObject();
                    selectedProjekt = null;
                }
                else
                {
                    selectedProjekt = (Projekt) node.getUserObject();
                    selectedBereich = null;
                }


                loadAufgaben();
            }
        });
        druckenButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                selectedProjekt.projektDrucken();
            }
        });
        bearbeitenButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

            }
        });
        löschenButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (selectedBereich != null)
                {
                    try
                    {
                        ArrayList<Bereich> bereiche = Bereich.getObjectsFromJson(Bereich.read(Bereich.getPath()), Bereich[].class);
                        for (Bereich b : bereiche)
                        {
                            if (b.getName().equals(selectedBereich.getName()))
                            {
                                bereiche.remove(b);
                                break;
                            }
                        }
                        Bereich.write(Bereich.getPath(), Bereich.getJsonFromObjects(bereiche));
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }
                else if (selectedProjekt != null)
                {
                    try
                    {
                        ArrayList<Projekt> projekte = Projekt.getObjectsFromJson(Projekt.read(Projekt.getPath()), Projekt[].class);
                        for (Projekt p : projekte)
                        {
                            if (p.getName().equals(selectedProjekt.getName()) && p.getBereich().getName().equals(selectedProjekt.getBereich().getName()))
                            {
                                projekte.remove(p);
                                break;
                            }
                        }
                        Projekt.write(Projekt.getPath(), Projekt.getJsonFromObjects(projekte));
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }
                loadProjektTree();
            }
        });
        projektNeuButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame addProjektWindow = new JFrame("AddProjektWindow");
                addProjektWindow.setContentPane(new AddProjektWindow().mainPanel);
                addProjektWindow.pack();
                addProjektWindow.setVisible(true);
            }
        });
        bereichNeuButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame addBereichWindow = new JFrame("AddBereichWindow");
                addBereichWindow.setContentPane(new AddBereichWindow().mainPanel);
                addBereichWindow.pack();
                addBereichWindow.setVisible(true);
            }
        });
        auftraggeberHinzufuegen.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame addAuftraggeberWindow = new JFrame("AddAuftraggeberWindow");
                addAuftraggeberWindow.setContentPane(new AddAuftraggeberWindow().mainPanel);
                addAuftraggeberWindow.pack();
                addAuftraggeberWindow.setVisible(true);
            }
        });
        auftraggeberBearbeitenButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame editAuftraggeberWindow = new JFrame("EditAuftraggeberWindow");
                editAuftraggeberWindow.setContentPane(new EditAuftraggeberWindow().editAuftraggeberPanel);
                editAuftraggeberWindow.pack();
                editAuftraggeberWindow.setVisible(true);
            }
        });
        identitätenNeuButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame addIdentitaetWindow = new JFrame("AddIdentitaetWindow");
                addIdentitaetWindow.setContentPane(new AddIdentitaetWindow().mainPanelIdentitaet);
                addIdentitaetWindow.pack();
                addIdentitaetWindow.setVisible(true);
            }
        });
        identitätenBearbeitenButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame editIdentWindow = new JFrame("EditIdentitaetWindow");
                editIdentWindow.setContentPane(new EditIdentitaetWindow().editIdentPanel);
                editIdentWindow.pack();
                editIdentWindow.setVisible(true);
            }
        });
        aufgabeNeuButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame addAufgabeWindow = new JFrame("AddAufgabeWindow");
                addAufgabeWindow.setContentPane(new AddAufgabeWindow().mainPanel);
                addAufgabeWindow.pack();
                addAufgabeWindow.setVisible(true);
            }
        });
        aufgabeBearbeitenButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

            }
        });
        reloadButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                loadAufgaben();
                loadProjektTree();
            }
        });
        bearbeitenButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

            }
        });
        bearbeitenButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (selectedBereich != null)
                {
                    JFrame editBereichWindow = new JFrame("EditBereichWindow");
                    editBereichWindow.setContentPane(new EditBereichWindow().editBereichePanel);
                    editBereichWindow.pack();
                    editBereichWindow.setVisible(true);
                }
                else if (selectedProjekt != null)
                {
                    JFrame editProjektWindow = new JFrame("EditProjektWindow");
                    editProjektWindow.setContentPane(new EditProjektWindow().mainPanel);
                    editProjektWindow.pack();
                    editProjektWindow.setVisible(true);
                }
            }
        });
        aufgabeBearbeitenButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame editAufgabenWindow = new JFrame("EditAufgabenWindow");
                editAufgabenWindow.setContentPane(new EditAufgabenWindow().editAufgabenPanel);
                editAufgabenWindow.pack();
                editAufgabenWindow.setVisible(true);
            }
        });
    }


    public void datenSetzen()
    {
        aktuellerBlock.setBeschreibung(textArea1.getText());
        aktuellerBlock.setAufgabe((Aufgaben) cBAufgabe.getSelectedItem());
    }

    public void loadAufgaben()
    {
        if (selectedProjekt != null)
        {

            ArrayList<Aufgaben> aufgaben = Aufgaben.getAufgaben(selectedProjekt);

            cBAufgabe.removeAllItems();
            for (Aufgaben a : aufgaben)
                cBAufgabe.addItem(a);
        }
        else
            cBAufgabe.removeAllItems();
    }

    public void loadProjektTree()
    {

        TreeModel projektTreeModel;

        try
        {

            ArrayList<Bereich> bereiche = Bereich.getObjectsFromJson(Bereich.read(Bereich.getPath()), Bereich[].class);
            ArrayList<Projekt> projekte = Projekt.getObjectsFromJson(Projekt.read(Projekt.getPath()), Projekt[].class);

            DefaultMutableTreeNode root = new DefaultMutableTreeNode("Projekte");

            for (Bereich b : bereiche)
            {
                DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(b);
                for (Projekt p : projekte)
                {
                    if (p.getBereich().getName().equals(b.getName()))
                    {
                        defaultMutableTreeNode.add(new DefaultMutableTreeNode(p));
                    }

                }
                root.add(defaultMutableTreeNode);
            }

            treeProjekte.setModel(new DefaultTreeModel(root));

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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
    private void $$$setupUI$$$()
    {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        final JToolBar toolBar1 = new JToolBar();
        mainPanel.add(toolBar1, BorderLayout.NORTH);
        reloadButton = new JButton();
        reloadButton.setText("Reload");
        toolBar1.add(reloadButton);
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        toolBar1.add(toolBar$Separator1);
        auftraggeberHinzufuegen = new JButton();
        auftraggeberHinzufuegen.setText("Auftraggeber neu");
        toolBar1.add(auftraggeberHinzufuegen);
        auftraggeberBearbeitenButton = new JButton();
        auftraggeberBearbeitenButton.setText("Auftraggeber bearbeiten");
        toolBar1.add(auftraggeberBearbeitenButton);
        final JToolBar.Separator toolBar$Separator2 = new JToolBar.Separator();
        toolBar1.add(toolBar$Separator2);
        identitätenNeuButton = new JButton();
        identitätenNeuButton.setText("Identitäten neu");
        toolBar1.add(identitätenNeuButton);
        identitätenBearbeitenButton = new JButton();
        identitätenBearbeitenButton.setText("Identitäten bearbeiten");
        toolBar1.add(identitätenBearbeitenButton);
        final JToolBar.Separator toolBar$Separator3 = new JToolBar.Separator();
        toolBar1.add(toolBar$Separator3);
        aufgabeNeuButton = new JButton();
        aufgabeNeuButton.setText("Aufgabe neu");
        toolBar1.add(aufgabeNeuButton);
        aufgabeBearbeitenButton = new JButton();
        aufgabeBearbeitenButton.setText("Aufgabe bearbeiten");
        toolBar1.add(aufgabeBearbeitenButton);
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        leftPanel.setMinimumSize(new Dimension(500, 450));
        mainPanel.add(leftPanel, BorderLayout.WEST);
        treeProjekte = new JTree();
        leftPanel.add(treeProjekte, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JToolBar toolBar2 = new JToolBar();
        leftPanel.add(toolBar2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
        druckenButton = new JButton();
        druckenButton.setText("Drucken");
        toolBar2.add(druckenButton);
        bearbeitenButton = new JButton();
        bearbeitenButton.setText("Bearbeiten");
        toolBar2.add(bearbeitenButton);
        löschenButton = new JButton();
        löschenButton.setText("Löschen");
        toolBar2.add(löschenButton);
        final JToolBar toolBar3 = new JToolBar();
        toolBar3.setFloatable(true);
        leftPanel.add(toolBar3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
        projektNeuButton = new JButton();
        projektNeuButton.setText("Projekt neu");
        toolBar3.add(projektNeuButton);
        bereichNeuButton = new JButton();
        bereichNeuButton.setText("Bereich neu");
        toolBar3.add(bereichNeuButton);
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        centerPanel.setMinimumSize(new Dimension(400, 300));
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        centerPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        stopButton = new JButton();
        stopButton.setText("Stop");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(stopButton, gbc);
        startButton = new JButton();
        startButton.setText("Start");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(startButton, gbc);
        final Spacer spacer1 = new Spacer();
        centerPanel.add(spacer1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        centerPanel.add(panel2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Startzeit");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label1, gbc);
        labelStart = new JLabel();
        labelStart.setText("Label");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(labelStart, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        centerPanel.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cBAufgabe = new JComboBox();
        panel3.add(cBAufgabe, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textArea1 = new JTextArea();
        panel3.add(textArea1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final Spacer spacer2 = new Spacer();
        centerPanel.add(spacer2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mainPanel;
    }
}
