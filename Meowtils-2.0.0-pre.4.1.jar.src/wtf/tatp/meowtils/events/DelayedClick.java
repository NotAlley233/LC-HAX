/*    */ package wtf.tatp.meowtils.events;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.C0EPacketClickWindow;
/*    */ import wtf.tatp.meowtils.DelayedTask;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DelayedClick
/*    */ {
/*    */   public static void windowClick(int windowId, int slotId, int mouseButtonClicked, int mode, int delayTicks, EntityPlayer player) {
/* 16 */     short transactionID = player.field_71070_bA.func_75136_a(player.field_71071_by);
/* 17 */     ItemStack itemstack = player.field_71070_bA.func_75144_a(slotId, mouseButtonClicked, mode, player);
/* 18 */     new DelayedTask(() -> Minecraft.func_71410_x().func_147114_u().func_147297_a((Packet)new C0EPacketClickWindow(windowId, slotId, mouseButtonClicked, mode, itemstack, transactionID)), delayTicks);
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/events/DelayedClick.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */