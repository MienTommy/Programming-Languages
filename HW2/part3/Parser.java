/* *** This file is given as part of the programming assignment. *** */
import java.util.* ;

public class Parser {


    // tok is global to all these parsing methods;
    // scan just calls the scanner's scan method and saves the result in tok.
    private Token tok; // the current token
    private Scan scanner;
    private ArrayList<ArrayList<String>> symbolTable; // symbolTalbe is an arrayList of arrayLists

    private void scan() {
    tok = scanner.scan();
    }

    Parser(Scan scanner) {
    this.scanner = scanner;
    symbolTable = new ArrayList<ArrayList<String>>(); // Initiliazing symbol table
    scan();
    program();
    if( tok.kind != TK.EOF )
        parse_error("junk after logical end of program");
    }

    // program ::= block
    private void program() {
    block();
    }

    // block ::= declaration_list statement_list
    private void block(){
    ArrayList<String> scope = new ArrayList<String>(); // new scope upon entering block
    symbolTable.add(scope);
    
    declaration_list();
    statement_list();

    symbolTable.remove(symbolTable.size() - 1); // removing scope upon exiting block
    }

    // declaration_list ::= {declaration}
    private void declaration_list() {
    // below checks whether tok is in first set of declaration.
    // here, that's easy since there's only one token kind in the set.
    // in other places, though, there might be more.
    // so, you might want to write a general function to handle that.
        while( is(TK.DECLARE) ) {
            declaration();
        }
    }

    // declaration ::= ’@’ id { ’,’ id }
    private void declaration() {
        mustbe(TK.DECLARE);
        // Loop through the array list to check if token string is already declared.
        if ( is(TK.ID) )
        {
            // getting the current scope (arrayList) of the symbolTable 
            ArrayList<String> currentScope = symbolTable.get(symbolTable.size() - 1 );
            if (currentScope.contains(tok.string))
            {
                System.err.println("redeclaration of variable " + tok.string);
            }

            else
            {
                currentScope.add(tok.string);
            }
        } 
        mustbe(TK.ID);
        while( is(TK.COMMA) )
        {
            scan();
                if ( is(TK.ID) )
                {
                    // getting the current scope (arrayList) of the symbolTable 
                    ArrayList<String> currentScope = symbolTable.get(symbolTable.size() - 1 );
                    if (currentScope.contains(tok.string))
                    {
                        System.err.println("redeclaration of variable " + tok.string);
                    }

                    else
                    {
                        currentScope.add(tok.string);
                    }
                } 
            mustbe(TK.ID);
        }
    }

    // statement_list ::= {statement}
    private void statement_list() {
        while (is(TK.ID) || is(TK.TILDE) || is(TK.PRINT) || is(TK.DO) || is(TK.IF))
        {
            statement();
        }
    }

    // statement ::= assignment | print | do | if
    private void statement() {
        if (is(TK.ID) || is (TK.TILDE))
        {
            assignment();
        }

        else if (is(TK.PRINT))
        {
            print();
        }

        else if (is(TK.DO))
        {
            Do();
        }

        else 
        {
            If();
        }
    }

    // assignment ::= ref_id ’=’ expr
    private void assignment() {
        ref_id();
        mustbe(TK.ASSIGN);
        expr();
    }

    //  print ::= ’!’ expr
    private void print() {
        scan();
        expr();
    }

   // do ::= ’<’ guarded_command ’>’
    private void Do() {
        scan();
        guarded_command();
        mustbe(TK.ENDDO);
    }

    // if ::= ’[’ guarded_command { ’|’ guarded_command } [ ’%’ block ] ’]’
    private void If () {
        scan();
        guarded_command();

        while (is(TK.ELSEIF))
        {
            scan();
            guarded_command();
        }

        if (is(TK.ELSE))
        {
            scan();
            block();
        }

        mustbe(TK.ENDIF);
    }

    // ref_id ::= [ ’ ̃’ [ number ] ] id
    private void ref_id () {
        if (is(TK.TILDE))
        {
            scan();

            if (is(TK.NUM))
            {
                scan();
            }
        }
        mustbe(TK.ID);
    }

    //   expr ::= term { addop term }
    private void expr () {
        term();

        while (addop())
        {
            term();
        }
    }

    //  guarded_command ::= expr ’:’ block
    private void guarded_command () {
        expr();
        mustbe(TK.THEN);
        block();
    }

    // term ::= factor { multop factor }
    private void term () {
        factor();

        while (multop())
        {
            factor();
        }
    }

    // factor ::= ’(’ expr ’)’ | ref_id | number
    private void factor () {
        if (is(TK.LPAREN))
        {
            scan();
            expr();
            mustbe(TK.RPAREN);
        }
        else if((is(TK.ID)) || is(TK.TILDE))
        {
            ref_id();
        }

        else 
        {
            mustbe(TK.NUM);
        }

    }
    // addop ::= ’+’ | ’-’
    private boolean addop() {
        if (is(TK.PLUS) || is(TK.MINUS))
        {
            scan();
            return true;
        }
        return false;

    }

    // multop ::= ’*’ | ’/’
    private boolean multop() {
        if (is(TK.TIMES) || is(TK.DIVIDE))
        {
            scan();
            return true;
        }
        return false;
    }

    // is current token what we want?
    private boolean is(TK tk) {
        return tk == tok.kind;
    }

    // ensure current token is tk and skip over it.
    private void mustbe(TK tk) {
    if( tok.kind != tk ) {
        System.err.println( "mustbe: want " + tk + ", got " +
                    tok);
        parse_error( "missing token (mustbe)" );
    }
    scan();
    }

    private void parse_error(String msg) {
    System.err.println( "can't parse: line "
                + tok.lineNumber + " " + msg );
    System.exit(1);
    }
}