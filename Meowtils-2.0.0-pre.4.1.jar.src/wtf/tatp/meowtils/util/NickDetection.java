/*    */ package wtf.tatp.meowtils.util;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class NickDetection
/*    */ {
/*    */   public static boolean isNicked(EntityPlayer player) {
/*  9 */     if (player == null || player.func_146103_bH() == null) return false;
/*    */     
/* 11 */     UUID uuid = player.func_146103_bH().getId();
/* 12 */     if (uuid == null) return false;
/*    */     
/* 14 */     return (uuid.version() == 1);
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/NickDetection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */