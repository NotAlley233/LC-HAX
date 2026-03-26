/*    */ package wtf.tatp.meowtils.util;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ 
/*    */ public class Wrapper
/*    */ {
/*    */   public static Minecraft getMinecraft() {
/* 10 */     return Minecraft.func_71410_x();
/*    */   }
/*    */   
/*    */   public static EntityPlayerSP getPlayer() {
/* 14 */     return (getMinecraft()).field_71439_g;
/*    */   }
/*    */   
/*    */   public static WorldClient getWorld() {
/* 18 */     return (getMinecraft()).field_71441_e;
/*    */   }
/*    */   
/*    */   public static void drawCenteredString(String text, int x, int y, int color) {
/* 22 */     (getMinecraft()).field_71466_p.func_78276_b(text, x - (getMinecraft()).field_71466_p.func_78256_a(text) / 2, y, color);
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/Wrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */