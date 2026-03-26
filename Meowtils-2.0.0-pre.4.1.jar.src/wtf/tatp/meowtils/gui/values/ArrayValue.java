/*    */ package wtf.tatp.meowtils.gui.values;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ 
/*    */ public class ArrayValue
/*    */ {
/*    */   private final String name;
/*    */   private final List<String> modes;
/*    */   private String current;
/*    */   private final Field configField;
/*    */   private final Object configInstance;
/*    */   
/*    */   public ArrayValue(String name, List<String> modes, String fieldName) {
/* 19 */     if (modes.isEmpty()) throw new IllegalArgumentException("Mode list must not be empty!");
/*    */     
/* 21 */     this.name = name;
/* 22 */     this.modes = new ArrayList<>(modes);
/*    */     
/*    */     try {
/* 25 */       this.configInstance = cfg.v;
/* 26 */       this.configField = this.configInstance.getClass().getField(fieldName);
/* 27 */       this.configField.setAccessible(true);
/*    */       
/* 29 */       Object fieldValue = this.configField.get(this.configInstance);
/* 30 */       if (fieldValue instanceof String && this.modes.contains(fieldValue)) {
/* 31 */         this.current = (String)fieldValue;
/*    */       } else {
/* 33 */         this.current = this.modes.get(0);
/* 34 */         syncToConfig();
/*    */       } 
/* 36 */     } catch (Exception e) {
/* 37 */       throw new RuntimeException("Failed to link config field: " + fieldName, e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getName() {
/* 42 */     return this.name;
/*    */   }
/*    */   
/*    */   public List<String> getModes() {
/* 46 */     return Collections.unmodifiableList(this.modes);
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 50 */     return this.current;
/*    */   }
/*    */   
/*    */   public int getIndex() {
/* 54 */     return this.modes.indexOf(this.current);
/*    */   }
/*    */   
/*    */   public boolean is(String mode) {
/* 58 */     return this.current.equals(mode);
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 62 */     if (!this.modes.contains(value)) throw new IllegalArgumentException("Mode not found: " + value); 
/* 63 */     this.current = value;
/* 64 */     syncToConfig();
/*    */   }
/*    */   
/*    */   public void setValue(int index) {
/* 68 */     if (index < 0 || index >= this.modes.size())
/* 69 */       throw new IndexOutOfBoundsException("Mode index out of range"); 
/* 70 */     this.current = this.modes.get(index);
/* 71 */     syncToConfig();
/*    */   }
/*    */   
/*    */   private void syncToConfig() {
/*    */     try {
/* 76 */       this.configField.set(this.configInstance, this.current);
/* 77 */       cfg.save();
/* 78 */     } catch (IllegalAccessException e) {
/* 79 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/values/ArrayValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */