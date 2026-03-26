/*     */ package wtf.tatp.meowtils.util;
/*     */ 
/*     */ import com.sun.jna.Library;
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.Platform;
/*     */ import net.minecraft.client.Minecraft;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class User32Util
/*     */ {
/*     */   private static User32 user32Instance;
/*     */   
/*     */   private static User32 getUser32() {
/*  18 */     Minecraft mc = Minecraft.func_71410_x();
/*  19 */     if (user32Instance == null && Platform.isWindows() && mc.field_71439_g != null && mc.field_71441_e != null && mc.field_71415_G) {
/*     */       try {
/*  21 */         user32Instance = (User32)Native.loadLibrary("user32", User32.class);
/*  22 */       } catch (Throwable t) {
/*  23 */         t.printStackTrace();
/*     */       } 
/*     */     }
/*  26 */     return user32Instance;
/*     */   }
/*     */   
/*     */   public static boolean holdingRightClick() {
/*  30 */     Minecraft mc = Minecraft.func_71410_x();
/*  31 */     if (!mc.field_71415_G || mc.field_71462_r != null || mc.field_71441_e == null || mc.field_71439_g == null) return false;
/*     */     
/*  33 */     User32 u32 = getUser32();
/*  34 */     return (u32 != null && (u32.GetAsyncKeyState(2) & 0x8000) != 0);
/*     */   }
/*     */   public static boolean holdingLeftClick() {
/*  37 */     Minecraft mc = Minecraft.func_71410_x();
/*  38 */     if (!mc.field_71415_G || mc.field_71462_r != null || mc.field_71441_e == null || mc.field_71439_g == null) {
/*  39 */       return false;
/*     */     }
/*  41 */     User32 u32 = getUser32();
/*  42 */     return (u32 != null && (u32.GetAsyncKeyState(1) & 0x8000) != 0);
/*     */   }
/*     */   public static boolean isKeyDown(int lwjglKeyCode) {
/*  45 */     Minecraft mc = Minecraft.func_71410_x();
/*  46 */     if (!mc.field_71415_G || mc.field_71462_r != null || mc.field_71441_e == null || mc.field_71439_g == null) {
/*  47 */       return false;
/*     */     }
/*  49 */     User32 u32 = getUser32();
/*  50 */     if (u32 == null) return false;
/*     */     
/*  52 */     int vk = lwjglToVK(lwjglKeyCode);
/*  53 */     if (vk == 0) return false;
/*     */     
/*  55 */     return ((u32.GetAsyncKeyState(vk) & 0x8000) != 0);
/*     */   }
/*     */   
/*     */   private static int lwjglToVK(int lwjglKey) {
/*  59 */     switch (lwjglKey) { case 30:
/*  60 */         return 65;
/*  61 */       case 48: return 66;
/*  62 */       case 46: return 67;
/*  63 */       case 32: return 68;
/*  64 */       case 18: return 69;
/*  65 */       case 33: return 70;
/*  66 */       case 34: return 71;
/*  67 */       case 35: return 72;
/*  68 */       case 23: return 73;
/*  69 */       case 36: return 74;
/*  70 */       case 37: return 75;
/*  71 */       case 38: return 76;
/*  72 */       case 50: return 77;
/*  73 */       case 49: return 78;
/*  74 */       case 24: return 79;
/*  75 */       case 25: return 80;
/*  76 */       case 16: return 81;
/*  77 */       case 19: return 82;
/*  78 */       case 31: return 83;
/*  79 */       case 20: return 84;
/*  80 */       case 22: return 85;
/*  81 */       case 47: return 86;
/*  82 */       case 17: return 87;
/*  83 */       case 45: return 88;
/*  84 */       case 21: return 89;
/*  85 */       case 44: return 90;
/*  86 */       case 11: return 48;
/*  87 */       case 2: return 49;
/*  88 */       case 3: return 50;
/*  89 */       case 4: return 51;
/*  90 */       case 5: return 52;
/*  91 */       case 6: return 53;
/*  92 */       case 7: return 54;
/*  93 */       case 8: return 55;
/*  94 */       case 9: return 56;
/*  95 */       case 10: return 57;
/*  96 */       case 42: return 160;
/*  97 */       case 54: return 161;
/*  98 */       case 29: return 162;
/*  99 */       case 157: return 163;
/* 100 */       case 56: return 164;
/* 101 */       case 184: return 165;
/* 102 */       case 57: return 32;
/* 103 */       case 28: return 13;
/* 104 */       case 15: return 9;
/* 105 */       case 14: return 8;
/* 106 */       case 1: return 27;
/* 107 */       case 200: return 38;
/* 108 */       case 208: return 40;
/* 109 */       case 203: return 37;
/* 110 */       case 205: return 39; }
/* 111 */      return 0;
/*     */   }
/*     */   
/*     */   public static interface User32 extends Library {
/*     */     short GetAsyncKeyState(int param1Int);
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/User32Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */