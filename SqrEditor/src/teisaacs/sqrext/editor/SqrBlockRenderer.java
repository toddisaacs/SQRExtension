package teisaacs.sqrext.editor;

import oracle.javatools.buffer.TextBuffer;
import oracle.javatools.editor.language.LexerBlockRenderer;
import oracle.javatools.parser.Lexer;

public class SqrBlockRenderer extends LexerBlockRenderer implements SqrTokens {

    public SqrBlockRenderer(TextBuffer textBuffer) {
        super(textBuffer);
    }

    protected Lexer createLexer() {
        return new SqrLexer();
    }

    protected String mapTokenToStyleName(int token) {
        switch (token) {
        case SQR_COMMENT:
            return SqrStyles.SQR_COMMENT_STYLE;
        case OPERATORS:
            return SqrStyles.SQR_OPERATORS_STYLE;

        case KW_BREAK:
        case KW_ELSE:
        case KW_IF:
        case KW_ENDIF:
            return SqrStyles.SQR_KEYWORD_STYLE;

        case KW_SQR_PROCEDURE:
            return SqrStyles.SQR_PROCEDURE_STYLE;
            //return SqrStyles.SQR_PROCEDURE_STYLE;
        case STRING_LITERAL:
            return SqrStyles.SQR_STRING_LIT_STYLE;
        case KW_SQR_FUNCTION_ARG:
            return SqrStyles.SQR_FUNCTION_ARG_STYLE;
        case KW_SQR_FUNCTION:
            return SqrStyles.SQR_FUNCTION_STYLE;
        case KW_SQR_PREPROCESS:
            return SqrStyles.SQR_PREPROCESS_STYLE;

        }
        return getDefaultStyleName();
    }
}
