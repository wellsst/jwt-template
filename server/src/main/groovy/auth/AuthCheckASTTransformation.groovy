package auth

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class AuthCheckASTTransformation implements ASTTransformation {

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        println "*************************************************************"
    }
}
