package teisaacs.sqrext.resources;

import java.util.ListResourceBundle;

public class SqrResBundle extends ListResourceBundle { 
        public Object[][] getContents() { 
            return contents; 
        } 
        static final Object[][] contents = 
                    { // LOCALIZE THIS 
                        {"EXTENSION_NAME", "Sqr Text Editor"}, 
                        {"EXTENSION_OWNER", "Todd Isaacs"} 
                        // END OF MATERIAL TO LOCALIZE 
                     }; 
    }
