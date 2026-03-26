/*    */ package wtf.tatp.meowtils.modules.render;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*    */ import wtf.tatp.meowtils.util.User32Util;
/*    */ 
/*    */ 
/*    */ public class FakeAutoBlock
/*    */   extends Module
/*    */ {
/*    */   private BooleanValue blockhitOnly;
/*    */   private BooleanValue killauraOnly;
/*    */   private NumberValue delay;
/* 21 */   private static long lastClickTime = 0L;
/*    */   private static boolean isKillaura = false;
/*    */   
/*    */   public FakeAutoBlock() {
/* 25 */     super("FakeAutoBlock", "fakeAutoBlockKey", "fakeAutoBlock", Module.Category.Render);
/* 26 */     tooltip("Renders your sword blocked while swinging.\n" + EnumChatFormatting.YELLOW + "§eNote: " + EnumChatFormatting.YELLOW + "Require " + EnumChatFormatting.YELLOW + "mouse " + EnumChatFormatting.YELLOW + "down " + EnumChatFormatting.YELLOW + "only " + EnumChatFormatting.YELLOW + "works " + EnumChatFormatting.YELLOW + "on " + EnumChatFormatting.YELLOW + "windows");
/* 27 */     addBoolean(this.blockhitOnly = new BooleanValue("Require mouse down", "blockhitOnly"));
/* 28 */     addBoolean(this.killauraOnly = new BooleanValue("Killaura only", "fakeAutoBlockKillauraOnly"));
/* 29 */     addValue(this.delay = new NumberValue("Release delay", 0.0D, 1000.0D, 50.0D, "ms", "fakeAutoBlockDelay", int.class));
/*    */   }
/*    */   public static boolean isSword(ItemStack item) {
/* 32 */     return (item != null && item.func_77973_b() instanceof net.minecraft.item.ItemSword);
/*    */   }
/*    */   
/*    */   public static boolean isKillaura() {
/* 36 */     return isKillaura;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTick(TickEvent.ClientTickEvent event) {
/* 41 */     if (event.phase != TickEvent.Phase.END)
/*    */       return; 
/* 43 */     Minecraft mc = Minecraft.func_71410_x();
/* 44 */     if (mc.field_71439_g == null || mc.field_71441_e == null)
/*    */       return; 
/* 46 */     boolean left = User32Util.holdingLeftClick();
/* 47 */     boolean right = User32Util.holdingRightClick();
/*    */     
/* 49 */     if (left || right) {
/* 50 */       lastClickTime = System.currentTimeMillis();
/*    */     }
/*    */     
/* 53 */     long timeSinceClick = System.currentTimeMillis() - lastClickTime;
/*    */     
/* 55 */     boolean swing = mc.field_71439_g.field_82175_bq;
/* 56 */     isKillaura = (swing && !left && !right && timeSinceClick > cfg.v.fakeAutoBlockDelay);
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/render/FakeAutoBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */