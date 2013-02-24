package pk.dyplom.editor.form;

import pk.dyplom.I18n;
import pk.dyplom.diagram.EndNode;
import pk.dyplom.eval.SyntaxValidator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Fomularz edycyjny bloku końcowego.
 */
public class EndNodeForm extends NodeForm<EndNode> {

    /**
     * {@inheritDoc}
     */
    public EndNodeForm(EndNode node) {
        super(node);

        setTitle(I18n.t("diagram.node.EndNode"));
        returnValueTextField.setFont(CODE_FONT);
        returnValueTextField.setText(node.getReturnValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JComponent[] fields() {
        return new JComponent[]{ returnValueTextField };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean verify() {
        boolean valid = true;
        SyntaxValidator syntaxValidator = new SyntaxValidator();
        
        if (!syntaxValidator.isValidReturnExpression(returnValueTextField.getText())) {
            valid = false;
            highlightFieldError(returnValueTextField, I18n.t("error.SyntaxError.invalidReturnValue"));
        } else {
            unhiglightField(returnValueTextField);
        }
        
        return valid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onSave() {
        node.setReturnValue(returnValueTextField.getText());
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
        returnValueTextField = new JTextField();
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
                label1.setText(I18n.t("diagram.node.EndNode.returnValue"));

                //---- label2 ----
                label2.setText(I18n.t("diagram.node.comment"));

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(commentTextArea);
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addComponent(label2)
                                        .addContainerGap(297, Short.MAX_VALUE))
                                .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addComponent(label1, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap())
                                .addComponent(returnValueTextField, GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                );
                contentPanelLayout.setVerticalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addComponent(label1)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(returnValueTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                                        .addComponent(label2)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                        .addGap(56, 56, 56))
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
    private JTextField returnValueTextField;
    private JPanel buttonBar;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
