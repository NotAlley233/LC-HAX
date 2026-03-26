package org.spongepowered.asm.service;

public interface IMixinServiceBootstrap {
  String getName();
  
  String getServiceClassName();
  
  void bootstrap();
}


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/org/spongepowered/asm/service/IMixinServiceBootstrap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */