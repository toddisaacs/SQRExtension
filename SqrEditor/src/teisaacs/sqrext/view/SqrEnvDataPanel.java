package teisaacs.sqrext.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import oracle.ide.panels.DefaultTraversablePanel;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.layout.PaneLayout;
import oracle.jdeveloper.layout.VerticalFlowLayout;

import teisaacs.sqrext.model.SqrEnvData;

public class SqrEnvDataPanel extends DefaultTraversablePanel{

    public static final String TITLE = "SQR Environment";
    
    private JLabel envLabel = new JLabel();
    private JLabel serverLabel = new JLabel();
    private JLabel useridLabel = new JLabel();
    private JLabel passwordLabel = new JLabel();
    private JLabel dir1Label = new JLabel();
    private JLabel dir2Label = new JLabel();
    private JLabel copyDir1Label = new JLabel();
    private JLabel copyDir2Label = new JLabel();
    
    private JTextField envText = new JTextField();
    private JTextField serverText = new JTextField();
    private JTextField useridText = new JTextField();
    private JPasswordField passwordText = new JPasswordField();
    private JTextField dir1Text = new JTextField();
    private JTextField dir2Text = new JTextField();
    private JTextField copyDir1Text = new JTextField();
    private JTextField copyDir2Text = new JTextField();

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    public SqrEnvDataPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(gridBagLayout1);
        envLabel.setText("Environment");
        serverLabel.setText("Server");
        useridLabel.setText("User ID");
        passwordLabel.setText("Password");
        dir1Label.setText("Directory 1");
        dir2Label.setText("Directory  2");
        copyDir1Label.setText("Copy Directory 1");
        copyDir2Label.setText("Copy Directory 2");
        this.add(copyDir2Text, 
                 new GridBagConstraints(1, 14, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
                                        new Insets(12, 70, 0, 10), 213, 5));
        this.add(copyDir1Text, 
                 new GridBagConstraints(1, 12, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
                                        new Insets(12, 70, 0, 10), 213, 5));
        this.add(dir2Text, 
                 new GridBagConstraints(1, 10, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
                                        new Insets(12, 70, 0, 10), 213, 5));
        this.add(dir1Text, 
                 new GridBagConstraints(1, 8, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
                                        new Insets(13, 70, 0, 10), 213, 5));
        this.add(passwordText, 
                 new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
                                        new Insets(12, 70, 0, 10), 213, 5));
        this.add(useridText, 
                 new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
                                        new Insets(12, 70, 0, 10), 213, 5));
        this.add(serverText, 
                 new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
                                        new Insets(12, 70, 0, 10), 213, 5));
        this.add(copyDir2Label, 
                 new GridBagConstraints(0, 14, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
                                        new Insets(12, 15, 0, 0), 4, 6));
        this.add(copyDir1Label, 
                 new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
                                        new Insets(12, 15, 0, 0), 4, 6));
        this.add(dir2Label, 
                 new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
                                        new Insets(12, 15, 0, 0), 29, 6));
        this.add(dir1Label, 
                 new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
                                        new Insets(13, 15, 0, 0), 32, 6));
        this.add(passwordLabel, 
                 new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
                                        new Insets(12, 15, 0, 0), 39, 6));
        this.add(useridLabel, 
                 new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
                                        new Insets(12, 15, 0, 0), 49, 6));
        this.add(serverLabel, 
                 new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
                                        new Insets(12, 15, 0, 0), 53, 6));
        this.add(envText, 
                 new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
                                        new Insets(5, 70, 0, 10), 213, 5));
        this.add(envLabel, 
                 new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 
                                        new Insets(5, 15, 0, 0), 25, 6));
    }
    
    public void onEntry(TraversableContext tc)
    {
      final SqrEnvData options =
        (SqrEnvData) tc.find(SqrEnvData.KEY);
      load(options);
    }

    public void onExit(TraversableContext tc)
    {
      final SqrEnvData options =
        (SqrEnvData) tc.find(SqrEnvData.KEY);
      commit(options);
    }

    private void load(SqrEnvData options)
    {
      envText.setText(options.getEnv());
      serverText.setText(options.getServer());
      useridText.setText(options.getUserid());
      passwordText.setText(options.getPassword());
      dir1Text.setText(options.getDir1());
      dir2Text.setText(options.getDir2());
      copyDir1Text.setText(options.getCopyDir1());
      copyDir2Text.setText(options.getCopyDir2());
    }

    private void commit(SqrEnvData options)
    {
       char[] password = passwordText.getPassword();
       
      options.setEnv(envText.getText());
      options.setServer(serverText.getText());
      options.setUserid(useridText.getText());
      options.setPassword(String.valueOf(password));
      options.setDir1(dir1Text.getText());
      options.setDir2(dir2Text.getText());
      options.setCopyDir1(copyDir1Text.getText());
      options.setCopyDir2(copyDir2Text.getText());
    }

}
