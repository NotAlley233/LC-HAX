/*    */ package wtf.tatp.meowtils.modules.advanced;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*    */ 
/*    */ public class InventoryMove
/*    */   extends Module
/*    */ {
/*    */   private NumberValue clickDelay;
/*    */   public static boolean clickedItem;
/*    */   
/*    */   public InventoryMove() {
/* 15 */     super("InventoryMove", "inventoryMoveKey", "inventoryMove", Module.Category.Advanced);
/* 16 */     tooltip("Allows you to move while in your inventory, attempts to not use illegitimate methods.\n§cWARNING: §cLow §cdelay §cmight §cflag. §cMay §calso §cnot §cwork §cwith §csome §cclient §cInventoryManagers.");
/* 17 */     addValue(this.clickDelay = new NumberValue("Click delay", 0.0D, 4.0D, 1.0D, "ticks", "inventoryMoveClickDelay", int.class));
/*    */   }
/*    */   public static boolean isMovementKey(KeyBinding key) {
/* 20 */     Minecraft mc = Minecraft.func_71410_x();
/* 21 */     return (key == mc.field_71474_y.field_74351_w || key == mc.field_71474_y.field_74368_y || key == mc.field_71474_y.field_74370_x || key == mc.field_71474_y.field_74366_z || key == mc.field_71474_y.field_74314_A);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isInSurvivalInventory() {
/* 29 */     Minecraft mc = Minecraft.func_71410_x();
/* 30 */     return (mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiInventory && !mc.field_71442_b.func_78758_h());
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/advanced/InventoryMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */