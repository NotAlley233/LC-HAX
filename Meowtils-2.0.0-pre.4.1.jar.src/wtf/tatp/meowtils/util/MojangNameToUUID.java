/*    */ package wtf.tatp.meowtils.util;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParser;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URL;
/*    */ 
/*    */ public class MojangNameToUUID
/*    */ {
/*    */   private final String name;
/*    */   private String uuid;
/*    */   
/*    */   public MojangNameToUUID(String name) throws IOException {
/* 16 */     this.name = name;
/* 17 */     lookupUUID();
/*    */   }
/*    */   
/*    */   private void lookupUUID() throws IOException {
/* 21 */     URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + this.name);
/* 22 */     HttpURLConnection connection = (HttpURLConnection)url.openConnection();
/* 23 */     connection.setReadTimeout(5000);
/* 24 */     connection.connect();
/*    */     
/* 26 */     if (connection.getResponseCode() == 200) {
/* 27 */       JsonObject response = (new JsonParser()).parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
/* 28 */       String rawId = response.get("id").getAsString();
/* 29 */       this.uuid = rawId.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
/*    */     
/*    */     }
/*    */     else {
/*    */       
/* 34 */       this.uuid = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getUUID() {
/* 39 */     return this.uuid;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 43 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/MojangNameToUUID.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */