/*    */ package wtf.tatp.meowtils.util.anticheat.checks;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ public class KillauraBCheck
/*    */ {
/* 16 */   Minecraft mc = Minecraft.func_71410_x();
/*    */   
/* 18 */   private final Map<UUID, Integer> useItemTicks = new HashMap<>();
/* 19 */   private final Map<UUID, Integer> lastEatTicks = new HashMap<>();
/* 20 */   private final Map<UUID, Integer> violationLevels = new HashMap<>();
/*    */   
/*    */   private static final int EAT_TIMEOUT = 33;
/*    */   
/*    */   private static final int MIN_USE_TIME = 6;
/*    */   private boolean failedKillauraB = false;
/*    */   
/*    */   public void anticheatCheck(EntityPlayer player) {
/* 28 */     if (!cfg.v.detectKillaura)
/* 29 */       return;  if (player == this.mc.field_71439_g || player.field_70154_o != null)
/*    */       return; 
/* 31 */     UUID uuid = player.func_110124_au();
/* 32 */     long tick = this.mc.field_71441_e.func_82737_E();
/*    */     
/* 34 */     ItemStack heldItem = player.func_70694_bm();
/* 35 */     boolean isUsingItem = player.func_71039_bw();
/* 36 */     boolean isConsumable = (heldItem != null && isConsumable(heldItem.func_77973_b()));
/* 37 */     boolean isAttacking = (player.field_110158_av > 0);
/*    */     
/* 39 */     int useTime = ((Integer)this.useItemTicks.getOrDefault(uuid, Integer.valueOf(0))).intValue();
/* 40 */     if (isUsingItem && isConsumable) {
/* 41 */       useTime++;
/* 42 */       this.useItemTicks.put(uuid, Integer.valueOf(useTime));
/*    */     } else {
/* 44 */       if (useTime > 0) {
/* 45 */         this.lastEatTicks.put(uuid, Integer.valueOf((int)tick));
/*    */       }
/* 47 */       this.useItemTicks.put(uuid, Integer.valueOf(0));
/*    */     } 
/*    */     
/* 50 */     int lastEatTick = ((Integer)this.lastEatTicks.getOrDefault(uuid, Integer.valueOf(0))).intValue();
/* 51 */     int sinceLastEat = (int)(tick - lastEatTick);
/*    */     
/* 53 */     if (isAttacking && useTime > 6 && sinceLastEat < 33 && isConsumable) {
/* 54 */       int vl = ((Integer)this.violationLevels.getOrDefault(uuid, Integer.valueOf(0))).intValue() + 1;
/* 55 */       this.violationLevels.put(uuid, Integer.valueOf(vl));
/*    */       
/* 57 */       if (cfg.v.debugMessages) {
/* 58 */         Meowtils.addMessage(EnumChatFormatting.YELLOW + "[AntiCheat]: " + EnumChatFormatting.WHITE + player.func_70005_c_() + " swinging while using item | Use Time=" + useTime + " | Last Ate=" + sinceLastEat + " | vl=" + vl + " | Item=" + ((heldItem != null) ? heldItem.func_77973_b().getRegistryName() : "none"));
/*    */       }
/*    */       
/* 61 */       if (vl >= 8) {
/* 62 */         this.failedKillauraB = true;
/*    */       }
/*    */     } else {
/*    */       
/* 66 */       int vl = ((Integer)this.violationLevels.getOrDefault(uuid, Integer.valueOf(0))).intValue();
/* 67 */       if (vl > 0) this.violationLevels.put(uuid, Integer.valueOf(vl - 1)); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean failedKillauraB() {
/* 72 */     return this.failedKillauraB;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 76 */     this.failedKillauraB = false;
/* 77 */     this.useItemTicks.clear();
/* 78 */     this.lastEatTicks.clear();
/* 79 */     this.violationLevels.clear();
/*    */   }
/*    */   
/*    */   private boolean isConsumable(Item item) {
/* 83 */     return (item instanceof net.minecraft.item.ItemFood || item instanceof net.minecraft.item.ItemPotion || item instanceof net.minecraft.item.ItemBucketMilk);
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/anticheat/checks/KillauraBCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */