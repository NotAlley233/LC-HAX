/*    */ package wtf.tatp.meowtils.modules.utility;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ 
/*    */ public class HotbarLock
/*    */   extends Module
/*    */ {
/*    */   private BooleanValue slot1;
/*    */   private BooleanValue slot2;
/*    */   private BooleanValue slot3;
/*    */   private BooleanValue slot4;
/*    */   private BooleanValue slot5;
/*    */   private BooleanValue slot6;
/*    */   private BooleanValue slot7;
/*    */   private BooleanValue slot8;
/*    */   private BooleanValue slot9;
/*    */   private ArrayValue mode;
/*    */   
/*    */   public HotbarLock() {
/* 31 */     super("HotbarLock", "hotbarLockKey", "hotbarLock", Module.Category.Utility);
/* 32 */     tooltip("Lock slots in your hotbar.");
/* 33 */     addBoolean(this.slot1 = new BooleanValue("Slot 1", "hotbarLock_slot1"));
/* 34 */     addBoolean(this.slot2 = new BooleanValue("Slot 2", "hotbarLock_slot2"));
/* 35 */     addBoolean(this.slot3 = new BooleanValue("Slot 3", "hotbarLock_slot3"));
/* 36 */     addBoolean(this.slot4 = new BooleanValue("Slot 4", "hotbarLock_slot4"));
/* 37 */     addBoolean(this.slot5 = new BooleanValue("Slot 5", "hotbarLock_slot5"));
/* 38 */     addBoolean(this.slot6 = new BooleanValue("Slot 6", "hotbarLock_slot6"));
/* 39 */     addBoolean(this.slot7 = new BooleanValue("Slot 7", "hotbarLock_slot7"));
/* 40 */     addBoolean(this.slot8 = new BooleanValue("Slot 8", "hotbarLock_slot8"));
/* 41 */     addBoolean(this.slot9 = new BooleanValue("Slot 9", "hotbarLock_slot9"));
/* 42 */     addArray(this.mode = new ArrayValue("Mode", Arrays.asList(new String[] { "Manual", "Swords" }, ), "hotbarLockMode"));
/*    */   }
/*    */   
/*    */   @SubscribeEvent(priority = EventPriority.HIGHEST)
/*    */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/* 47 */     Minecraft mc = Minecraft.func_71410_x();
/* 48 */     if (mc.field_71462_r != null)
/*    */       return; 
/* 50 */     if (Keyboard.isKeyDown(mc.field_71474_y.field_74316_C.func_151463_i()) && cfg.v.hotbarLockMode.equals("Manual") && isSlotLocked(mc.field_71439_g.field_71071_by.field_70461_c + 1)) {
/* 51 */       Meowtils.addMessage(EnumChatFormatting.RED + "Prevented you from dropping item in locked slot!");
/* 52 */     } else if (Keyboard.isKeyDown(mc.field_71474_y.field_74316_C.func_151463_i()) && cfg.v.hotbarLockMode.equals("Swords") && mc.field_71439_g.func_70694_bm().func_77973_b() instanceof net.minecraft.item.ItemSword) {
/* 53 */       Meowtils.addMessage(EnumChatFormatting.RED + "Prevented you from dropping your sword!");
/*    */     } 
/*    */   }
/*    */   public static boolean isSlotLocked(int slot) {
/* 57 */     switch (slot) { case 1:
/* 58 */         return cfg.v.hotbarLock_slot1;
/* 59 */       case 2: return cfg.v.hotbarLock_slot2;
/* 60 */       case 3: return cfg.v.hotbarLock_slot3;
/* 61 */       case 4: return cfg.v.hotbarLock_slot4;
/* 62 */       case 5: return cfg.v.hotbarLock_slot5;
/* 63 */       case 6: return cfg.v.hotbarLock_slot6;
/* 64 */       case 7: return cfg.v.hotbarLock_slot7;
/* 65 */       case 8: return cfg.v.hotbarLock_slot8;
/* 66 */       case 9: return cfg.v.hotbarLock_slot9; }
/*    */     
/* 68 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/utility/HotbarLock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */