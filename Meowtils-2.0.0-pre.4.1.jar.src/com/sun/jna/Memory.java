/*     */ package com.sun.jna;
/*     */ 
/*     */ import com.sun.jna.internal.Cleaner;
/*     */ import java.io.Closeable;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Memory
/*     */   extends Pointer
/*     */   implements Closeable
/*     */ {
/*  56 */   private static final Map<Long, Reference<Memory>> allocatedMemory = new ConcurrentHashMap<Long, Reference<Memory>>();
/*     */ 
/*     */   
/*  59 */   private static final WeakMemoryHolder buffers = new WeakMemoryHolder();
/*     */   
/*     */   private final Cleaner.Cleanable cleanable;
/*     */   protected long size;
/*     */   
/*     */   public static void purge() {
/*  65 */     buffers.clean();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void disposeAll() {
/*  71 */     Collection<Reference<Memory>> refs = new ArrayList<Reference<Memory>>(allocatedMemory.values());
/*  72 */     for (Reference<Memory> r : refs) {
/*  73 */       Memory m = r.get();
/*  74 */       if (m != null) {
/*  75 */         m.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class SharedMemory
/*     */     extends Memory
/*     */   {
/*     */     public SharedMemory(long offset, long size) {
/*  88 */       this.size = size;
/*  89 */       this.peer = Memory.this.peer + offset;
/*     */     }
/*     */ 
/*     */     
/*     */     protected synchronized void dispose() {
/*  94 */       this.peer = 0L;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void boundsCheck(long off, long sz) {
/*  99 */       Memory.this.boundsCheck(this.peer - Memory.this.peer + off, sz);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 103 */       return super.toString() + " (shared from " + Memory.this.toString() + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Memory(long size) {
/* 113 */     this.size = size;
/* 114 */     if (size <= 0L) {
/* 115 */       throw new IllegalArgumentException("Allocation size must be greater than zero");
/*     */     }
/* 117 */     this.peer = malloc(size);
/* 118 */     if (this.peer == 0L) {
/* 119 */       throw new OutOfMemoryError("Cannot allocate " + size + " bytes");
/*     */     }
/* 121 */     allocatedMemory.put(Long.valueOf(this.peer), new WeakReference<Memory>(this));
/* 122 */     this.cleanable = Cleaner.getCleaner().register(this, new MemoryDisposer(this.peer));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Memory() {
/* 127 */     this.cleanable = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pointer share(long offset) {
/* 138 */     return share(offset, size() - offset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pointer share(long offset, long sz) {
/* 150 */     boundsCheck(offset, sz);
/* 151 */     return new SharedMemory(offset, sz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Memory align(int byteBoundary) {
/* 163 */     if (byteBoundary <= 0) {
/* 164 */       throw new IllegalArgumentException("Byte boundary must be positive: " + byteBoundary);
/*     */     }
/* 166 */     for (int i = 0; i < 32; i++) {
/* 167 */       if (byteBoundary == 1 << i) {
/* 168 */         long mask = byteBoundary - 1L ^ 0xFFFFFFFFFFFFFFFFL;
/*     */         
/* 170 */         if ((this.peer & mask) != this.peer) {
/* 171 */           long newPeer = this.peer + byteBoundary - 1L & mask;
/* 172 */           long newSize = this.peer + this.size - newPeer;
/* 173 */           if (newSize <= 0L) {
/* 174 */             throw new IllegalArgumentException("Insufficient memory to align to the requested boundary");
/*     */           }
/* 176 */           return (Memory)share(newPeer - this.peer, newSize);
/*     */         } 
/* 178 */         return this;
/*     */       } 
/*     */     } 
/* 181 */     throw new IllegalArgumentException("Byte boundary must be a power of two");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 187 */     this.peer = 0L;
/* 188 */     if (this.cleanable != null) {
/* 189 */       this.cleanable.clean();
/*     */     }
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected void dispose() {
/* 195 */     close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 200 */     clear(this.size);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean valid() {
/* 205 */     return (this.peer != 0L);
/*     */   }
/*     */   
/*     */   public long size() {
/* 209 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void boundsCheck(long off, long sz) {
/* 218 */     if (off < 0L) {
/* 219 */       throw new IndexOutOfBoundsException("Invalid offset: " + off);
/*     */     }
/* 221 */     if (off + sz > this.size) {
/* 222 */       String msg = "Bounds exceeds available space : size=" + this.size + ", offset=" + (off + sz);
/*     */       
/* 224 */       throw new IndexOutOfBoundsException(msg);
/*     */     } 
/*     */   }
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
/*     */   public void read(long bOff, byte[] buf, int index, int length) {
/* 242 */     boundsCheck(bOff, length * 1L);
/* 243 */     super.read(bOff, buf, index, length);
/*     */   }
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
/*     */   public void read(long bOff, short[] buf, int index, int length) {
/* 256 */     boundsCheck(bOff, length * 2L);
/* 257 */     super.read(bOff, buf, index, length);
/*     */   }
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
/*     */   public void read(long bOff, char[] buf, int index, int length) {
/* 270 */     boundsCheck(bOff, (length * Native.WCHAR_SIZE));
/* 271 */     super.read(bOff, buf, index, length);
/*     */   }
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
/*     */   public void read(long bOff, int[] buf, int index, int length) {
/* 284 */     boundsCheck(bOff, length * 4L);
/* 285 */     super.read(bOff, buf, index, length);
/*     */   }
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
/*     */   public void read(long bOff, long[] buf, int index, int length) {
/* 298 */     boundsCheck(bOff, length * 8L);
/* 299 */     super.read(bOff, buf, index, length);
/*     */   }
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
/*     */   public void read(long bOff, float[] buf, int index, int length) {
/* 312 */     boundsCheck(bOff, length * 4L);
/* 313 */     super.read(bOff, buf, index, length);
/*     */   }
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
/*     */   public void read(long bOff, double[] buf, int index, int length) {
/* 326 */     boundsCheck(bOff, length * 8L);
/* 327 */     super.read(bOff, buf, index, length);
/*     */   }
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
/*     */   public void read(long bOff, Pointer[] buf, int index, int length) {
/* 340 */     boundsCheck(bOff, (length * Native.POINTER_SIZE));
/* 341 */     super.read(bOff, buf, index, length);
/*     */   }
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
/*     */   public void write(long bOff, byte[] buf, int index, int length) {
/* 358 */     boundsCheck(bOff, length * 1L);
/* 359 */     super.write(bOff, buf, index, length);
/*     */   }
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
/*     */   public void write(long bOff, short[] buf, int index, int length) {
/* 372 */     boundsCheck(bOff, length * 2L);
/* 373 */     super.write(bOff, buf, index, length);
/*     */   }
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
/*     */   public void write(long bOff, char[] buf, int index, int length) {
/* 386 */     boundsCheck(bOff, (length * Native.WCHAR_SIZE));
/* 387 */     super.write(bOff, buf, index, length);
/*     */   }
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
/*     */   public void write(long bOff, int[] buf, int index, int length) {
/* 400 */     boundsCheck(bOff, length * 4L);
/* 401 */     super.write(bOff, buf, index, length);
/*     */   }
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
/*     */   public void write(long bOff, long[] buf, int index, int length) {
/* 414 */     boundsCheck(bOff, length * 8L);
/* 415 */     super.write(bOff, buf, index, length);
/*     */   }
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
/*     */   public void write(long bOff, float[] buf, int index, int length) {
/* 428 */     boundsCheck(bOff, length * 4L);
/* 429 */     super.write(bOff, buf, index, length);
/*     */   }
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
/*     */   public void write(long bOff, double[] buf, int index, int length) {
/* 442 */     boundsCheck(bOff, length * 8L);
/* 443 */     super.write(bOff, buf, index, length);
/*     */   }
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
/*     */   public void write(long bOff, Pointer[] buf, int index, int length) {
/* 456 */     boundsCheck(bOff, (length * Native.POINTER_SIZE));
/* 457 */     super.write(bOff, buf, index, length);
/*     */   }
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
/*     */   public byte getByte(long offset) {
/* 474 */     boundsCheck(offset, 1L);
/* 475 */     return super.getByte(offset);
/*     */   }
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
/*     */   public char getChar(long offset) {
/* 488 */     boundsCheck(offset, Native.WCHAR_SIZE);
/* 489 */     return super.getChar(offset);
/*     */   }
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
/*     */   public short getShort(long offset) {
/* 502 */     boundsCheck(offset, 2L);
/* 503 */     return super.getShort(offset);
/*     */   }
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
/*     */   public int getInt(long offset) {
/* 516 */     boundsCheck(offset, 4L);
/* 517 */     return super.getInt(offset);
/*     */   }
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
/*     */   public long getLong(long offset) {
/* 530 */     boundsCheck(offset, 8L);
/* 531 */     return super.getLong(offset);
/*     */   }
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
/*     */   public float getFloat(long offset) {
/* 544 */     boundsCheck(offset, 4L);
/* 545 */     return super.getFloat(offset);
/*     */   }
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
/*     */   public double getDouble(long offset) {
/* 558 */     boundsCheck(offset, 8L);
/* 559 */     return super.getDouble(offset);
/*     */   }
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
/*     */   public Pointer getPointer(long offset) {
/* 572 */     boundsCheck(offset, Native.POINTER_SIZE);
/* 573 */     return shareReferenceIfInBounds(super.getPointer(offset));
/*     */   }
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
/*     */   public ByteBuffer getByteBuffer(long offset, long length) {
/* 590 */     boundsCheck(offset, length);
/* 591 */     ByteBuffer b = super.getByteBuffer(offset, length);
/*     */ 
/*     */     
/* 594 */     buffers.put(b, this);
/* 595 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString(long offset, String encoding) {
/* 601 */     boundsCheck(offset, 0L);
/* 602 */     return super.getString(offset, encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWideString(long offset) {
/* 608 */     boundsCheck(offset, 0L);
/* 609 */     return super.getWideString(offset);
/*     */   }
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
/*     */   public void setByte(long offset, byte value) {
/* 626 */     boundsCheck(offset, 1L);
/* 627 */     super.setByte(offset, value);
/*     */   }
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
/*     */   public void setChar(long offset, char value) {
/* 640 */     boundsCheck(offset, Native.WCHAR_SIZE);
/* 641 */     super.setChar(offset, value);
/*     */   }
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
/*     */   public void setShort(long offset, short value) {
/* 654 */     boundsCheck(offset, 2L);
/* 655 */     super.setShort(offset, value);
/*     */   }
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
/*     */   public void setInt(long offset, int value) {
/* 668 */     boundsCheck(offset, 4L);
/* 669 */     super.setInt(offset, value);
/*     */   }
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
/*     */   public void setLong(long offset, long value) {
/* 682 */     boundsCheck(offset, 8L);
/* 683 */     super.setLong(offset, value);
/*     */   }
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
/*     */   public void setFloat(long offset, float value) {
/* 696 */     boundsCheck(offset, 4L);
/* 697 */     super.setFloat(offset, value);
/*     */   }
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
/*     */   public void setDouble(long offset, double value) {
/* 710 */     boundsCheck(offset, 8L);
/* 711 */     super.setDouble(offset, value);
/*     */   }
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
/*     */   public void setPointer(long offset, Pointer value) {
/* 724 */     boundsCheck(offset, Native.POINTER_SIZE);
/* 725 */     super.setPointer(offset, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setString(long offset, String value, String encoding) {
/* 730 */     boundsCheck(offset, (Native.getBytes(value, encoding)).length + 1L);
/* 731 */     super.setString(offset, value, encoding);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWideString(long offset, String value) {
/* 736 */     boundsCheck(offset, (value.length() + 1L) * Native.WCHAR_SIZE);
/* 737 */     super.setWideString(offset, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 742 */     return "allocated@0x" + Long.toHexString(this.peer) + " (" + this.size + " bytes)";
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void free(long p) {
/* 747 */     if (p != 0L) {
/* 748 */       Native.free(p);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static long malloc(long size) {
/* 753 */     return Native.malloc(size);
/*     */   }
/*     */ 
/*     */   
/*     */   public String dump() {
/* 758 */     return dump(0L, (int)size());
/*     */   }
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
/*     */   private Pointer shareReferenceIfInBounds(Pointer target) {
/* 772 */     if (target == null) {
/* 773 */       return null;
/*     */     }
/* 775 */     long offset = target.peer - this.peer;
/* 776 */     if (offset >= 0L && offset < this.size) {
/* 777 */       return share(offset);
/*     */     }
/* 779 */     return target;
/*     */   }
/*     */   
/*     */   private static final class MemoryDisposer
/*     */     implements Runnable
/*     */   {
/*     */     private long peer;
/*     */     
/*     */     public MemoryDisposer(long peer) {
/* 788 */       this.peer = peer;
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized void run() {
/*     */       try {
/* 794 */         Memory.free(this.peer);
/*     */       } finally {
/* 796 */         Memory.allocatedMemory.remove(Long.valueOf(this.peer));
/* 797 */         this.peer = 0L;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/com/sun/jna/Memory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */