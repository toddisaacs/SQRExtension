package teisaacs.sqrext.editor;

import java.awt.Color;

import java.awt.Font;

import oracle.javatools.editor.language.BaseStyle;
import oracle.javatools.editor.language.BuiltInStyles;
import oracle.javatools.editor.language.StyleRegistry;

public class SqrStyles {

    public static final String SQR_COMMENT_STYLE = "sqr-comments-style";
    public static final String SQR_KEYWORD_STYLE = "sqr-keyword-style";
    public static final String SQR_PROCEDURE_STYLE = "sqr-procedure-style";
    public static final String SQR_IDENTIFIER_STYLE = "sqr-identifier-style";
    public static final String SQR_OPERATORS_STYLE = "sqr-operators-style";
    public static final String SQR_STRING_LIT_STYLE = 
        "sqr-string-literal-style";
    public static final String SQR_FUNCTION_STYLE = "sqr-function-style";
    public static final String SQR_FUNCTION_ARG_STYLE = 
        "sqr-function-arg-style";
    public static final String SQR_PREPROCESS_STYLE = "sqr-preprocess-style";

    public static final String[] STYLE_NAMES = 
        new String[] { SQR_COMMENT_STYLE, SQR_KEYWORD_STYLE, 
                       SQR_PROCEDURE_STYLE, SQR_IDENTIFIER_STYLE, 
                       SQR_OPERATORS_STYLE, SQR_STRING_LIT_STYLE, 
                       SQR_FUNCTION_STYLE, SQR_FUNCTION_ARG_STYLE, 
                       SQR_PREPROCESS_STYLE };


    public static BaseStyle sqrCommentStyle;
    public static BaseStyle sqrKeywordStyle;
    public static BaseStyle sqrProcedureStyle;
    public static BaseStyle sqrIdentifierStyle;
    public static BaseStyle sqrOperatorsStyle;
    public static BaseStyle sqrStringStyle;
    public static BaseStyle sqrFunctionStyle;
    public static BaseStyle sqrFunctionArgStyle;
    public static BaseStyle sqrPreprocessStyle;

    public SqrStyles(StyleRegistry styleRegistry) {
        reloadStyles(styleRegistry);
    }

    public static String[] getStyleNames() {
        return STYLE_NAMES;
    }

    public void reloadStyles(StyleRegistry styleRegistry) {
        //BuiltInStyles builtInStyles = new BuiltInStyles(styleRegistry);

        //sqrPlainStyle = builtInStyles.plainStyle;

        sqrCommentStyle = styleRegistry.lookupStyle(SQR_COMMENT_STYLE);
        if (sqrCommentStyle == null) {
            sqrCommentStyle = 
                    styleRegistry.createStyle(SQR_COMMENT_STYLE, "Sqr Comment", 
                                              BuiltInStyles.BUILTIN_COMMENT_STYLE, 
                                              new Color(0, 128, 0));
        }

        sqrKeywordStyle = styleRegistry.lookupStyle(SQR_KEYWORD_STYLE);
        if (sqrKeywordStyle == null) {
            sqrKeywordStyle = 
                    styleRegistry.createStyle(SQR_KEYWORD_STYLE, "Sqr keyword", 
                                              BuiltInStyles.BUILTIN_KEYWORD_STYLE);
        }

        sqrProcedureStyle = styleRegistry.lookupStyle(SQR_PROCEDURE_STYLE);
        if (sqrProcedureStyle == null) {
            sqrProcedureStyle = 
                    styleRegistry.createStyle(SQR_PROCEDURE_STYLE, "Sqr Structure", 
                                              BuiltInStyles.BUILTIN_KEYWORD_STYLE, 
                                              Color.DARK_GRAY);
        }

        sqrIdentifierStyle = styleRegistry.lookupStyle(SQR_IDENTIFIER_STYLE);
        if (sqrIdentifierStyle == null) {
            sqrIdentifierStyle = 
                    styleRegistry.createStyle(SQR_IDENTIFIER_STYLE, 
                                              "Sqr Identifier", 
                                              BuiltInStyles.BUILTIN_IDENTIFIER_STYLE);
        }

        sqrOperatorsStyle = styleRegistry.lookupStyle(SQR_OPERATORS_STYLE);
        if (sqrOperatorsStyle == null) {
            sqrOperatorsStyle = 
                    styleRegistry.createStyle(SQR_OPERATORS_STYLE, "Sqr Operators", 
                                              BuiltInStyles.BUILTIN_BRACE_STYLE, 
                                              Color.RED);
        }

        sqrStringStyle = styleRegistry.lookupStyle(SQR_STRING_LIT_STYLE);
        if (sqrStringStyle == null) {
            sqrStringStyle = 
                    styleRegistry.createStyle(SQR_STRING_LIT_STYLE, "Sqr Strings", 
                                              BuiltInStyles.BUILTIN_STRING_STYLE, 
                                              new Color(0, 128,128));
        }

        sqrFunctionArgStyle = 
                styleRegistry.lookupStyle(SQR_FUNCTION_ARG_STYLE);
        if (sqrFunctionArgStyle == null) {
            sqrFunctionArgStyle = 
                    styleRegistry.createStyle(SQR_FUNCTION_ARG_STYLE, 
                                              "Sqr Function Args", 
                                              BuiltInStyles.BUILTIN_HEADER_STYLE, 
                                              new Color(0,0,128));
        }

        sqrFunctionStyle = styleRegistry.lookupStyle(SQR_FUNCTION_STYLE);
        if (sqrFunctionStyle == null) {
            sqrFunctionStyle = 
                    styleRegistry.createStyle(SQR_FUNCTION_STYLE, "Sqr Function", 
                                              BuiltInStyles.BUILTIN_HEADER_STYLE, 
                                              new Color(0,128,128));
        }

        sqrPreprocessStyle = styleRegistry.lookupStyle(SQR_PREPROCESS_STYLE);
        if (sqrPreprocessStyle == null) {
            sqrPreprocessStyle = 
                    styleRegistry.createStyle(SQR_PREPROCESS_STYLE, 
                                              "Sqr Preprocessor Commands", 
                                              BuiltInStyles.BUILTIN_KEYWORD_STYLE, 
                                              Color.DARK_GRAY);
        }

    }

}
