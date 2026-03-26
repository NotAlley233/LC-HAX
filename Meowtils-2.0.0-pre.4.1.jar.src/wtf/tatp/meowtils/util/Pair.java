/*    */ package wtf.tatp.meowtils.util;
/*    */ 
/*    */ public class Pair<K, V> {
/*    */   private final K key;
/*    */   private final V value;
/*    */   
/*    */   public Pair(K key, V value) {
/*  8 */     this.key = key;
/*  9 */     this.value = value;
/*    */   }
/*    */   public K getKey() {
/* 12 */     return this.key;
/*    */   } public V getValue() {
/* 14 */     return this.value;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/Pair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */