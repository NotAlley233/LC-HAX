package org.spongepowered.asm.mixin.injection.code;

import org.spongepowered.asm.mixin.injection.IInjectionPointContext;

public interface ISliceContext extends IInjectionPointContext {
  MethodSlice getSlice(String paramString);
}


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/org/spongepowered/asm/mixin/injection/code/ISliceContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */