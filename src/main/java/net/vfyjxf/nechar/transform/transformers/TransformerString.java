/*
 * 基于Towdium的JustEnoughCharacters(https://github.com/Towdium/JustEnoughCharacters/blob/1.12.0/src/main/java/me/towdium/
 * jecharacters/transform/transformers/TransformerString.java)
 * 原文件协议为MIT
 */

package net.vfyjxf.nechar.transform.transformers;

import net.vfyjxf.nechar.NechConfig;
import net.vfyjxf.nechar.transform.Transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

public class TransformerString extends Transformer.Configurable {

    public TransformerString() {
        reload();
    }

    @Override
    protected String[] getDefault() {
        return NechConfig.defaultTransformerStringList;
    }

    @Override
    protected String[] getAdditional() {
        return NechConfig.transformerStringAdditionalList;
    }

    @Override
    protected String getName() {
        return "string contains";
    }

    @Override
    protected void transform(MethodNode n) {
        Transformer.transformInvoke(
            n,
            "java/lang/String",
            "contains",
            "net/vfyjxf/nechar/utils/Match",
            "contains",
            "(Ljava/lang/String;Ljava/lang/CharSequence;)Z",
            false,
            Opcodes.INVOKESTATIC,
            "(Ljava/lang/Object;)Z",
            "(Ljava/lang/String;)Z");
    }
}
