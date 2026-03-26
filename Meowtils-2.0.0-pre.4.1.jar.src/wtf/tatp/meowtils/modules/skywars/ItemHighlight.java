/*     */ package wtf.tatp.meowtils.modules.skywars;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ 
/*     */ public class ItemHighlight extends Module {
/*  24 */   private static Set<String> blacklist = new HashSet<>();
/*  25 */   private static Set<String> safelist = new HashSet<>();
/*  26 */   private static final File BLACKLIST_FILE = new File((Minecraft.func_71410_x()).field_71412_D, "meowtils/items/itemblacklist.json");
/*  27 */   private static final File SAFELIST_FILE = new File((Minecraft.func_71410_x()).field_71412_D, "meowtils/items/itemsafelist.json");
/*     */   private BooleanValue skywarsOnly;
/*     */   private BooleanValue bestOnly;
/*     */   
/*     */   public ItemHighlight() {
/*  32 */     super("ItemHighlight", "itemHighlightKey", "itemHighlight", Module.Category.Skywars);
/*  33 */     tooltip("Highlights important items in your inventory.\n" + EnumChatFormatting.RED + "BETA");
/*  34 */     addBoolean(this.skywarsOnly = new BooleanValue("Skywars only", "itemHighlightSkywarsOnly"));
/*  35 */     addBoolean(this.bestOnly = new BooleanValue("Highlight best only", "itemHighlightBestOnly"));
/*     */     
/*  37 */     loadItemList();
/*     */   }
/*     */   
/*     */   private void loadItemList() {
/*  41 */     Gson gson = new Gson();
/*     */     try {
/*  43 */       if (!BLACKLIST_FILE.exists()) {
/*  44 */         saveItemList(BLACKLIST_FILE, DEFAULT_BLACKLIST);
/*     */       }
/*  46 */       if (!SAFELIST_FILE.exists()) {
/*  47 */         saveItemList(SAFELIST_FILE, DEFAULT_SAFELIST);
/*     */       }
/*     */       
/*  50 */       List<String> loadedBlacklist = (List<String>)gson.fromJson(new FileReader(BLACKLIST_FILE), (new TypeToken<List<String>>() {  }).getType());
/*  51 */       blacklist = (loadedBlacklist != null) ? new HashSet<>(loadedBlacklist) : new HashSet<>();
/*     */       
/*  53 */       List<String> loadedSafelist = (List<String>)gson.fromJson(new FileReader(SAFELIST_FILE), (new TypeToken<List<String>>() {  }).getType());
/*  54 */       safelist = (loadedSafelist != null) ? new HashSet<>(loadedSafelist) : new HashSet<>();
/*     */     }
/*  56 */     catch (IOException e) {
/*  57 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void saveItemList(File file, List<String> list) throws IOException {
/*  62 */     Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
/*  63 */     file.getParentFile().mkdirs();
/*  64 */     try (FileWriter writer = new FileWriter(file)) {
/*  65 */       gson.toJson(list, writer);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Set<String> getItemBlacklist() {
/*  70 */     return blacklist;
/*     */   }
/*     */   
/*     */   public static Set<String> getItemSafelist() {
/*  74 */     return safelist;
/*     */   }
/*     */   
/*     */   public static boolean shouldHighlight(ItemStack stack) {
/*  78 */     if (stack == null) return false; 
/*  79 */     String id = ((ResourceLocation)Item.field_150901_e.func_177774_c(stack.func_77973_b())).toString();
/*     */     
/*  81 */     if (blacklist.contains(id)) return false;
/*     */     
/*  83 */     if (!safelist.contains(id)) return false;
/*     */     
/*  85 */     if (!cfg.v.itemHighlightBestOnly) return true;
/*     */     
/*  87 */     Item item = stack.func_77973_b();
/*     */     
/*  89 */     if (item instanceof net.minecraft.item.ItemSword || item instanceof net.minecraft.item.ItemAxe)
/*  90 */       return isBestMelee(stack); 
/*  91 */     if (item instanceof ItemArmor)
/*  92 */       return isBestArmor((ItemArmor)item, stack); 
/*  93 */     if (item instanceof net.minecraft.item.ItemBow) {
/*  94 */       return isBestBow(stack);
/*     */     }
/*     */     
/*  97 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isBestMelee(ItemStack candidate) {
/* 102 */     double EPS = 1.0E-6D;
/* 103 */     boolean isSword = candidate.func_77973_b() instanceof net.minecraft.item.ItemSword;
/* 104 */     boolean isAxe = candidate.func_77973_b() instanceof net.minecraft.item.ItemAxe;
/* 105 */     if (!isSword && !isAxe) return false;
/*     */     
/* 107 */     Minecraft mc = Minecraft.func_71410_x();
/* 108 */     ItemStack equipped = mc.field_71439_g.func_70694_bm();
/*     */     
/* 110 */     double bestSword = 0.0D;
/* 111 */     double bestAxe = 0.0D;
/* 112 */     ItemStack bestSwordStack = null;
/* 113 */     ItemStack bestAxeStack = null;
/*     */     
/* 115 */     for (ItemStack stack : getStacks()) {
/* 116 */       if (stack == null)
/* 117 */         continue;  Item item = stack.func_77973_b();
/* 118 */       double score = getMeleeDamage(stack);
/*     */       
/* 120 */       if (item instanceof net.minecraft.item.ItemSword) {
/* 121 */         if (score > bestSword + 1.0E-6D) {
/* 122 */           bestSword = score;
/* 123 */           bestSwordStack = stack;
/*     */         }  continue;
/* 125 */       }  if (item instanceof net.minecraft.item.ItemAxe && 
/* 126 */         score > bestAxe + 1.0E-6D) {
/* 127 */         bestAxe = score;
/* 128 */         bestAxeStack = stack;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 133 */     boolean swordWins = (bestSword >= bestAxe - 1.0E-6D);
/*     */     
/* 135 */     if (equipped != null) {
/* 136 */       Item eqItem = equipped.func_77973_b();
/* 137 */       if ((isSword && eqItem instanceof net.minecraft.item.ItemSword && Math.abs(getMeleeDamage(equipped) - bestSword) < 1.0E-6D) || (isAxe && eqItem instanceof net.minecraft.item.ItemAxe && 
/* 138 */         Math.abs(getMeleeDamage(equipped) - bestAxe) < 1.0E-6D)) {
/* 139 */         return (candidate == equipped);
/*     */       }
/*     */     } 
/*     */     
/* 143 */     if (isSword) return (candidate == bestSwordStack && swordWins); 
/* 144 */     return (candidate == bestAxeStack && !swordWins);
/*     */   }
/*     */   
/*     */   private static boolean isBestBow(ItemStack candidate) {
/* 148 */     Minecraft mc = Minecraft.func_71410_x();
/* 149 */     ItemStack equipped = mc.field_71439_g.func_70694_bm();
/*     */     
/* 151 */     double bestScore = 0.0D;
/* 152 */     ItemStack bestStack = null;
/*     */     
/* 154 */     for (ItemStack stack : getStacks()) {
/* 155 */       if (stack == null || !(stack.func_77973_b() instanceof net.minecraft.item.ItemBow))
/* 156 */         continue;  double score = getBowDamage(stack);
/* 157 */       if (score > bestScore) {
/* 158 */         bestScore = score;
/* 159 */         bestStack = stack;
/*     */       } 
/*     */     } 
/*     */     
/* 163 */     if (equipped != null && equipped.func_77973_b() instanceof net.minecraft.item.ItemBow && 
/* 164 */       Math.abs(getBowDamage(equipped) - bestScore) < 1.0E-6D) {
/* 165 */       return (candidate == equipped);
/*     */     }
/*     */     
/* 168 */     return (candidate == bestStack);
/*     */   }
/*     */   
/*     */   private static boolean isBestArmor(ItemArmor candidateArmor, ItemStack candidate) {
/* 172 */     int armorType = candidateArmor.field_77881_a;
/* 173 */     Minecraft mc = Minecraft.func_71410_x();
/* 174 */     ItemStack equipped = mc.field_71439_g.func_82169_q(3 - armorType);
/*     */     
/* 176 */     double bestScore = 0.0D;
/* 177 */     ItemStack bestStack = null;
/*     */     
/* 179 */     for (ItemStack stack : getStacks()) {
/* 180 */       if (stack == null || !(stack.func_77973_b() instanceof ItemArmor))
/* 181 */         continue;  ItemArmor armor = (ItemArmor)stack.func_77973_b();
/* 182 */       if (armor.field_77881_a != armorType)
/*     */         continue; 
/* 184 */       double score = getArmorValue(stack);
/* 185 */       if (score > bestScore) {
/* 186 */         bestScore = score;
/* 187 */         bestStack = stack;
/*     */       } 
/*     */     } 
/*     */     
/* 191 */     if (equipped != null && equipped.func_77973_b() instanceof ItemArmor) {
/* 192 */       double eqScore = getArmorValue(equipped);
/* 193 */       if (eqScore >= bestScore - 1.0E-6D) {
/* 194 */         return (candidate == equipped);
/*     */       }
/*     */     } 
/*     */     
/* 198 */     return (candidate == bestStack);
/*     */   }
/*     */   
/*     */   private static double getMeleeDamage(ItemStack stack) {
/* 202 */     Item item = stack.func_77973_b();
/* 203 */     double base = 0.0D;
/*     */     
/* 205 */     if (item == Items.field_151041_m || item == Items.field_151010_B) { base = 4.0D; }
/* 206 */     else if (item == Items.field_151052_q) { base = 5.0D; }
/* 207 */     else if (item == Items.field_151040_l) { base = 6.0D; }
/* 208 */     else if (item == Items.field_151048_u) { base = 7.0D; }
/* 209 */     else if (item == Items.field_151053_p || item == Items.field_151006_E) { base = 3.0D; }
/* 210 */     else if (item == Items.field_151049_t) { base = 4.0D; }
/* 211 */     else if (item == Items.field_151036_c) { base = 5.0D; }
/* 212 */     else if (item == Items.field_151056_x) { base = 6.0D; }
/* 213 */     else if (item instanceof net.minecraft.item.ItemSword)
/* 214 */     { base = 4.0D; }
/* 215 */     else if (item instanceof net.minecraft.item.ItemAxe)
/* 216 */     { base = 3.0D; }
/*     */ 
/*     */     
/* 219 */     int sharp = EnchantmentHelper.func_77506_a(Enchantment.field_180314_l.field_77352_x, stack);
/* 220 */     return base + sharp * 1.25D;
/*     */   }
/*     */   
/*     */   private static double getBowDamage(ItemStack stack) {
/* 224 */     double base = 5.0D;
/* 225 */     int power = EnchantmentHelper.func_77506_a(Enchantment.field_77345_t.field_77352_x, stack);
/* 226 */     return base + power * 0.5D;
/*     */   }
/*     */ 
/*     */   
/*     */   private static double getArmorValue(ItemStack stack) {
/* 231 */     if (!(stack.func_77973_b() instanceof ItemArmor)) return 0.0D; 
/* 232 */     ItemArmor armor = (ItemArmor)stack.func_77973_b();
/* 233 */     double base = armor.field_77879_b;
/* 234 */     int prot = EnchantmentHelper.func_77506_a(Enchantment.field_180310_c.field_77352_x, stack);
/* 235 */     return base + prot * 0.75D;
/*     */   }
/*     */   
/*     */   public static boolean isBestItem(ItemStack stack) {
/* 239 */     if (stack == null) return false; 
/* 240 */     Item item = stack.func_77973_b();
/*     */     
/* 242 */     if (item instanceof net.minecraft.item.ItemSword || item instanceof net.minecraft.item.ItemAxe)
/* 243 */       return isBestMelee(stack); 
/* 244 */     if (item instanceof ItemArmor)
/* 245 */       return isBestArmor((ItemArmor)item, stack); 
/* 246 */     if (item instanceof net.minecraft.item.ItemBow) {
/* 247 */       return isBestBow(stack);
/*     */     }
/*     */     
/* 250 */     return false;
/*     */   }
/*     */   
/*     */   public static List<ItemStack> getStacks() {
/* 254 */     List<ItemStack> stacks = new ArrayList<>();
/* 255 */     Minecraft mc = Minecraft.func_71410_x();
/*     */     
/* 257 */     Collections.addAll(stacks, mc.field_71439_g.field_71071_by.field_70462_a);
/*     */     
/* 259 */     Collections.addAll(stacks, mc.field_71439_g.field_71071_by.field_70460_b);
/*     */     
/* 261 */     if (mc.field_71439_g.field_71070_bA != null && mc.field_71439_g.field_71070_bA != mc.field_71439_g.field_71069_bz) {
/* 262 */       for (Object obj : mc.field_71439_g.field_71070_bA.field_75151_b) {
/* 263 */         if (obj instanceof Slot) {
/* 264 */           ItemStack stack = ((Slot)obj).func_75211_c();
/* 265 */           if (stack != null) stacks.add(stack);
/*     */         
/*     */         } 
/*     */       } 
/*     */     }
/* 270 */     ItemStack cursor = mc.field_71439_g.field_71071_by.func_70445_o();
/* 271 */     if (cursor != null) stacks.add(cursor);
/*     */     
/* 273 */     return stacks;
/*     */   }
/*     */ 
/*     */   
/* 277 */   public static final List<String> DEFAULT_BLACKLIST = Arrays.asList(new String[] { "minecraft:chest", "minecraft:double_plant", "minecraft:feather", "minecraft:fireworks", "minecraft:glass_bottle", "minecraft:gunpowder", "minecraft:jukebox", "minecraft:leather", "minecraft:lever", "minecraft:magma_cream", "minecraft:noteblock", "minecraft:prismarine_crystals", "minecraft:prismarine_shard", "minecraft:rabbit_foot", "minecraft:rabbit_hide", "minecraft:record_11", "minecraft:record_13", "minecraft:record_blocks", "minecraft:record_cat", "minecraft:record_chirp", "minecraft:record_far", "minecraft:record_mall", "minecraft:record_mellohi", "minecraft:record_stal", "minecraft:record_strad", "minecraft:record_wait", "minecraft:record_ward", "minecraft:red_flower", "minecraft:redstone", "minecraft:redstone_torch", "minecraft:repeater", "minecraft:rotten_flesh", "minecraft:saddle", "minecraft:sand", "minecraft:sapling", "minecraft:spider_eye", "minecraft:stone_button", "minecraft:stone_pressure_plate", "minecraft:string", "minecraft:torch", "minecraft:tripwire_hook", "minecraft:waterlily", "minecraft:wheat", "minecraft:wooden_button", "minecraft:wooden_pressure_plate", "minecraft:yellow_flower", "minecraft:carrot_on_a_stick", "minecraft:cactus" });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 329 */   public static final List<String> DEFAULT_SAFELIST = Arrays.asList(new String[] { "minecraft:arrow", "minecraft:bow", "minecraft:chainmail_boots", "minecraft:chainmail_chestplate", "minecraft:chainmail_helmet", "minecraft:chainmail_leggings", "minecraft:clock", "minecraft:diamond", "minecraft:diamond_axe", "minecraft:diamond_block", "minecraft:diamond_boots", "minecraft:diamond_chestplate", "minecraft:diamond_helmet", "minecraft:diamond_leggings", "minecraft:diamond_sword", "minecraft:ender_pearl", "minecraft:fishing_rod", "minecraft:golden_apple", "minecraft:golden_axe", "minecraft:golden_boots", "minecraft:golden_chestplate", "minecraft:golden_helmet", "minecraft:golden_leggings", "minecraft:golden_sword", "minecraft:iron_axe", "minecraft:iron_boots", "minecraft:iron_chestplate", "minecraft:iron_helmet", "minecraft:iron_leggings", "minecraft:iron_sword", "minecraft:leather_boots", "minecraft:leather_chestplate", "minecraft:leather_helmet", "minecraft:leather_leggings", "minecraft:snowball", "minecraft:stone_axe", "minecraft:stone_sword", "minecraft:wooden_axe", "minecraft:wooden_sword", "minecraft:egg" });
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/skywars/ItemHighlight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */