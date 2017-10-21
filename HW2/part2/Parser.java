/* *** This file is given as part of the programming assignment. *** */

public class Parser {


    // tok is global to all these parsing methods;
    // scan just calls the scanner's scan method and saves the result in tok.
    private Token tok; // the current token
    private void scan() {
	tok = scanner.scan();
    }

    private Scan scanner;
    Parser(Scan scanner) {
	this.scanner = scanner;
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
	declaration_list();
	statement_list();
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
	mustbe(TK.ID);
	while( is(TK.COMMA) ) {
	    scan();
	    mustbe(TK.ID);
		}
    }

    // statement_list ::= {statement}
    private void statement_list() {
    	statement()
    }

    // statement ::= assignment | print | do | if
    private void statement() {
    	// complete function
    }

    // assignment ::= ref_id ’=’ expr
    private void assignment() {
    	//complete function
    }

    //  print ::= ’!’ expr
    private void print() {
    	//complete function
    }

   // do ::= ’<’ guarded_command ’>’
    private void do() {
    	//complete function
    }

    // if ::= ’[’ guarded_command { ’|’ guarded_command }
    private void if () {
    	//complete function
    }

    // ref_id ::= [ ’ ̃’ [ number ] ] id
    private void ref_id () {
    	//complete function

    }

    //   expr ::= term { addop term }
    private void expr () {
    	//complete function

    }

    //  guarded_command ::= expr ’:’ block
    private void guarded_command () {
    	//complete function
    }

    // term ::= factor { multop factor }
    private void term () {
    	// complete function

    }

    // factor ::= ’(’ expr ’)’ | ref_id | number
    private void factor () {
    	//complete function

    }

    private void addop() {
    	//complete function

    }

    private void multop() {
    	//complete function 
    	
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
