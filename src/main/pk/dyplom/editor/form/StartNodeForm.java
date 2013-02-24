package pk.dyplom.editor.form;

import pk.dyplom.I18n;
import pk.dyplom.diagram.StartNode;
import pk.dyplom.eval.SyntaxValidator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Formularz edycyjny bloku startowego.
 */
public class StartNodeForm extends NodeForm<StartNode> {

    /**
     * {@inheritDoc}
     */
    public StartNodeForm(StartNode node) {
        super(node);

        setTitle(I18n.t("diagram.node.StartNode"));
        nameTextField.setFont(CODE_FONT);
        nameTextField.setText(node.getName());
        paramsTextField.setFont(CODE_FONT);
        paramsTextField.setText(node.getParamsAsString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JComponent[] fields() {
        return new JComponent[]{ nameTextField, paramsTextField };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onSave() {
        node.setName(nameTextField.getText());
        node.setParamsFromString(paramsTextField.getText());
        node.setText(node.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean verify() {
        SyntaxValidator syntaxValidator = new SyntaxValidator();
        boolean valid = true;
        
        if (!syntaxValidator.isValidFunctionName(nameTextField.getText())) {
            valid = false;
            highlightFieldError(nameTextField, I18n.t("error.SyntaxError.invalidName", nameTextField.getText()));
        } else {
            unhiglightField(nameTextField);
        }
        
        if (!syntaxValidator.isValidParamList(paramsTextField.getText())) {
            valid = false;
            highlightFieldError(paramsTextField, I18n.t("error.SyntaxError.invalidParams"));
        } else {
            unhiglightField(paramsTextField);
        }

        return valid;
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
        nameLabel = new JLabel();
        nameTextField = new JTextField();
        commentLabel = new JLabel();
        scrollPane1 = new JScrollPane();
        paramsLabel = new JLabel();
        paramsTextField = new JTextField();
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

                //---- nameLabel ----
                nameLabel.setText(I18n.t("diagram.node.StartNode.name"));

                //---- commentLabel ----
                commentLabel.setText(I18n.t("diagram.node.comment"));

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(commentTextArea);
                }

                //---- paramsLabel ----
                paramsLabel.setText(I18n.t("diagram.node.StartNode.params"));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(280, Short.MAX_VALUE))
                                .addComponent(nameTextField, GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addComponent(paramsLabel)
                                        .addContainerGap())
                                .addComponent(paramsTextField, GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addComponent(commentLabel)
                                        .addContainerGap(297, Short.MAX_VALUE))
                                .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                );
                contentPanelLayout.setVerticalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addComponent(nameLabel)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(paramsLabel)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(paramsTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(commentLabel)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(28, Short.MAX_VALUE))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

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
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JLabel commentLabel;
    private JScrollPane scrollPane1;
    private JLabel paramsLabel;
    private JTextField paramsTextField;
    private JPanel buttonBar;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
