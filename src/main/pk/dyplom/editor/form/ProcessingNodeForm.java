package pk.dyplom.editor.form;

import pk.dyplom.I18n;
import pk.dyplom.diagram.ProcessingNode;
import pk.dyplom.eval.SyntaxValidator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Formularz edycyjny bloku przetwarzania.
 */
public class ProcessingNodeForm extends NodeForm<ProcessingNode> {

    /**
     * {@inheritDoc}
     */
    public ProcessingNodeForm(ProcessingNode node) {
        super(node);

        setTitle(I18n.t("diagram.node.ProcessingNode"));
        operationsTextArea.setFont(CODE_FONT);
        operationsTextArea.setText(node.getOperations());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JComponent[] fields() {
        return new JComponent[]{ operationsTextArea };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean verify() {
        boolean valid = true;
        SyntaxValidator syntaxValidator = new SyntaxValidator();
        
        if (!syntaxValidator.isValid(operationsTextArea.getText()) ||
            !syntaxValidator.containsValidVarDeclarations(operationsTextArea.getText())) {
            valid = false;
            highlightFieldError(operationsTextArea, I18n.t("error.SyntaxError.fieldErrors"));
        } else {
            unhiglightField(operationsTextArea);
        }

        return valid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onSave() {
        node.setOperations(operationsTextArea.getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Mateusz Buczek
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        scrollPane1 = new JScrollPane();
        operationsTextArea = new JTextArea();
        scrollPane2 = new JScrollPane();
        buttonBar = new JPanel();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));

            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- label1 ----
                label1.setText(I18n.t("diagram.node.ProcessingNode.operations"));

                //---- label2 ----
                label2.setText(I18n.t("diagram.node.comment"));

                //======== scrollPane1 ========
                {
                    scrollPane2.setViewportView(operationsTextArea);
                }

                //======== scrollPane2 ========
                {
                    scrollPane1.setViewportView(commentTextArea);
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addComponent(label1, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(280, Short.MAX_VALUE))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addComponent(label2)
                                        .addContainerGap())
                                .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                                .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                );
                contentPanelLayout.setVerticalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addComponent(label1)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                                        .addComponent(label2)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap())
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 85, 80};
                ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{1.0, 0.0, 0.0};

                //---- saveButton ----
                buttonBar.add(saveButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Mateusz Buczek
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JLabel label2;
    private JScrollPane scrollPane1;
    private JTextArea operationsTextArea;
    private JScrollPane scrollPane2;
    private JPanel buttonBar;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
