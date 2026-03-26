/*    */ package wtf.tatp.meowtils.gui.values;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.util.List;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ public class BooleanValue
/*    */ {
/*    */   private final String name;
/*    */   private boolean value;
/*    */   private final Field configField;
/*    */   private final Object configInstance;
/*    */   private final List<BooleanValue> group;
/*    */   
/*    */   public BooleanValue(String name, String fieldName) {
/* 16 */     this(name, fieldName, null);
/*    */   }
/*    */   
/*    */   public BooleanValue(String name, String fieldName, List<BooleanValue> group) {
/* 20 */     this.name = name;
/* 21 */     this.group = group;
/*    */     
/*    */     try {
/* 24 */       this.configInstance = cfg.v;
/* 25 */       this.configField = this.configInstance.getClass().getField(fieldName);
/* 26 */       this.configField.setAccessible(true);
/*    */       
/* 28 */       Object fieldValue = this.configField.get(this.configInstance);
/* 29 */       if (fieldValue instanceof Boolean) {
/* 30 */         this.value = ((Boolean)fieldValue).booleanValue();
/*    */       } else {
/* 32 */         this.value = false;
/* 33 */         syncToConfig();
/*    */       }
/*    */     
/* 36 */     } catch (Exception e) {
/* 37 */       throw new RuntimeException("Failed to link config field: " + fieldName, e);
/*    */     } 
/*    */     
/* 40 */     if (this.group != null) {
/* 41 */       this.group.add(this);
/*    */     }
/* 43 */     syncToConfig();
/*    */   }
/*    */   
/*    */   public String getName() {
/* 47 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean getState() {
/* 51 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setState(boolean state) {
/* 55 */     if (state == this.value)
/*    */       return; 
/* 57 */     if (state && this.group != null) {
/* 58 */       for (BooleanValue other : this.group) {
/* 59 */         if (other != this && other.getState()) {
/* 60 */           other.setState(false);
/*    */         }
/*    */       } 
/*    */     }
/*    */     
/* 65 */     this.value = state;
/* 66 */     syncToConfig();
/*    */   }
/*    */   
/*    */   public void toggle() {
/* 70 */     setState(!this.value);
/*    */   }
/*    */   
/*    */   private void syncToConfig() {
/*    */     try {
/* 75 */       this.configField.set(this.configInstance, Boolean.valueOf(this.value));
/* 76 */       cfg.save();
/* 77 */     } catch (IllegalAccessException e) {
/* 78 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/values/BooleanValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */