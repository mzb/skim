package pk.dyplom.editor.form;

import pk.dyplom.I18n;
import pk.dyplom.diagram.InputNode;
import pk.dyplom.eval.SyntaxValidator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Formularz edycyjny bloku wejścia.
 */
public class InputNodeForm extends NodeForm<InputNode> {

    /**
     * {@inheritDoc}
     */
    public InputNodeForm(InputNode node) {
        super(node);

        setTitle(I18n.t("diagram.node.InputNode"));
        variableTextField.setFont(CODE_FONT);
        variableTextField.setText(node.getVariable());
        messageTextArea.setText(node.getMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JComponent[] fields() {
        return new JComponent[]{ variableTextField, messageTextArea };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean verify() {
        boolean valid = true;
        SyntaxValidator syntaxValidator = new SyntaxValidator();
        
        if (!syntaxValidator.isValidVariableName(variableTextField.getText())) {
            valid = false;
            highlightFieldError(variableTextField, I18n.t("error.SyntaxError.invalidVariable"));
        } else {
            unhiglightField(variableTextField);
        }
        
        return valid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onSave() {
        node.setVariable(variableTextField.getText());
        node.setMessage(messageTextArea.getText());
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
        scrollPane2 = new JScrollPane();
        messageTextArea = new JTextArea();
        label3 = new JLabel();
        variableTextField = new JTextField();
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
                label1.setText(I18n.t("diagram.node.InputNode.message"));

                //---- label2 ----
                label2.setText(I18n.t("diagram.node.comment"));

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(commentTextArea);
                }

                //======== scrollPane2 ========
                {
                    scrollPane2.setViewportView(messageTextArea);
                }

                //---- label3 ----
                label3.setText(I18n.t("diagram.node.InputNode.variable"));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addGroup(contentPanelLayout.createParallelGroup()
                                                .addComponent(label3, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label1, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label2))
                                        .addContainerGap())
                                .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                                .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                                .addComponent(variableTextField, GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                );
                contentPanelLayout.setVerticalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                                        .addComponent(label3)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(variableTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(label1)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(label2)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 85, 80};
                ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{1.0, 0.0, 0.0};

                buttonBar.add(saveButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

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
    private JScrollPane scrollPane2;
    private JTextArea messageTextArea;
    private JLabel label3;
    private JTextField variableTextField;
    private JPanel buttonBar;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
