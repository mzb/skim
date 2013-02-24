package pk.dyplom.eval;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SyntaxValidatorTest {

    SyntaxValidator validator = new SyntaxValidator();

    @Test
    public void validatesFunctionName() {
        assertTrue(validator.isValidFunctionName("foo"));
        assertTrue(validator.isValidFunctionName("foo_bar"));
        assertTrue(validator.isValidFunctionName("$foo"));
        assertTrue(validator.isValidFunctionName("foo2"));

        assertFalse(validator.isValidFunctionName(""));
        assertFalse(validator.isValidFunctionName("foo bar"));
        assertFalse(validator.isValidFunctionName("2foo"));
    }
    
    @Test
    public void validatesParamList() {
        assertTrue("no params", validator.isValidParamList(""));
        assertTrue("single param", validator.isValidParamList("a:int"));
        assertTrue("several params", validator.isValidParamList("a:string, b:int[10]"));
        assertTrue("no space separator", validator.isValidParamList("a:real,b:bool"));

        assertFalse("no type", validator.isValidParamList("a"));
        assertFalse("no array size", validator.isValidParamList("a:int[]"));
        assertFalse("invalid type", validator.isValidParamList("a:invalid"));
        assertFalse(validator.isValidParamList("a,"));
        assertFalse(validator.isValidParamList(","));
        assertFalse("literal", validator.isValidParamList("2"));
    }
    
    @Test
    public void validatesExpression() {
        assertTrue(validator.isValidExpression("true"));
        assertTrue(validator.isValidExpression("x < 0"));
        assertTrue(validator.isValidExpression("x > 0 || x < 0 && x == 0"));
        assertTrue(validator.isValidExpression("0"));

        assertFalse(validator.isValidExpression(""));
        assertFalse(validator.isValidExpression(" "));
        assertFalse(validator.isValidExpression("if () {}"));
        assertFalse(validator.isValidExpression("return"));
    }
    
    @Test
    public void validatesVariableName() {
        assertTrue(validator.isValidVariableName("foo"));
        assertTrue(validator.isValidVariableName("foo_bar"));
        assertTrue(validator.isValidVariableName("$foo"));
        assertTrue(validator.isValidVariableName("foo2"));

        assertFalse(validator.isValidVariableName(""));
        assertFalse(validator.isValidVariableName("foo bar"));
        assertFalse(validator.isValidVariableName("2foo"));
        assertFalse(validator.isValidVariableName("var"));
    }

    @Test
    public void validatesReturnExpression() {
        assertTrue("empty", validator.isValidReturnExpression(""));

        assertFalse("bool literal", validator.isValidReturnExpression("true"));
        assertFalse("num literal", validator.isValidReturnExpression("0"));
        assertFalse("expression", validator.isValidReturnExpression("x < 0"));
        assertFalse("statement", validator.isValidReturnExpression("if () {}"));
        assertFalse("statement", validator.isValidReturnExpression("return"));
    }
    
    @Test
    public void validatesVariableDeclarations() {
        assertTrue(validator.containsValidVarDeclarations("var i:int;"));
        assertTrue(validator.containsValidVarDeclarations("var s:string[10];"));
                
        assertFalse("no semicolon", validator.containsValidVarDeclarations("var i:int"));
        assertFalse("no type", validator.containsValidVarDeclarations("var i;"));
        assertFalse("invalid type", validator.containsValidVarDeclarations("var i:invalid;"));
        assertFalse("no array size", validator.containsValidVarDeclarations("var a:int[];"));
        assertFalse("declaration + initialization", validator.containsValidVarDeclarations("var i:int = 23;"));
    }
}
