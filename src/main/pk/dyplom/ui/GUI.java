package pk.dyplom.ui;

import pk.dyplom.I18n;

import javax.swing.*;
import java.awt.*;

public class GUI extends JDesktopPane {

    public GUI() {
        initComponents();

        fileMenu.setText(I18n.t("menu.file"));
        editMenu.setText(I18n.t("menu.edit"));
        runMenu.setText(I18n.t("menu.run"));
        codeMenu.setText(I18n.t("menu.code"));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Mateusz Buczek
        mainMenu = new JMenuBar();
        fileMenu = new JMenu();
        newDiagramMenuItem = new JMenuItem();
        openDiagramMenuItem = new JMenuItem();
        saveDiagramMenuItem = new JMenuItem();
        exportDiagramMenuItem = new JMenuItem();
        closeDiagramMenuItem = new JMenuItem();
        editMenu = new JMenu();
        undoMenuItem = new JMenuItem();
        redoMenuItem = new JMenuItem();
        addNodeMenu = new JMenu();
        editNodeMenuItem = new JMenuItem();
        deleteNodeMenuItem = new JMenuItem();
        runMenu = new JMenu();
        startExecMenuItem = new JMenuItem();
        stopExecMenuItem = new JMenuItem();
        codeMenu = new JMenu();
        generateCodeMenuItem = new JMenuItem();
        toolbar = new JToolBar();
        newDiagramBtn = new JButton();
        openDiagramBtn = new JButton();
        saveDiagramBtn = new JButton();
        closeDiagramBtn = new JButton();
        separator1 = new JSeparator();
        runTargetSelect = new JComboBox();
        runDiagramBtn = new JButton();
        stopDiagramBtn = new JButton();
        separator2 = new JSeparator();
        generateCodeBtn = new JButton();
        nodeButtonsPanel = new JScrollPane();
        nodeButtons = new JToolBar();
        diagramsPanel = new JTabbedPane();

        //======== this ========
        setBackground(SystemColor.window);
        setName("this");

        //======== mainMenu ========
        {
            mainMenu.setName("mainMenu");

            //======== fileMenu ========
            {
                fileMenu.setText("Plik");
                fileMenu.setName("fileMenu");

                //---- newDiagramMenuItem ----
                newDiagramMenuItem.setText("text");
                newDiagramMenuItem.setName("newDiagramMenuItem");
                fileMenu.add(newDiagramMenuItem);

                //---- openDiagramMenuItem ----
                openDiagramMenuItem.setText("text");
                openDiagramMenuItem.setName("openDiagramMenuItem");
                fileMenu.add(openDiagramMenuItem);

                //---- saveDiagramMenuItem ----
                saveDiagramMenuItem.setText("text");
                saveDiagramMenuItem.setName("saveDiagramMenuItem");
                fileMenu.add(saveDiagramMenuItem);

                //---- exportDiagramMenuItem ----
                exportDiagramMenuItem.setText("text");
                exportDiagramMenuItem.setName("exportDiagramMenuItem");
                fileMenu.add(exportDiagramMenuItem);

                //---- closeDiagramMenuItem ----
                closeDiagramMenuItem.setText("text");
                closeDiagramMenuItem.setName("closeDiagramMenuItem");
                fileMenu.add(closeDiagramMenuItem);
            }
            mainMenu.add(fileMenu);

            //======== editMenu ========
            {
                editMenu.setText("Edycja");
                editMenu.setName("editMenu");

                //---- undoMenuItem ----
                undoMenuItem.setText("text");
                undoMenuItem.setName("undoMenuItem");
                editMenu.add(undoMenuItem);

                //---- redoMenuItem ----
                redoMenuItem.setText("text");
                redoMenuItem.setName("redoMenuItem");
                editMenu.add(redoMenuItem);
                editMenu.addSeparator();

                //======== addNodeMenu ========
                {
                    addNodeMenu.setText("text");
                    addNodeMenu.setName("addNodeMenu");
                }
                editMenu.add(addNodeMenu);

                //---- editNodeMenuItem ----
                editNodeMenuItem.setText("text");
                editNodeMenuItem.setName("editNodeMenuItem");
                editMenu.add(editNodeMenuItem);

                //---- deleteNodeMenuItem ----
                deleteNodeMenuItem.setText("text");
                deleteNodeMenuItem.setName("deleteNodeMenuItem");
                editMenu.add(deleteNodeMenuItem);
            }
            mainMenu.add(editMenu);

            //======== runMenu ========
            {
                runMenu.setText("text");
                runMenu.setName("runMenu");

                //---- startExecMenuItem ----
                startExecMenuItem.setText("text");
                startExecMenuItem.setName("startExecMenuItem");
                runMenu.add(startExecMenuItem);

                //---- stopExecMenuItem ----
                stopExecMenuItem.setText("text");
                stopExecMenuItem.setName("stopExecMenuItem");
                runMenu.add(stopExecMenuItem);
            }
            mainMenu.add(runMenu);

            //======== codeMenu ========
            {
                codeMenu.setText("text");
                codeMenu.setName("codeMenu");

                //---- generateCodeMenuItem ----
                generateCodeMenuItem.setText("text");
                generateCodeMenuItem.setName("generateCodeMenuItem");
                codeMenu.add(generateCodeMenuItem);
            }
            mainMenu.add(codeMenu);
        }

        //======== toolbar ========
        {
            toolbar.setFloatable(false);
            toolbar.setName("toolbar");

            //---- newDiagramBtn ----
            newDiagramBtn.setText("Nowy");
            newDiagramBtn.setName("newDiagramBtn");
            toolbar.add(newDiagramBtn);

            //---- openDiagramBtn ----
            openDiagramBtn.setText("Otw\u00f3rz");
            openDiagramBtn.setName("openDiagramBtn");
            toolbar.add(openDiagramBtn);

            //---- saveDiagramBtn ----
            saveDiagramBtn.setText("Zapisz");
            saveDiagramBtn.setName("saveDiagramBtn");
            toolbar.add(saveDiagramBtn);

            //---- closeDiagramBtn ----
            closeDiagramBtn.setText("Zamknij");
            closeDiagramBtn.setName("closeDiagramBtn");
            toolbar.add(closeDiagramBtn);

            //---- separator1 ----
            separator1.setOrientation(SwingConstants.VERTICAL);
            separator1.setMaximumSize(new Dimension(4, 0));
            separator1.setName("separator1");
            toolbar.add(separator1);

            //---- runTargetSelect ----
            runTargetSelect.setEditable(true);
            runTargetSelect.setMaximumSize(new Dimension(255, 24));
            runTargetSelect.setEnabled(false);
            runTargetSelect.setName("runTargetSelect");
            toolbar.add(runTargetSelect);

            //---- runDiagramBtn ----
            runDiagramBtn.setText("Uruchom");
            runDiagramBtn.setName("runDiagramBtn");
            toolbar.add(runDiagramBtn);

            //---- stopDiagramBtn ----
            stopDiagramBtn.setText("Zako\u0144cz");
            stopDiagramBtn.setName("stopDiagramBtn");
            toolbar.add(stopDiagramBtn);

            //---- separator2 ----
            separator2.setOrientation(SwingConstants.VERTICAL);
            separator2.setMaximumSize(new Dimension(4, 0));
            separator2.setName("separator2");
            toolbar.add(separator2);

            //---- generateCodeBtn ----
            generateCodeBtn.setText("text");
            generateCodeBtn.setName("generateCodeBtn");
            toolbar.add(generateCodeBtn);
        }

        //======== nodeButtonsPanel ========
        {
            nodeButtonsPanel.setName("nodeButtonsPanel");

            //======== nodeButtons ========
            {
                nodeButtons.setOrientation(SwingConstants.VERTICAL);
                nodeButtons.setFloatable(false);
                nodeButtons.setName("nodeButtons");
            }
            nodeButtonsPanel.setViewportView(nodeButtons);
        }

        //======== diagramsPanel ========
        {
            diagramsPanel.setName("diagramsPanel");
        }

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addComponent(mainMenu, GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE)
                .addComponent(toolbar, GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(nodeButtonsPanel, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(diagramsPanel, GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addComponent(mainMenu, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(toolbar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup()
                        .addComponent(diagramsPanel, GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                        .addComponent(nodeButtonsPanel, GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)))
        );
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Mateusz Buczek
    public JMenuBar mainMenu;
    public JMenu fileMenu;
    public JMenuItem newDiagramMenuItem;
    public JMenuItem openDiagramMenuItem;
    public JMenuItem saveDiagramMenuItem;
    public JMenuItem exportDiagramMenuItem;
    public JMenuItem closeDiagramMenuItem;
    public JMenu editMenu;
    public JMenuItem undoMenuItem;
    public JMenuItem redoMenuItem;
    public JMenu addNodeMenu;
    public JMenuItem editNodeMenuItem;
    public JMenuItem deleteNodeMenuItem;
    public JMenu runMenu;
    public JMenuItem startExecMenuItem;
    public JMenuItem stopExecMenuItem;
    public JMenu codeMenu;
    public JMenuItem generateCodeMenuItem;
    public JToolBar toolbar;
    public JButton newDiagramBtn;
    public JButton openDiagramBtn;
    public JButton saveDiagramBtn;
    public JButton closeDiagramBtn;
    public JSeparator separator1;
    public JComboBox runTargetSelect;
    public JButton runDiagramBtn;
    public JButton stopDiagramBtn;
    public JSeparator separator2;
    public JButton generateCodeBtn;
    public JScrollPane nodeButtonsPanel;
    public JToolBar nodeButtons;
    public JTabbedPane diagramsPanel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
