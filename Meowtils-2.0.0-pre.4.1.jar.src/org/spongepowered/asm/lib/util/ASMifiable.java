package org.spongepowered.asm.lib.util;

import java.util.Map;
import org.spongepowered.asm.lib.Label;

public interface ASMifiable {
  void asmify(StringBuffer paramStringBuffer, String paramString, Map<Label, String> paramMap);
}


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/org/spongepowered/asm/lib/util/ASMifiable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */