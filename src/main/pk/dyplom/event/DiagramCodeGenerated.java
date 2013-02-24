package pk.dyplom.event;

import pk.dyplom.codegen.ICodeGenerator;

public class DiagramCodeGenerated {

    public final String code;
    public final ICodeGenerator generator;

    public DiagramCodeGenerated(String code, ICodeGenerator generator) {
        this.code = code;
        this.generator = generator;
    }
}
