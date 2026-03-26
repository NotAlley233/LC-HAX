/*    */ package com.sun.jna;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FromNativeContext
/*    */ {
/*    */   private Class<?> type;
/*    */   
/*    */   FromNativeContext(Class<?> javaType) {
/* 30 */     this.type = javaType;
/*    */   }
/*    */   
/*    */   public Class<?> getTargetType() {
/* 34 */     return this.type;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/com/sun/jna/FromNativeContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */