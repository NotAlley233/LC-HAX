/*    */ package wtf.tatp.meowtils.modules.bedwars;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.inventory.GuiChest;
/*    */ import net.minecraft.inventory.ContainerChest;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.inventory.Slot;
/*    */ import net.minecraftforge.client.event.GuiOpenEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import wtf.tatp.meowtils.DelayedTask;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*    */ import wtf.tatp.meowtils.mixins.GuiContainerAccessor;
/*    */ 
/*    */ public class AutoClaim extends Module {
/*    */   public static boolean autoClaimPending;
/*    */   private NumberValue delay;
/*    */   
/*    */   public AutoClaim() {
/* 21 */     super("AutoClaim", "autoClaimKey", "autoClaim", Module.Category.Bedwars);
/* 22 */     tooltip("Automatically accepts when claiming tickets from\nSlumber Hotel NPC's.");
/* 23 */     addValue(this.delay = new NumberValue("Delay", 100.0D, 1000.0D, 50.0D, "ms", "autoClaimDelay", int.class));
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onGuiOpen(GuiOpenEvent event) {
/* 28 */     Minecraft mc = Minecraft.func_71410_x();
/* 29 */     int clickDelay = cfg.v.autoClaimDelay / 50;
/*    */     
/* 31 */     if (event.gui instanceof GuiChest) {
/* 32 */       GuiChest chest = (GuiChest)event.gui;
/* 33 */       if (chest.field_147002_h instanceof ContainerChest) {
/* 34 */         ContainerChest container = (ContainerChest)chest.field_147002_h;
/* 35 */         IInventory containerName = container.func_85151_d();
/*    */         
/* 37 */         if ("Item Submission".equals(containerName.func_145748_c_().func_150260_c())) {
/* 38 */           autoClaimPending = true;
/* 39 */           new DelayedTask(() -> { if (mc.field_71462_r instanceof GuiChest) { Slot slot = container.func_75139_a(11); ((GuiContainerAccessor)chest).clickSlot(slot, slot.field_75222_d, 0, 0); autoClaimPending = false; }  }clickDelay);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/*    */         }
/* 46 */         else if ("Trade Emeralds".equals(containerName.func_145748_c_().func_150260_c())) {
/* 47 */           new DelayedTask(() -> { autoClaimPending = true; if (mc.field_71462_r instanceof GuiChest) { Slot slot = container.func_75139_a(22); ((GuiContainerAccessor)chest).clickSlot(slot, slot.field_75222_d, 0, 0); autoClaimPending = false; }  }clickDelay);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/bedwars/AutoClaim.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */