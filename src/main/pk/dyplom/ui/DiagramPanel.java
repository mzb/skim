/*
 * Created by JFormDesigner on Mon Apr 02 23:19:03 CEST 2012
 */

package pk.dyplom.ui;

import com.mindfusion.diagramming.DiagramView;

import javax.swing.*;

/**
 * @author Mateusz Buczek
 */
public class DiagramPanel extends JSplitPane {
    public DiagramPanel() {
        initComponents();

        diagramLog.setContentType("text/html");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Mateusz Buczek
        diagramViewPanel = new JScrollPane();
        diagramView = new DiagramView();
        diagramRuntimePanel = new JTabbedPane();
        diagramVariablesPanel = new JScrollPane();
        diagramVariables = new JTable();
        diagramLogPanel = new JScrollPane();
        diagramLog = new JTextPane();

        //======== diagramPanel ========
        {
            setOrientation(JSplitPane.VERTICAL_SPLIT);
            setOneTouchExpandable(true);
            setDividerLocation(400);

            //======== diagramViewPanel ========
            {

                //======== diagramView ========
                {

                    GroupLayout diagramViewLayout = new GroupLayout(diagramView);
                    diagramView.setLayout(diagramViewLayout);
                    diagramViewLayout.setHorizontalGroup(
                        diagramViewLayout.createParallelGroup()
                            .addGap(0, 3780, Short.MAX_VALUE)
                    );
                    diagramViewLayout.setVerticalGroup(
                        diagramViewLayout.createParallelGroup()
                            .addGap(0, 3780, Short.MAX_VALUE)
                    );
                }
                diagramViewPanel.setViewportView(diagramView);
            }
            setTopComponent(diagramViewPanel);

            //======== diagramRuntimePanel ========
            {

                //======== diagramVariablesPanel ========
                {
                    diagramVariablesPanel.setViewportView(diagramVariables);
                }
                diagramRuntimePanel.addTab("Zmienne", diagramVariablesPanel);


                //======== diagramLogPanel ========
                {

                    //---- diagramLog ----
                    diagramLog.setEditable(false);
                    diagramLogPanel.setViewportView(diagramLog);
                }
                diagramRuntimePanel.addTab("LOG", diagramLogPanel);

            }
            setBottomComponent(diagramRuntimePanel);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Mateusz Buczek
    public JScrollPane diagramViewPanel;
    public DiagramView diagramView;
    public JTabbedPane diagramRuntimePanel;
    public JScrollPane diagramVariablesPanel;
    public JTable diagramVariables;
    public JScrollPane diagramLogPanel;
    public JTextPane diagramLog;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
