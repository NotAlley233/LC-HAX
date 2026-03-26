/*     */ package wtf.tatp.meowtils.modules.utility;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ 
/*     */ public class AutoSwap
/*     */   extends Module
/*     */ {
/*     */   private Item lastItem;
/*  18 */   private int lastSlot = -1;
/*     */   
/*     */   private BooleanValue blocks;
/*     */   
/*     */   private BooleanValue projectiles;
/*     */   private BooleanValue pearls;
/*     */   private BooleanValue swords;
/*     */   private BooleanValue tools;
/*     */   private BooleanValue resources;
/*  27 */   private final List<String> ALLOWED_BLOCKS = Arrays.asList(new String[] { "stone", "grass", "dirt", "planks", "wool", "wood", "glass", "leaves", "clay", "cloth" });
/*  28 */   private final List<String> PROJECTILES = Arrays.asList(new String[] { "egg", "snowball" });
/*  29 */   private final List<String> PEARLS = Arrays.asList(new String[] { "pearl" });
/*  30 */   private final List<String> SWORDS = Arrays.asList(new String[] { "sword" });
/*  31 */   private final List<String> TOOLS = Arrays.asList(new String[] { "rod", "pickaxe", "axe", "shovel", "hoe", "flint_and_steel" });
/*  32 */   private final List<String> RESOURCES = Arrays.asList(new String[] { "265", "266", "388", "264" });
/*     */   
/*     */   public AutoSwap() {
/*  35 */     super("AutoSwap", "autoSwapKey", "autoSwap", Module.Category.Utility);
/*  36 */     tooltip("Automatically swap when running out of certain items.\n" + EnumChatFormatting.RED + "BETA");
/*  37 */     addBoolean(this.blocks = new BooleanValue("§7Blocks", "autoSwap_blocks"));
/*  38 */     addBoolean(this.projectiles = new BooleanValue("§6Projectiles", "autoSwap_projectiles"));
/*  39 */     addBoolean(this.resources = new BooleanValue("§2Resources", "autoSwap_resources"));
/*  40 */     addBoolean(this.pearls = new BooleanValue("§5Pearls", "autoSwap_pearls"));
/*  41 */     addBoolean(this.swords = new BooleanValue("§3Swords", "autoSwap_swords"));
/*  42 */     addBoolean(this.tools = new BooleanValue("§eTools", "autoSwap_tools"));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderTick(TickEvent.RenderTickEvent event) {
/*  47 */     if (this.mc.field_71441_e == null || this.mc.field_71439_g == null || event.phase != TickEvent.Phase.END)
/*  48 */       return;  if (this.mc.field_71462_r != null)
/*     */       return; 
/*  50 */     int slot = this.mc.field_71439_g.field_71071_by.field_70461_c;
/*  51 */     ItemStack held = this.mc.field_71439_g.field_71071_by.func_70301_a(slot);
/*     */     
/*  53 */     if (this.lastItem != null && slot == this.lastSlot && (held == null || held.field_77994_a < 1)) {
/*  54 */       swapItem(this.lastItem);
/*     */     }
/*     */     
/*  57 */     this.lastItem = (held != null) ? held.func_77973_b() : null;
/*  58 */     this.lastSlot = slot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void swapItem(Item lastItem) {
/*  75 */     if (lastItem == null)
/*     */       return; 
/*  77 */     int itemId = Item.func_150891_b(lastItem);
/*  78 */     String idString = String.valueOf(itemId);
/*  79 */     String lastId = lastItem.func_77658_a().toLowerCase();
/*     */     
/*  81 */     boolean isBlock = lastItem instanceof net.minecraft.item.ItemBlock;
/*  82 */     int current = this.mc.field_71439_g.field_71071_by.field_70461_c;
/*  83 */     List<String> category = null;
/*     */ 
/*     */     
/*  86 */     if (!isBlock)
/*  87 */       if (this.PROJECTILES.stream().anyMatch(lastId::contains) && !lastId.contains("leggings") && cfg.v.autoSwap_projectiles) { category = this.PROJECTILES; }
/*  88 */       else if (this.PEARLS.stream().anyMatch(lastId::contains) && cfg.v.autoSwap_pearls) { category = this.PEARLS; }
/*  89 */       else if (this.SWORDS.stream().anyMatch(lastId::contains) && cfg.v.autoSwap_swords) { category = this.SWORDS; }
/*  90 */       else if (this.TOOLS.stream().anyMatch(lastId::contains) && cfg.v.autoSwap_tools) { category = this.TOOLS; }
/*  91 */       else if (this.RESOURCES.stream().anyMatch(idString::contains) && cfg.v.autoSwap_resources) { category = this.RESOURCES; }
/*     */       else
/*     */       { return; }
/*     */        
/*  95 */     for (int offset = 1; offset <= 9; offset++) {
/*  96 */       int i = (current + offset) % 9;
/*  97 */       ItemStack stack = this.mc.field_71439_g.field_71071_by.func_70301_a(i);
/*  98 */       if (stack != null && stack.field_77994_a >= 1) {
/*     */         
/* 100 */         Item item = stack.func_77973_b();
/* 101 */         String id = item.func_77658_a().toLowerCase();
/* 102 */         String numericId = String.valueOf(Item.func_150891_b(item));
/*     */ 
/*     */         
/* 105 */         if (item == lastItem) {
/* 106 */           this.mc.field_71439_g.field_71071_by.field_70461_c = i;
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 111 */         if (isBlock && cfg.v.autoSwap_blocks && isValidBlock(stack)) {
/* 112 */           this.mc.field_71439_g.field_71071_by.field_70461_c = i;
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 117 */         if (category != null && (category.stream().anyMatch(id::contains) || category.stream().anyMatch(numericId::contains)) && !id.contains("leggings")) {
/* 118 */           this.mc.field_71439_g.field_71071_by.field_70461_c = i;
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private boolean isValidBlock(ItemStack stack) {
/* 125 */     if (!cfg.v.autoSwap_blocks) return false; 
/* 126 */     if (!(stack.func_77973_b() instanceof net.minecraft.item.ItemBlock)) return false; 
/* 127 */     String id = stack.func_77973_b().func_77658_a().toLowerCase();
/* 128 */     return this.ALLOWED_BLOCKS.stream().anyMatch(id::contains);
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/utility/AutoSwap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */