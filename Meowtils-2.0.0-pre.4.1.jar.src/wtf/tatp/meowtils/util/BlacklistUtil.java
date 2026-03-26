/*     */ package wtf.tatp.meowtils.util;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ 
/*     */ public class BlacklistUtil {
/*  15 */   private static final File FILE = new File(new File((Minecraft.func_71410_x()).field_71412_D, "meowtils"), "meowtilsblacklist.json");
/*  16 */   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
/*  17 */   private static final Type TYPE = (new TypeToken<Map<String, String>>() {  }).getType();
/*  18 */   private static Map<String, String> blacklist = new HashMap<>();
/*  19 */   public static final String RED_ICON = EnumChatFormatting.DARK_RED.toString() + EnumChatFormatting.BOLD + "⚠ " + EnumChatFormatting.RESET;
/*  20 */   public static final String GOLD_ICON = EnumChatFormatting.GOLD.toString() + EnumChatFormatting.BOLD + "⚠ " + EnumChatFormatting.RESET;
/*  21 */   public static final String GREEN_ICON = EnumChatFormatting.DARK_GREEN.toString() + EnumChatFormatting.BOLD + "⚠ " + EnumChatFormatting.RESET;
/*  22 */   public static final String LIGHT_PURPLE_ICON = EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD + "⚠ " + EnumChatFormatting.RESET;
/*  23 */   public static final String CHECK_ICON = EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "✓ " + EnumChatFormatting.RESET;
/*  24 */   public static final Set<String> NON_BLATANT = new HashSet<>(Arrays.asList(new String[] { "cheating", "aimassist", "autoclicker", "reach", "velocity", "esp", "fakelag", "legit scaffold", "fastplace", "closet" }));
/*  25 */   public static final Set<String> BLATANT = new HashSet<>(Arrays.asList(new String[] { "antivoid", "bednuker", "fly", "keepsprint", "killaura", "nofall", "noslow", "scaffold", "bhop", "antifireball", "safewalk", "blink", "autoblock", "blatant", "strafe" }));
/*     */   
/*     */   static {
/*  28 */     loadBlacklist();
/*     */   }
/*     */   
/*     */   public static void addToBlacklist(String uuidOrName, String reason) {
/*  32 */     blacklist.put(uuidOrName, System.currentTimeMillis() + " " + reason);
/*  33 */     saveBlacklist();
/*     */   }
/*     */   
/*     */   public static boolean isBlacklisted(String uuidOrName) {
/*  37 */     return blacklist.containsKey(uuidOrName);
/*     */   }
/*     */   
/*     */   public static Map<String, String> getBlacklist() {
/*  41 */     return blacklist;
/*     */   }
/*     */   
/*     */   private static void loadBlacklist() {
/*  45 */     if (!FILE.exists())
/*     */       return; 
/*  47 */     boolean modified = false;
/*     */     
/*  49 */     try (Reader reader = new FileReader(FILE)) {
/*  50 */       JsonElement jsonElement = (new JsonParser()).parse(reader);
/*     */       
/*  52 */       if (jsonElement.isJsonObject()) {
/*     */         
/*  54 */         blacklist = (Map<String, String>)GSON.fromJson(jsonElement, TYPE);
/*  55 */       } else if (jsonElement.isJsonArray()) {
/*     */         
/*  57 */         blacklist.clear();
/*  58 */         for (JsonElement element : jsonElement.getAsJsonArray()) {
/*  59 */           if (!element.isJsonPrimitive())
/*     */             continue; 
/*  61 */           String line = element.getAsString().trim();
/*  62 */           String[] parts = line.split(" ", 3);
/*  63 */           if (parts.length < 3)
/*     */             continue; 
/*  65 */           String uuid = parts[0];
/*  66 */           String timestamp = parts[1];
/*  67 */           String reasonsRaw = parts[2];
/*     */ 
/*     */           
/*  70 */           String formattedReasons = String.join(" | ", (CharSequence[])reasonsRaw.split("\\s+")).replaceAll("\\[.*?\\]", "").trim();
/*     */           
/*  72 */           blacklist.put(uuid, timestamp + " " + formattedReasons);
/*  73 */           modified = true;
/*     */         } 
/*     */         
/*  76 */         if (modified) {
/*  77 */           saveBlacklist();
/*     */         }
/*     */       } else {
/*  80 */         System.err.println("[Meowtils] Unknown JSON format.");
/*     */       }
/*     */     
/*  83 */     } catch (Exception e) {
/*  84 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void saveBlacklist() {
/*  89 */     try (Writer writer = new FileWriter(FILE)) {
/*  90 */       GSON.toJson(blacklist, writer);
/*  91 */     } catch (IOException e) {
/*  92 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   public static boolean isBlacklistedByUUID(String uuid) {
/*  96 */     return blacklist.containsKey(uuid);
/*     */   }
/*     */   public static String getEntry(String uuidOrName) {
/*  99 */     return blacklist.get(uuidOrName);
/*     */   }
/*     */   public static void saveBL() {
/* 102 */     saveBlacklist();
/*     */   }
/*     */   public static void removeFromBlacklist(String key) {
/* 105 */     getBlacklist().remove(key);
/* 106 */     saveBlacklist();
/*     */   }
/*     */   public static String getFormattedIcon(String uuid, String name) {
/* 109 */     boolean isBlacklisted = (isBlacklistedByUUID(uuid) || isBlacklisted(name));
/* 110 */     boolean isSafelisted = (SafelistUtil.isSafelisted(uuid) || SafelistUtil.isSafelisted(name));
/*     */     
/* 112 */     if (!isBlacklisted && !isSafelisted) {
/* 113 */       return null;
/*     */     }
/*     */     
/* 116 */     if (isBlacklisted && isSafelisted) {
/* 117 */       return LIGHT_PURPLE_ICON;
/*     */     }
/*     */     
/* 120 */     if (isSafelisted) {
/* 121 */       return CHECK_ICON;
/*     */     }
/*     */     
/* 124 */     String entry = getEntry(uuid);
/* 125 */     if (entry == null) entry = getEntry(name); 
/* 126 */     if (entry == null) return GREEN_ICON;
/*     */     
/* 128 */     String[] parts = entry.split(" ", 2);
/* 129 */     String reasons = (parts.length > 1) ? parts[1].toLowerCase() : "";
/*     */     
/* 131 */     for (String reason : reasons.split(" \\| ")) {
/* 132 */       if (BLATANT.contains(reason.trim())) return RED_ICON;
/*     */     
/*     */     } 
/* 135 */     for (String reason : reasons.split(" \\| ")) {
/* 136 */       if (NON_BLATANT.contains(reason.trim())) return GOLD_ICON;
/*     */     
/*     */     } 
/* 139 */     return GREEN_ICON;
/*     */   }
/*     */   public static String getFormattedReason(String reason) {
/* 142 */     StringBuilder formatted = new StringBuilder();
/* 143 */     String[] parts = reason.split(" \\| ");
/*     */     
/* 145 */     for (int i = 0; i < parts.length; i++) {
/* 146 */       String part = parts[i].trim().toLowerCase();
/*     */       
/* 148 */       if (NON_BLATANT.contains(part)) {
/* 149 */         formatted.append(EnumChatFormatting.GOLD).append(part);
/* 150 */       } else if (BLATANT.contains(part)) {
/* 151 */         formatted.append(EnumChatFormatting.DARK_RED).append(part);
/*     */       } else {
/* 153 */         formatted.append(EnumChatFormatting.DARK_GREEN).append(part);
/*     */       } 
/*     */       
/* 156 */       if (i < parts.length - 1) {
/* 157 */         formatted.append(EnumChatFormatting.DARK_GRAY).append(" | ");
/*     */       }
/*     */     } 
/*     */     
/* 161 */     return formatted.toString();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/BlacklistUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */