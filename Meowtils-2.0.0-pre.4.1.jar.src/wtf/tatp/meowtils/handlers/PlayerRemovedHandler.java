/*    */ package wtf.tatp.meowtils.handlers;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ public class PlayerRemovedHandler
/*    */ {
/*    */   private static final int MAX_TICKS = 10;
/* 18 */   private static int ticksRemaining = 0;
/* 19 */   private static final Set<String> previousPlayerNames = new HashSet<>();
/*    */   private static boolean registerOnTick = false;
/*    */   private static PlayerRemovedHandler instance;
/* 22 */   private int tickCounter = 0;
/*    */   
/*    */   public static void startTracking() {
/* 25 */     if ((Minecraft.func_71410_x()).field_71441_e == null)
/* 26 */       return;  if (registerOnTick)
/*    */       return; 
/* 28 */     ticksRemaining = 10;
/* 29 */     previousPlayerNames.clear();
/*    */     
/* 31 */     for (EntityPlayer player : (Minecraft.func_71410_x()).field_71441_e.field_73010_i) {
/* 32 */       previousPlayerNames.add(player.func_70005_c_());
/*    */     }
/*    */     
/* 35 */     instance = new PlayerRemovedHandler();
/* 36 */     MinecraftForge.EVENT_BUS.register(instance);
/* 37 */     registerOnTick = true;
/*    */   }
/*    */   
/*    */   public static void stopTracking() {
/* 41 */     if (!registerOnTick || instance == null)
/*    */       return; 
/* 43 */     MinecraftForge.EVENT_BUS.unregister(instance);
/* 44 */     instance = null;
/* 45 */     registerOnTick = false;
/* 46 */     previousPlayerNames.clear();
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onChatReceived(ClientChatReceivedEvent event) {
/* 51 */     String message = event.message.func_150260_c();
/*    */     
/* 53 */     if (message.equals("A player has been removed from your game.") && cfg.v.playerRemovedMessage) {
/* 54 */       startTracking();
/*    */     }
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTick(TickEvent.ClientTickEvent event) {
/* 60 */     if (event.phase != TickEvent.Phase.END)
/* 61 */       return;  if ((Minecraft.func_71410_x()).field_71441_e == null)
/* 62 */       return;  this.tickCounter++;
/* 63 */     if (this.tickCounter < 20)
/* 64 */       return;  this.tickCounter = 0;
/*    */     
/* 66 */     ticksRemaining--;
/*    */     
/* 68 */     Set<String> currentPlayerNames = new HashSet<>();
/* 69 */     for (EntityPlayer player : (Minecraft.func_71410_x()).field_71441_e.field_73010_i) {
/* 70 */       currentPlayerNames.add(player.func_70005_c_());
/*    */     }
/*    */     
/* 73 */     for (String name : previousPlayerNames) {
/* 74 */       if (!currentPlayerNames.contains(name)) {
/* 75 */         Meowtils.addMessage(EnumChatFormatting.RED + "Player removed: " + EnumChatFormatting.GRAY + name);
/* 76 */         stopTracking();
/*    */         
/*    */         return;
/*    */       } 
/*    */     } 
/* 81 */     if (ticksRemaining <= 0)
/* 82 */       stopTracking(); 
/*    */   }
/*    */   
/*    */   public static void clear() {
/* 86 */     previousPlayerNames.clear();
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/handlers/PlayerRemovedHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */