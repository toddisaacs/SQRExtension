package teisaacs.sqrext.editor;

import oracle.javatools.editor.language.LanguageModule;
import oracle.javatools.editor.language.LanguageSupport;
import oracle.javatools.editor.language.StyleRegistry;

public class SqrLanguageModule extends LanguageModule 
    
    {
    public SqrLanguageModule() {
    }

    public String getPresentationName() {
        return "SQR Editor";
    }

    public String[] getSupportedFileTypes() {
        String[] suffix = { "sqr", "sqc" };
        return suffix;
    }

    public String[] getStyleNames() {
        return SqrStyles.getStyleNames();
    }

    public String getContentSample() {

        StringBuffer buffer = new StringBuffer();
        buffer.append("#include 'stderror.sqc'\n");
        buffer.append("#define max_ssn_changes 3000\n");
        buffer.append("\n");
        buffer.append("!---------------------------------------------------\n");
        buffer.append("! Procedure:      begin-heading\n");           
        buffer.append("!---------------------------------------------------\n");
        buffer.append("begin-heading 8\n");
        buffer.append("  #include 'stdhdg01.sqc'\n");
        buffer.append("  print 'Process Date:'           (3,3)\n");
        buffer.append("  Print $run_date                 (+0,20)\n");
        buffer.append("end-heading \n");
        buffer.append("\n");
        buffer.append("!---------------------------------------------------\n");
        buffer.append("! Procedure:      init-report\n");               
        buffer.append("!---------------------------------------------------\n");
        buffer.append("begin-procedure init-report (#var1, $var2, :retunr_var)\n");
        buffer.append("  move 'BEN306A' to $ReportID\n");
        buffer.append("\n");
        buffer.append("  create-array name=ssn-changes size={max_ssn_changes}\n");
        buffer.append("    field=emplid:char\n");
        buffer.append("    field=dependent:char\n");
        buffer.append("\n");
        buffer.append("  do get-user-variables\n");  
        buffer.append("end-procedure init-report\n");

        return buffer.toString();

    }


    public void registerStyles(StyleRegistry styleRegistry) {
        SqrStyles styles = new SqrStyles(styleRegistry);
    }

    public LanguageSupport createLanguageSupport() {
        return new SqrLanguageSupport();
    }

    public String toString() {
        return "SQR";
    }
}
