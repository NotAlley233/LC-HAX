/*     */ package com.sun.jna.internal;
/*     */ 
/*     */ import java.lang.ref.PhantomReference;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Cleaner
/*     */ {
/*  41 */   private static final Cleaner INSTANCE = new Cleaner();
/*     */   
/*     */   public static Cleaner getCleaner() {
/*  44 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private final ReferenceQueue<Object> referenceQueue;
/*     */   private final Thread cleanerThread;
/*     */   private CleanerRef firstCleanable;
/*     */   
/*     */   private Cleaner() {
/*  52 */     this.referenceQueue = new ReferenceQueue();
/*  53 */     this.cleanerThread = new Thread()
/*     */       {
/*     */         public void run() {
/*     */           while (true) {
/*     */             try {
/*  58 */               Reference<? extends Object> ref = (Reference)Cleaner.this.referenceQueue.remove();
/*  59 */               if (ref instanceof Cleaner.CleanerRef) {
/*  60 */                 ((Cleaner.CleanerRef)ref).clean();
/*     */               }
/*  62 */             } catch (InterruptedException ex) {
/*     */ 
/*     */               
/*     */               break;
/*     */             
/*     */             }
/*  68 */             catch (Exception ex) {
/*  69 */               Logger.getLogger(Cleaner.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */             } 
/*     */           } 
/*     */         }
/*     */       };
/*  74 */     this.cleanerThread.setName("JNA Cleaner");
/*  75 */     this.cleanerThread.setDaemon(true);
/*  76 */     this.cleanerThread.start();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Cleanable register(Object obj, Runnable cleanupTask) {
/*  82 */     return add(new CleanerRef(this, obj, this.referenceQueue, cleanupTask));
/*     */   }
/*     */   
/*     */   private synchronized CleanerRef add(CleanerRef ref) {
/*  86 */     if (this.firstCleanable == null) {
/*  87 */       this.firstCleanable = ref;
/*     */     } else {
/*  89 */       ref.setNext(this.firstCleanable);
/*  90 */       this.firstCleanable.setPrevious(ref);
/*  91 */       this.firstCleanable = ref;
/*     */     } 
/*  93 */     return ref;
/*     */   }
/*     */   
/*     */   private synchronized boolean remove(CleanerRef ref) {
/*  97 */     boolean inChain = false;
/*  98 */     if (ref == this.firstCleanable) {
/*  99 */       this.firstCleanable = ref.getNext();
/* 100 */       inChain = true;
/*     */     } 
/* 102 */     if (ref.getPrevious() != null) {
/* 103 */       ref.getPrevious().setNext(ref.getNext());
/*     */     }
/* 105 */     if (ref.getNext() != null) {
/* 106 */       ref.getNext().setPrevious(ref.getPrevious());
/*     */     }
/* 108 */     if (ref.getPrevious() != null || ref.getNext() != null) {
/* 109 */       inChain = true;
/*     */     }
/* 111 */     ref.setNext(null);
/* 112 */     ref.setPrevious(null);
/* 113 */     return inChain;
/*     */   }
/*     */   
/*     */   private static class CleanerRef extends PhantomReference<Object> implements Cleanable {
/*     */     private final Cleaner cleaner;
/*     */     private final Runnable cleanupTask;
/*     */     private CleanerRef previous;
/*     */     private CleanerRef next;
/*     */     
/*     */     public CleanerRef(Cleaner cleaner, Object referent, ReferenceQueue<? super Object> q, Runnable cleanupTask) {
/* 123 */       super(referent, q);
/* 124 */       this.cleaner = cleaner;
/* 125 */       this.cleanupTask = cleanupTask;
/*     */     }
/*     */     
/*     */     public void clean() {
/* 129 */       if (this.cleaner.remove(this)) {
/* 130 */         this.cleanupTask.run();
/*     */       }
/*     */     }
/*     */     
/*     */     CleanerRef getPrevious() {
/* 135 */       return this.previous;
/*     */     }
/*     */     
/*     */     void setPrevious(CleanerRef previous) {
/* 139 */       this.previous = previous;
/*     */     }
/*     */     
/*     */     CleanerRef getNext() {
/* 143 */       return this.next;
/*     */     }
/*     */     
/*     */     void setNext(CleanerRef next) {
/* 147 */       this.next = next;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Cleanable {
/*     */     void clean();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/com/sun/jna/internal/Cleaner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */