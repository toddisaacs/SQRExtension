package teisaacs.sqrext.editor;

public interface SqrTokens {
    int EOF = 0;
    int SPACE = EOF + 1;
    
    int OPERATORS = 900;
    
    int NOTEQUAL = 500;
    int FLOAT_LITERAL = 501;
    int STRING_LITERAL = 502;
    int INT_LITERAL = 503;
    int EQUAL = 40;
    int GREATER = 41;
    int LESS = 42;
    int EQEQUAL = 43;
    int EQLESS = 44;
    int EQGREATER = 45;
    int LESSGREATER = 46;
    int IDENTIFIER = 505;
    int KW_BREAK = 610;
    int KW_ELSE = 611;
    int KW_IF = KW_ELSE + 1;
    int KW_ENDIF = KW_IF + 1;
    int KW_SQR_PROCEDURE  = KW_ENDIF + 1;
    int KW_SQR_PROGRAM_STRUCTURE = KW_SQR_PROCEDURE + 1;
    int KW_SQR_SYSTEM_VARS = KW_SQR_PROGRAM_STRUCTURE + 1;
    int KW_SQR_FUNCTION = KW_SQR_SYSTEM_VARS + 1;
    int KW_SQR_FUNCTION_ARG = KW_SQR_FUNCTION + 1;
    int KW_SQR_SQL = KW_SQR_FUNCTION_ARG + 1;
    int KW_SQR_PREPROCESS = KW_SQR_SQL + 1;
    int SQR_COMMENT = KW_SQR_PREPROCESS + 1;
    
    
}
