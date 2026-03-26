package org.spongepowered.asm.mixin.injection;

import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.refmap.IMixinContext;

public interface IInjectionPointContext {
  IMixinContext getContext();
  
  MethodNode getMethod();
  
  AnnotationNode getAnnotation();
}


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/org/spongepowered/asm/mixin/injection/IInjectionPointContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */