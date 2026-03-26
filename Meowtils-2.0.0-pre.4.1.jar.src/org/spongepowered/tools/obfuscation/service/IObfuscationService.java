package org.spongepowered.tools.obfuscation.service;

import java.util.Collection;
import java.util.Set;

public interface IObfuscationService {
  Set<String> getSupportedOptions();
  
  Collection<ObfuscationTypeDescriptor> getObfuscationTypes();
}


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/org/spongepowered/tools/obfuscation/service/IObfuscationService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */