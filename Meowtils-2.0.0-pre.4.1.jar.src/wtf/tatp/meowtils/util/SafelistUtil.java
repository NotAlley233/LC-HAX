/*    */ package wtf.tatp.meowtils.util;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.reflect.TypeToken;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.io.Writer;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class SafelistUtil {
/* 13 */   private static final File FILE = new File(new File((Minecraft.func_71410_x()).field_71412_D, "meowtils"), "meowtilssafelist.json");
/* 14 */   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
/* 15 */   private static final Type TYPE = (new TypeToken<Set<String>>() {  }).getType();
/* 16 */   private static Set<String> safelist = new HashSet<>();
/*    */   
/*    */   static {
/* 19 */     loadSafelist();
/*    */   }
/*    */   
/*    */   public static void addToSafelist(String uuidOrName) {
/* 23 */     safelist.add(uuidOrName);
/* 24 */     saveSafelist();
/*    */   }
/*    */   
/*    */   public static boolean isSafelisted(String uuidOrName) {
/* 28 */     return safelist.contains(uuidOrName);
/*    */   }
/*    */   
/*    */   public static Set<String> getSafelist() {
/* 32 */     return safelist;
/*    */   }
/*    */   
/*    */   private static void loadSafelist() {
/* 36 */     if (!FILE.exists())
/*    */       return; 
/* 38 */     try (Reader reader = new FileReader(FILE)) {
/* 39 */       safelist = (Set<String>)GSON.fromJson(reader, TYPE);
/* 40 */       if (safelist == null) {
/* 41 */         safelist = new HashSet<>();
/*    */       }
/* 43 */     } catch (Exception e) {
/* 44 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private static void saveSafelist() {
/* 50 */     try (Writer writer = new FileWriter(FILE)) {
/* 51 */       GSON.toJson(safelist, writer);
/* 52 */     } catch (IOException e) {
/* 53 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */   public static void saveSL() {
/* 57 */     saveSafelist();
/*    */   }
/*    */   public static void removeFromSafelist(String uuidOrName) {
/* 60 */     safelist.remove(uuidOrName);
/* 61 */     saveSafelist();
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/SafelistUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */