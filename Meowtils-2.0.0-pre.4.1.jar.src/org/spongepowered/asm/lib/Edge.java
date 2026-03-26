package org.spongepowered.asm.lib;

class Edge {
  static final int NORMAL = 0;
  
  static final int EXCEPTION = 2147483647;
  
  int info;
  
  Label successor;
  
  Edge next;
}


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/org/spongepowered/asm/lib/Edge.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */