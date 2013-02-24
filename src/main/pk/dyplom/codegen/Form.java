/*
 * Created by JFormDesigner on Mon May 07 19:15:55 CEST 2012
 */

package pk.dyplom.codegen;

import pk.dyplom.I18n;

import javax.swing.*;
import java.awt.*;

/**
 * Formularz do generowania i zapisu kodu w wybranycm języku programowania.
 */
public class Form extends JDialog {

    /**
     * Inicjuje formularz.
     */
    public Form() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setResizable(false);
        
        setTitle(I18n.t("generateCode.title"));

        initComponents();

        codeEditorPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        codeEditorPane.setLineWrap(false);
    }

    /**
     * Wyświetla formularz.
     *
     * @param parent Nadrzędny kontener
     */
    public void open(Container parent) {
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Zamyka formularz.
     */
    public void close() {
        dispose();
    }

    /**
     * Inicjuje komponenty.
     */
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Mateusz Buczek
        codeEditorScrollPane = new JScrollPane();
        codeEditorPane = new JTextArea();
        codeGeneratorSelect = new JComboBox();
        generateCodeBtn = new JButton();
        saveCodeBtn = new JButton();
        closeBtn = new JButton();

        //======== this ========
        Container contentPane = getContentPane();

        //======== codeEditorScrollPane ========
        {
            codeEditorScrollPane.setViewportView(codeEditorPane);
        }

        //---- generateCodeBtn ----
        generateCodeBtn.setText("text");

        //---- saveCodeBtn ----
        saveCodeBtn.setText("text");

        //---- closeBtn ----
        closeBtn.setText("text");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addGroup(GroupLayout.Alignment.LEADING, contentPaneLayout.createSequentialGroup()
                                    .addComponent(codeGeneratorSelect, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(generateCodeBtn)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(saveCodeBtn)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(closeBtn))
                            .addComponent(codeEditorScrollPane, GroupLayout.PREFERRED_SIZE, 614, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(codeEditorScrollPane, GroupLayout.PREFERRED_SIZE, 389, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(codeGeneratorSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(generateCodeBtn)
                        .addComponent(saveCodeBtn)
                        .addComponent(closeBtn))
                    .addContainerGap(16, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Mateusz Buczek
    public JScrollPane codeEditorScrollPane;
    public JTextArea codeEditorPane;
    public JComboBox codeGeneratorSelect;
    public JButton generateCodeBtn;
    public JButton saveCodeBtn;
    public JButton closeBtn;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
