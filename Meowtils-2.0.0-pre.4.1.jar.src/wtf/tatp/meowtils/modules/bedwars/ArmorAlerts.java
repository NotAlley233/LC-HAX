/*     */ package wtf.tatp.meowtils.modules.bedwars;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ import wtf.tatp.meowtils.util.NameUtil;
/*     */ import wtf.tatp.meowtils.util.NotifyUtil;
/*     */ import wtf.tatp.meowtils.util.PlaySound;
/*     */ import wtf.tatp.meowtils.util.TeamUtil;
/*     */ 
/*     */ public class ArmorAlerts
/*     */   extends Module {
/*     */   private BooleanValue armorPingSound;
/*     */   private BooleanValue detectDiamondArmor;
/*     */   private BooleanValue detectIronArmor;
/*     */   private BooleanValue detectChainArmor;
/*     */   private BooleanValue alertParty;
/*  31 */   private int tickCounter = 0;
/*     */   
/*     */   Minecraft mc;
/*     */   
/*     */   private static Map<String, String> currentPlayerArmorMap;
/*     */   List<Integer> cache;
/*     */   
/*     */   public ArmorAlerts() {
/*  39 */     super("ArmorAlerts", "armorDetectorKey", "armorDetector", Module.Category.Bedwars);
/*  40 */     tooltip("Alerts you when players buy an armor upgrade.");
/*  41 */     addBoolean(this.armorPingSound = new BooleanValue("Ping sound", "armorDetectorSound"));
/*  42 */     addBoolean(this.detectChainArmor = new BooleanValue("§7Chainmail Armor", "detectChainArmor"));
/*  43 */     addBoolean(this.detectIronArmor = new BooleanValue("Iron Armor", "detectIronArmor"));
/*  44 */     addBoolean(this.detectDiamondArmor = new BooleanValue("§bDiamond Armor", "detectDiamondArmor"));
/*  45 */     this.cache = new ArrayList<>();
/*  46 */     this; playerItemMap = new HashMap<>();
/*  47 */     this; playerItemNameMap = new HashMap<>();
/*  48 */     this.mc = Minecraft.func_71410_x();
/*  49 */     currentPlayerArmorMap = new HashMap<>();
/*  50 */     this.you = (EntityPlayer)(Minecraft.func_71410_x()).field_71439_g;
/*     */   }
/*     */   static Map<String, Integer> playerItemMap; static Map<String, String> playerItemNameMap; EntityPlayer you;
/*     */   public boolean isPlayerInGame() {
/*  54 */     return (this.mc.field_71439_g != null && this.mc.field_71441_e != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTick(TickEvent.ClientTickEvent event) {
/*  60 */     if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/*  61 */       return;  if (event.phase == TickEvent.Phase.END) {
/*  62 */       Minecraft mc = Minecraft.func_71410_x();
/*  63 */       if (mc.field_71441_e != null) {
/*  64 */         List<EntityPlayer> playerList = mc.field_71441_e.field_73010_i;
/*  65 */         if (++this.tickCounter % 20 != 0)
/*     */           return; 
/*  67 */         for (EntityPlayer player : playerList) {
/*  68 */           String playerName = player.func_70005_c_();
/*  69 */           processArmor(player);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void processArmor(EntityPlayer player) {
/*  76 */     ItemStack[] armorInventory = player.field_71071_by.field_70460_b;
/*  77 */     ItemStack leggingsStack = armorInventory[1];
/*  78 */     String playerName = player.func_70005_c_();
/*  79 */     if (TeamUtil.inSpectator())
/*  80 */       return;  if (TeamUtil.ignoreSelf(playerName))
/*  81 */       return;  if (TeamUtil.ignoreTeam(playerName))
/*  82 */       return;  if (leggingsStack != null && leggingsStack.func_77973_b() instanceof ItemArmor && ((ItemArmor)leggingsStack.func_77973_b()).field_77881_a == 2) {
/*  83 */       String armorType = ((ItemArmor)leggingsStack.func_77973_b()).func_82812_d().name();
/*     */       
/*  85 */       if (!currentPlayerArmorMap.containsKey(playerName)) {
/*  86 */         currentPlayerArmorMap.put(playerName, armorType);
/*     */       }
/*  88 */       String currentArmorType = currentPlayerArmorMap.get(playerName);
/*  89 */       if (cfg.v.detectDiamondArmor && !currentArmorType.equals(armorType) && armorType == "DIAMOND") {
/*  90 */         String check = NameUtil.getTabDisplayName(playerName);
/*  91 */         if (check != "NONE") {
/*  92 */           Meowtils.addMessage(check + EnumChatFormatting.GRAY + " purchased " + EnumChatFormatting.AQUA + "Diamond Armor");
/*  93 */           currentPlayerArmorMap.put(playerName, armorType);
/*  94 */           if (cfg.v.armorDetectorSound) {
/*  95 */             PlaySound.getInstance().playPingSound();
/*     */           }
/*  97 */           if (cfg.v.notify && cfg.v.notifyArmorAlerts) {
/*  98 */             NotifyUtil.notifyParty(playerName + " purchased Diamond Armor");
/*     */           }
/*     */         }
/*     */       
/* 102 */       } else if (cfg.v.detectIronArmor && !currentArmorType.equals(armorType) && armorType == "IRON") {
/* 103 */         String check = NameUtil.getTabDisplayName(playerName);
/* 104 */         if (check != "NONE") {
/* 105 */           Meowtils.addMessage(check + EnumChatFormatting.GRAY + " purchased " + EnumChatFormatting.WHITE + "Iron Armor");
/* 106 */           currentPlayerArmorMap.put(playerName, armorType);
/* 107 */           if (cfg.v.armorDetectorSound) {
/* 108 */             PlaySound.getInstance().playPingSound();
/*     */           }
/* 110 */           if (cfg.v.notify && cfg.v.notifyArmorAlerts) {
/* 111 */             NotifyUtil.notifyParty(playerName + " purchased Iron Armor");
/*     */           }
/*     */         }
/*     */       
/* 115 */       } else if (cfg.v.detectChainArmor && !currentArmorType.equals(armorType) && armorType == "CHAIN") {
/* 116 */         String check = NameUtil.getTabDisplayName(playerName);
/* 117 */         if (check != "NONE") {
/* 118 */           Meowtils.addMessage(check + EnumChatFormatting.GRAY + " purchased " + EnumChatFormatting.DARK_GRAY + "Chainmail Armor");
/* 119 */           currentPlayerArmorMap.put(playerName, armorType);
/* 120 */           if (cfg.v.armorDetectorSound) {
/* 121 */             PlaySound.getInstance().playPingSound();
/*     */           }
/* 123 */           if (cfg.v.notify && cfg.v.notifyArmorAlerts) {
/* 124 */             NotifyUtil.notifyParty(playerName + " purchased Chainmail Armor");
/*     */           }
/*     */         }
/*     */       
/* 128 */       } else if (!currentArmorType.equals(armorType) && armorType == "LEATHER") {
/* 129 */         String check = NameUtil.getTabDisplayName(playerName);
/* 130 */         if (check != "NONE") {
/* 131 */           currentPlayerArmorMap.put(playerName, armorType);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void clear() {
/* 138 */     currentPlayerArmorMap.clear();
/* 139 */     playerItemMap.clear();
/* 140 */     playerItemNameMap.clear();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/bedwars/ArmorAlerts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */