package teisaacs.sqrext.editor;

import java.io.IOException;

import java.net.URL;

import oracle.ide.Context;
import oracle.ide.Ide;
import oracle.ide.cmd.NewWorkspaceCommand;
import oracle.ide.log.LogManager;
import oracle.ide.log.LogPage;
import oracle.ide.model.NodeFactory;
import oracle.ide.model.Project;
import oracle.ide.model.ProjectContent;
import oracle.ide.model.ProjectMigrator;
import oracle.ide.model.ProjectVersion;
import oracle.ide.model.Workspace;
import oracle.ide.net.URLFactory;
import oracle.ide.wizard.Wizard;

import oracle.jdeveloper.builder.file.FileBuilderPanel;

public class SqrNewProjectWizard extends Wizard {
    public SqrNewProjectWizard() {
    }

    public boolean isAvailable(Context context) {
        return (context.getWorkspace() != null); 
    }

    public boolean invoke(Context context) {
        return createProject(context);
    }

    public String getShortLabel() {
        return "SQR Project";
    }
    
    boolean createProject(Context context) {
        FileBuilderPanel nfp = new FileBuilderPanel();
        
         nfp.setFileExtension(".jpr");
         nfp.setEnforceFileExtension(true);
         nfp.setShowExtension(true);
         URL u = nfp.runDialog("New SQR Project ");
          
         //log(dir.getPath()); 
         //log(dir.getFile());
        Workspace application = context.getWorkspace();
        
        //break this out to method and create another project method
        try {
            Project project = createProjectInApplication( 
              URLFactory.newFileURL( u.getFile() ), application );
            if ( project == null ) {
                log( "Can't create project." );
                return false;
            }
            
        } catch (Exception e) {
            // TODO
            return false;
        }

        return true;
    }

    
    
    Project createProjectInApplication( URL projectURL, Workspace app )
      throws Exception
    {
      
      final Project newProject = createNewProject( projectURL );
      if ( newProject == null ) return null;
      
      newProject.applyBatchChanges( new Runnable() {
        public void run() { initializeNewProject( newProject ); }                      
      });
      
      Project existingProject = (Project) NodeFactory.find( projectURL );
      if ( existingProject != null ) updateExistingProject( existingProject, newProject );
      
      Project projectToAdd = existingProject == null ? newProject : existingProject;
      app.add( projectToAdd, true );
      
      app.save();
      projectToAdd.save();
      
      return projectToAdd;
    }
    
    private void updateExistingProject( Project oldProject, Project newProject )
      throws IOException
    {
      oldProject.close();
      
      newProject.save();
      newProject.close();
      
      oldProject.open();
      
      // Force the old project back into the node cache.
      NodeFactory.recache( newProject.getURL(), newProject.getURL(), oldProject );
    }
    
    private Project createNewProject( URL newURL )
      throws IOException, IllegalAccessException, InstantiationException
    {
      // Try to clone the default project. If we can't find the default project
      // (it may not exist in some products based on the IDE), fall back to
      // just "raw" creation via the node factory findOrCreate method.
      
      Project defaultProject = Ide.getDefaultProject();    
      if ( defaultProject == null ) return (Project) NodeFactory.findOrCreate( Project.class, newURL );

      // It's possible changes were made to the default project, and it hasn't
      // been saved yet. Make sure we save it first.
      if ( defaultProject.isDirty() ) defaultProject.save();    
      
      return (Project) NodeFactory.clone( defaultProject, newURL );
    }
    
    private void initializeNewProject( Project project )
    {
      ProjectContent.initializeContentSets( project );
      ProjectVersion.initializeVersions( project, ProjectMigrator.MIGRATOR_NAME );
    }
    
    private void log( String message )
    {
      LogPage msgLog = LogManager.getLogManager().getMsgPage();
      msgLog.log( "(Workspace Wizard) " + message + "\n" );
    }
    
}
