package teisaacs.sqrext.editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import javax.ide.*;
import javax.ide.wizard.Wizard;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import oracle.bali.ewt.dialog.JEWTDialog;

import oracle.ide.Ide;
import oracle.ide.cmd.FileOpenHistory;
import oracle.ide.dialogs.DialogUtil;
import oracle.ide.dialogs.MessageDialog;
import oracle.ide.editor.EditorManager;
import oracle.ide.exception.ChangeVetoException;
import oracle.ide.model.Node;
import oracle.ide.model.NodeFactory;
import javax.ide.command.Context;

import oracle.ide.component.NewFilePanel;
import oracle.ide.log.LogManager;
import oracle.ide.log.LogPage;
import oracle.ide.net.URLChooser;
import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;

import oracle.jdeveloper.builder.file.FileBuilderPanel;
import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;


public class SqrNewFileWizard  extends JPanel implements Wizard {


    

   
    
    public SqrNewFileWizard() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    public String getShortLabel() {
        return "SQR Editor";
    }
       

    public boolean invoke(final Context context) {
       FileBuilderPanel nfp = new FileBuilderPanel();
       
        nfp.setFileExtension(".sqr");
        nfp.setEnforceFileExtension(true);
        URL dir = nfp.runDialog("New SQR File ");
        
        Node node;
        // It's a new node
        try
        {
          node = NodeFactory.findOrCreate( dir );;
        }
        catch (IllegalAccessException ille )
        {
          log( "Failed to open "+ dir );
          return false;
        }
        catch (InstantiationException ie )
        {
          log( "Failed to open " + dir );
          return false;
        }
        
        log(dir.toString());
        
        return true;
    }

    /**
     *  determins if this is isAvailable (greyed out or not) in the 
     *  New Gallery dialog.
     * @param context
     * @return
     */
    public boolean isAvailable(Context context) {
        return (context != null);
    }
    
   
    private boolean createSqrFile(Context context, String dir, String name) {
        URL u = URLFactory.newFileURL( new File( dir.toString()  ) );
                if ( URLFileSystem.exists( u ) )
                {
                  log( "A file called " + dir.toString() +
                    " already exists in the specified location" );
                  return false;
                }

                if ( !URLFileSystem.hasSuffix( u, ".sqr" ) )
                {
                  // Hmm
                  name = name + ".py";
                 // m_tfName.setText( name );
                  return createSqrFile( context, dir, name );
                  
                }

                
                Node node;
                // It's a new node
                try
                {
                  node = NodeFactory.findOrCreate( u );;
                }
                catch (IllegalAccessException ille )
                {
                  log( "Failed to open "+u );
                  return false;
                }
                catch (InstantiationException ie )
                {
                  log( "Failed to open "+u );
                  return false;
                }
                
                
                
                
                return true;
    }
   
    
    private void jbInit() throws Exception {


    }
    
    private void log( String message )
    {
      LogPage msgLog = LogManager.getLogManager().getMsgPage();
      msgLog.log( "(ESDK Sample - CreateStructure) " + message + "\n" );
    }

}
