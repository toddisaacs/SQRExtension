package teisaacs.sqrext.editor;

import java.net.URL;

import oracle.ide.Context;

import oracle.ide.wizard.Wizard;

import javax.swing.JPanel;

import oracle.ide.cmd.NewWorkspaceCommand;
import oracle.ide.log.LogManager;
import oracle.ide.log.LogPage;
import oracle.ide.model.Workspace;

import oracle.ide.net.URLFactory;

import oracle.jdeveloper.builder.file.FileBuilderPanel;

public class SqrNewWorkspaceWizard extends Wizard{
    public SqrNewWorkspaceWizard() {
       
    }

    public boolean invoke(Context context) {
        if (!createWorkSpace(context)) {
            return false;
        } else {
            return true;
        }
    }

    

    boolean createWorkSpace(Context context) {
        FileBuilderPanel nfp = new FileBuilderPanel();
        
         nfp.setFileExtension(".jws");
         nfp.setEnforceFileExtension(true);
         nfp.setShowExtension(true);
         URL dir = nfp.runDialog("New SQR Workspace ");
          
         //log(dir.getPath()); 
         //log(dir.getFile());
        Workspace application; 
        
        //break this out to method and create another project method
        try {
            application = NewWorkspaceCommand.createEmptyWorkspace( 
              context, URLFactory.newFileURL(  dir.getFile() ) );
            if ( application == null ) {
                log( "Can't create application." );
                return false;
            }
            
        } catch (IllegalAccessException e) {
            // TODO
            return false;
        } catch (InstantiationException e) {
            // TODO
            return false;
        }

        return true;
    }


    /**
     * Displays the wizard as avilable in the Gallery.
     * @param context
     * @return 
     */
    public boolean isAvailable(Context context) {
        return true;
    }
    
    private void log( String message )
    {
      LogPage msgLog = LogManager.getLogManager().getMsgPage();
      msgLog.log( "(Workspace Wizard) " + message + "\n" );
    }

    public String getShortLabel() {
        return "test";
    }
}
