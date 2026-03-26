/*     */ package wtf.tatp.meowtils.modules.bedwars;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ import wtf.tatp.meowtils.util.PlaySound;
/*     */ 
/*     */ public class ResourceTracker
/*     */   extends Module
/*     */ {
/*     */   private BooleanValue resourcePingSound;
/*     */   private BooleanValue trackIron;
/*     */   private BooleanValue trackGold;
/*     */   private BooleanValue trackDiamond;
/*     */   private BooleanValue trackEmerald;
/*     */   private BooleanValue hideDefault;
/*  28 */   private static final Map<Item, Integer> resourceAmount = new HashMap<>();
/*     */   
/*     */   public ResourceTracker() {
/*  31 */     super("ResourceTracker", "resourceTrackerKey", "resourceTracker", Module.Category.Bedwars);
/*  32 */     tooltip("Tracks resources in your inventory.");
/*  33 */     addBoolean(this.resourcePingSound = new BooleanValue("Ping sound", "resourceTrackerSound"));
/*  34 */     addBoolean(this.hideDefault = new BooleanValue("Hide default message", "resourceTrackerHideDefault"));
/*  35 */     addBoolean(this.trackIron = new BooleanValue("Track §7Iron Ingots", "trackIron"));
/*  36 */     addBoolean(this.trackGold = new BooleanValue("Track §6Gold Ingots", "trackGold"));
/*  37 */     addBoolean(this.trackDiamond = new BooleanValue("Track §bDiamonds", "trackDiamond"));
/*  38 */     addBoolean(this.trackEmerald = new BooleanValue("Track §2Emeralds", "trackEmerald"));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTick(TickEvent.ClientTickEvent event) {
/*  43 */     if (event.phase != TickEvent.Phase.END || this.mc.field_71439_g == null || this.mc.field_71441_e == null)
/*  44 */       return;  if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/*     */       return; 
/*  46 */     Map<Item, Integer> newAmount = new HashMap<>();
/*  47 */     newAmount.put(Items.field_151042_j, Integer.valueOf(0));
/*  48 */     newAmount.put(Items.field_151043_k, Integer.valueOf(0));
/*  49 */     newAmount.put(Items.field_151045_i, Integer.valueOf(0));
/*  50 */     newAmount.put(Items.field_151166_bC, Integer.valueOf(0));
/*     */     
/*  52 */     for (ItemStack stack : this.mc.field_71439_g.field_71071_by.field_70462_a) {
/*  53 */       if (stack != null && newAmount.containsKey(stack.func_77973_b())) {
/*  54 */         newAmount.put(stack.func_77973_b(), Integer.valueOf(((Integer)newAmount.get(stack.func_77973_b())).intValue() + stack.field_77994_a));
/*     */       }
/*     */     } 
/*     */     
/*  58 */     checkResourceChange(newAmount);
/*  59 */     resourceAmount.clear();
/*  60 */     resourceAmount.putAll(newAmount);
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onChatReceived(ClientChatReceivedEvent event) {
/*  66 */     String msg = event.message.func_150260_c();
/*  67 */     if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/*  68 */       return;  if (!cfg.v.resourceTrackerHideDefault)
/*     */       return; 
/*  70 */     if (msg.startsWith("+") && (msg.contains("Iron") || msg.contains("Gold") || msg.contains("Emerald") || msg.contains("Diamond"))) {
/*  71 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkResourceChange(Map<Item, Integer> newCounts) {
/*  76 */     for (Map.Entry<Item, Integer> entry : newCounts.entrySet()) {
/*  77 */       Item item = entry.getKey();
/*  78 */       int newCount = ((Integer)entry.getValue()).intValue();
/*  79 */       int oldCount = ((Integer)resourceAmount.getOrDefault(item, Integer.valueOf(0))).intValue();
/*     */       
/*  81 */       if (newCount != oldCount && trackItems(item)) {
/*  82 */         String itemName = getItemName(item);
/*  83 */         String prefix = (newCount > oldCount) ? (EnumChatFormatting.GREEN + "[+] ") : (EnumChatFormatting.RED + "[-] ");
/*  84 */         Meowtils.addMessage(prefix + itemName + " " + EnumChatFormatting.DARK_GRAY + "(" + newCount + ")");
/*  85 */         if (cfg.v.resourceTrackerSound) {
/*  86 */           PlaySound.getInstance().playPingSoundLevel();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getItemName(Item item) {
/*  93 */     if (item == Items.field_151042_j) return EnumChatFormatting.WHITE + "Iron"; 
/*  94 */     if (item == Items.field_151043_k) return EnumChatFormatting.GOLD + "Gold"; 
/*  95 */     if (item == Items.field_151045_i) return EnumChatFormatting.AQUA + "Diamond"; 
/*  96 */     if (item == Items.field_151166_bC) return EnumChatFormatting.DARK_GREEN + "Emerald"; 
/*  97 */     return "Unknown Item";
/*     */   }
/*     */   private boolean trackItems(Item item) {
/* 100 */     if (item == Items.field_151042_j) return this.trackIron.getState(); 
/* 101 */     if (item == Items.field_151043_k) return this.trackGold.getState(); 
/* 102 */     if (item == Items.field_151045_i) return this.trackDiamond.getState(); 
/* 103 */     if (item == Items.field_151166_bC) return this.trackEmerald.getState(); 
/* 104 */     return false;
/*     */   }
/*     */   public static void clear() {
/* 107 */     resourceAmount.clear();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/bedwars/ResourceTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */