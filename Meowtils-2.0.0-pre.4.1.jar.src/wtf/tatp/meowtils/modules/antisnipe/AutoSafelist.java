/*    */ package wtf.tatp.meowtils.modules.antisnipe;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.network.NetworkPlayerInfo;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*    */ import wtf.tatp.meowtils.util.MojangNameToUUID;
/*    */ import wtf.tatp.meowtils.util.NameUtil;
/*    */ import wtf.tatp.meowtils.util.SafelistUtil;
/*    */ import wtf.tatp.meowtils.util.TeamUtil;
/*    */ 
/*    */ public class AutoSafelist
/*    */   extends Module {
/*    */   public AutoSafelist() {
/* 24 */     super("AutoSafelist", "autoSafelistKey", "autoSafelist", Module.Category.Antisnipe);
/* 25 */     tooltip("Automatically safelists players that take a final death.");
/* 26 */     addBoolean(this.autoSafelistMessage = new BooleanValue("Safelist feedback", "autoSafelistMessage"));
/*    */   }
/*    */   private BooleanValue autoSafelistMessage;
/*    */   @SubscribeEvent
/*    */   public void onChatMessageReceived(ClientChatReceivedEvent event) {
/* 31 */     if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/* 32 */       return;  if (event == null || event.message == null || event.message.func_150260_c() == null)
/*    */       return; 
/* 34 */     String message = event.message.func_150260_c();
/*    */     
/* 36 */     if (message.contains("FINAL KILL!")) {
/* 37 */       Matcher matcher = Pattern.compile("^([A-Za-z0-9_]+)(?=[\\s'])").matcher(message);
/*    */       
/* 39 */       if (matcher.find()) {
/* 40 */         String playerName = matcher.group(1);
/* 41 */         if (TeamUtil.ignoreSelf(playerName))
/* 42 */           return;  if (GamemodeUtil.replay) {
/*    */           return;
/*    */         }
/* 45 */         for (NetworkPlayerInfo info : Minecraft.func_71410_x().func_147114_u().func_175106_d()) {
/* 46 */           if (info.func_178845_a().getName().equalsIgnoreCase(playerName)) {
/* 47 */             String uuid = info.func_178845_a().getId().toString();
/* 48 */             if (!SafelistUtil.isSafelisted(uuid)) {
/* 49 */               SafelistUtil.addToSafelist(uuid);
/* 50 */               if (cfg.v.autoSafelistMessage) {
/* 51 */                 Meowtils.addMessage(EnumChatFormatting.GREEN + "Auto-safelisted " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playerName) + EnumChatFormatting.GREEN + ".");
/*    */               }
/*    */             } 
/*    */             
/*    */             return;
/*    */           } 
/*    */         } 
/* 58 */         (new Thread(() -> {
/*    */               try {
/*    */                 MojangNameToUUID lookup = new MojangNameToUUID(playerName);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */                 
/*    */                 String uuid = lookup.getUUID();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */                 
/*    */                 Minecraft.func_71410_x().func_152344_a(());
/* 81 */               } catch (IOException e) {
/*    */                 
/*    */                 Minecraft.func_71410_x().func_152343_a(());
/*    */               }
/*    */             
/* 86 */             })).start();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/antisnipe/AutoSafelist.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */