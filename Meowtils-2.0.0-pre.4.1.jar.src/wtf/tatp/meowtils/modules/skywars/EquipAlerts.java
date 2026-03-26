/*    */ package wtf.tatp.meowtils.modules.skywars;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*    */ import wtf.tatp.meowtils.util.NameUtil;
/*    */ import wtf.tatp.meowtils.util.PlaySound;
/*    */ 
/*    */ public class EquipAlerts
/*    */   extends Module
/*    */ {
/*    */   private BooleanValue sound;
/*    */   private BooleanValue helmet;
/*    */   private BooleanValue chestplate;
/*    */   private BooleanValue leggings;
/*    */   private BooleanValue boots;
/* 30 */   private static final Map<UUID, ItemStack[]> lastArmor = (Map)new HashMap<>();
/*    */   public EquipAlerts() {
/* 32 */     super("EquipAlerts", "equipAlertsKey", "equipAlerts", Module.Category.Skywars);
/* 33 */     tooltip("Alerts when a player equips selected armor piece.");
/* 34 */     addBoolean(this.sound = new BooleanValue("Ping sound", "equipAlertsSound"));
/* 35 */     addBoolean(this.helmet = new BooleanValue("§bDiamond Helmet", "equipAlerts_helmet"));
/* 36 */     addBoolean(this.chestplate = new BooleanValue("§bDiamond Chestplate", "equipAlerts_chestplate"));
/* 37 */     addBoolean(this.leggings = new BooleanValue("§bDiamond Leggings", "equipAlerts_leggings"));
/* 38 */     addBoolean(this.boots = new BooleanValue("§bDiamond Boots", "equipAlerts_boots"));
/*    */   }
/*    */   @SubscribeEvent
/*    */   public void onClientTick(TickEvent.ClientTickEvent event) {
/* 42 */     Minecraft mc = Minecraft.func_71410_x();
/* 43 */     if (event.phase != TickEvent.Phase.END)
/* 44 */       return;  if (mc.field_71441_e == null || mc.field_71439_g == null)
/* 45 */       return;  if (!GamemodeUtil.skywarsGame)
/*    */       return; 
/* 47 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 48 */       if (player == mc.field_71439_g)
/*    */         continue; 
/* 50 */       ItemStack[] currentArmor = (ItemStack[])player.field_71071_by.field_70460_b.clone();
/* 51 */       ItemStack[] previousArmor = lastArmor.get(player.func_110124_au());
/*    */       
/* 53 */       if (previousArmor != null) {
/* 54 */         for (int i = 0; i < currentArmor.length; i++) {
/* 55 */           ItemStack before = previousArmor[i];
/* 56 */           ItemStack now = currentArmor[i];
/*    */           
/* 58 */           if (!ItemStack.func_77989_b(before, now) && now != null) {
/* 59 */             String armorName = allowedArmor(now.func_77973_b());
/* 60 */             if (armorName != null) {
/* 61 */               Meowtils.addMessage(NameUtil.getTabDisplayName(player.func_70005_c_()) + EnumChatFormatting.GRAY + " equipped " + EnumChatFormatting.DARK_AQUA + armorName);
/* 62 */               if (cfg.v.equipAlertsSound) {
/* 63 */                 PlaySound.getInstance().playPingSoundMedium();
/*    */               }
/*    */             } 
/*    */           } 
/*    */         } 
/*    */       }
/* 69 */       lastArmor.put(player.func_110124_au(), currentArmor);
/*    */     } 
/*    */   }
/*    */   private String allowedArmor(Item item) {
/* 73 */     if (item == Items.field_151161_ac && cfg.v.equipAlerts_helmet) return "Diamond Helmet"; 
/* 74 */     if (item == Items.field_151163_ad && cfg.v.equipAlerts_chestplate) return "Diamond Chestplate"; 
/* 75 */     if (item == Items.field_151173_ae && cfg.v.equipAlerts_leggings) return "Diamond Leggings"; 
/* 76 */     if (item == Items.field_151175_af && cfg.v.equipAlerts_boots) return "Diamond Boots"; 
/* 77 */     return null;
/*    */   }
/*    */   public static void clear() {
/* 80 */     lastArmor.clear();
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/skywars/EquipAlerts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */