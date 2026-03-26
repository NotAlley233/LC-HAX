/*     */ package wtf.tatp.meowtils.modules.hypixel;
/*     */ 
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ 
/*     */ public class ChatCleaner
/*     */   extends Module {
/*     */   private BooleanValue hideBedwarsExp;
/*     */   private BooleanValue hideSendTo;
/*     */   private BooleanValue hideWait;
/*     */   
/*     */   public ChatCleaner() {
/*  20 */     super("ChatCleaner", "chatCleanerKey", "chatCleaner", Module.Category.Hypixel);
/*  21 */     tooltip("Removes chat messages that aren't important on Hypixel.\nContains settings for hiding more optional messages");
/*  22 */     addBoolean(this.hideBedwarsExp = new BooleanValue("Hide §7'Bed Wars XP'", "hideBedwarsExpMessage"));
/*  23 */     addBoolean(this.hideSendTo = new BooleanValue("Hide §7'Sending you to..'", "hideSendToMessage"));
/*  24 */     addBoolean(this.hideWait = new BooleanValue("Hide §7'You must wait..'", "hideWaitMessage"));
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.LOWEST)
/*     */   public void onChatReceived(ClientChatReceivedEvent event) {
/*  29 */     String msg = event.message.func_150260_c();
/*     */     
/*  31 */     if (!msg.contains(":") && 
/*  32 */       !msg.contains("[Meow]") && 
/*  33 */       !msg.contains("[M]") && (msg
/*  34 */       .startsWith("You tipped") || msg
/*  35 */       .contains("You are still radiating with Generosity!") || msg
/*  36 */       .contains("Your game was boosted by") || msg
/*  37 */       .equals("Coins just earned DOUBLED as a Guild Level Reward!") || msg
/*  38 */       .endsWith("'s Network Booster)") || msg
/*  39 */       .equals("You are AFK. Move around to return from AFK.") || msg
/*  40 */       .contains("joined the lobby!") || msg
/*  41 */       .equals("Tokens just earned DOUBLED as a Guild Level Reward!") || msg
/*  42 */       .contains("tokens! (") || msg
/*  43 */       .contains("coins! (") || msg
/*  44 */       .equals("If you get disconnected use /rejoin to join back in the game.") || msg
/*  45 */       .startsWith("You purchased") || msg
/*  46 */       .startsWith("Contents of") || msg
/*  47 */       .contains("Bed Wars Experience (Group Bonus)") || msg
/*  48 */       .contains("Bed Wars Experience (Position Bonus)") || msg
/*  49 */       .contains("Bed Wars Experience (Time Played)") || msg
/*  50 */       .startsWith("You deposited") || msg
/*  51 */       .equals("You can't deposit this item!") || msg
/*  52 */       .startsWith("You don't have enough") || msg
/*  53 */       .equals("YOU GOT LUCKY!") || msg
/*  54 */       .equals("You will receive DOUBLE EXP this game!") || msg
/*  55 */       .endsWith("Karma!") || msg
/*  56 */       .contains("You've already purchased this item") || msg
/*  57 */       .contains("You are already connected to this server") || msg
/*  58 */       .contains("The game starts in 10 seconds!") || msg
/*  59 */       .contains("The game starts in 20 seconds!") || msg
/*  60 */       .endsWith("Mystery Dust!") || msg
/*  61 */       .endsWith("Pet Consumables items!") || msg
/*  62 */       .equals("Please don't spam the command!") || msg
/*  63 */       .equals("You are sending commands too fast! Please slow down.") || msg
/*  64 */       .contains("Click here to watch the Replay!") || msg
/*  65 */       .contains("tokens! Picked up gold") || msg
/*  66 */       .contains("tokens! Survived 30 seconds") || msg
/*  67 */       .equals("You can't use gadgets right now!") || msg
/*  68 */       .endsWith("Soul") || msg
/*  69 */       .endsWith("Souls (Xezbeth Luck Perk)") || msg
/*  70 */       .equals("You're already in this channel!") || msg
/*  71 */       .startsWith("Teaming is not allowed") || msg
/*  72 */       .equals("We don't have enough players! Start cancelled.") || msg
/*  73 */       .endsWith("unclaimed achievement reward!") || msg
/*  74 */       .contains("Click here to view it!") || msg
/*  75 */       .contains("is available to join! CLICK HERE to join!") || msg
/*  76 */       .equals("[WATCHDOG ANNOUNCEMENT]") || msg
/*  77 */       .contains("Watchdog has banned") || msg
/*  78 */       .equals("Blacklisted modifications are a bannable offense!") || msg
/*  79 */       .contains("You've already started the") || msg
/*  80 */       .startsWith("You displayed your") || msg
/*  81 */       .equals("You can't break that block!") || msg
/*  82 */       .startsWith("Click to view the stats of your") || msg
/*  83 */       .equals("You cannot ride another player's mobs!") || msg
/*  84 */       .equals("You will be teleported to an arena to fight to the death.") || msg
/*  85 */       .equals("Right-click the bow to select a kit!") || msg
/*  86 */       .equals("If you find yourself to be going too fast, consider a slowness potion for more control.") || msg
/*  87 */       .equals("Later on in the game, some jumps may only be possible with the use of a speed potion.") || msg
/*  88 */       .equals("Try to save your double jumps for situations where you really need them.") || msg
/*  89 */       .endsWith("more players are required for the game to start.") || msg
/*  90 */       .equals("You died! You can now spectate the game!") || msg
/*  91 */       .equals("Right click with the compass in a lobby to select a game!") || msg
/*  92 */       .equals("Rate this map by clicking: [5] [4] [3] [2] [1]") || msg
/*  93 */       .equals("Prepare your defenses!") || msg
/*  94 */       .equals("Your spawn protection was removed!") || msg
/*  95 */       .equals("Click with any sword or bow to activate your skill!") || msg
/*  96 */       .equals("The next game will begin in 10s. Prepare yourselves!") || msg
/*  97 */       .equals("You've already claimed that reward!") || msg
/*  98 */       .equals("You are not allowed to use that command as a spectator!") || msg
/*  99 */       .equals("Resource Pack Declined! Make sure you have resource packs enabled in your multiplayer server settings!") || msg
/* 100 */       .equals("Click here or do /resource to open the resource pack book.") || msg
/* 101 */       .equals("Resource pack not working? Type /resource to fix it!") || msg
/* 102 */       .contains("Welcome to the Prototype Lobby") || msg
/* 103 */       .equals("All games in this lobby are currently in development.") || msg
/* 104 */       .startsWith("Click here to leave feedback!") || msg
/* 105 */       .startsWith("PIT! Latest update:") || msg
/* 106 */       .startsWith("STREAK! of") || msg
/* 107 */       .startsWith("GOLD PICKUP! from the ground") || msg
/* 108 */       .startsWith("DEATH! by") || msg
/* 109 */       .startsWith("BOUNTY! bump") || (msg
/* 110 */       .startsWith("You died while carrying") && msg.contains("Comfy Pillows!")) || msg
/* 111 */       .endsWith("unclaimed leveling rewards!") || msg
/* 112 */       .contains("Click here to view them!") || msg
/* 113 */       .endsWith("items of this type to deposit!") || (msg
/* 114 */       .startsWith(">>> [MVP++]") && msg.contains("joined the lobby!")) || (msg
/* 115 */       .startsWith("Staff have banned an additional") && msg.endsWith("in the last 7 days.")) || msg
/* 116 */       .endsWith("unclaimed achievement rewards!") || msg
/* 117 */       .equals("Cross-teaming is not allowed! Report cross-teamers using /report.") || msg
/* 118 */       .contains("Slumber Tickets! (") || msg
/* 119 */       .equals("You can only break blocks placed by a player!") || msg
/* 120 */       .equals("Woah there, slow down!") || msg
/* 121 */       .equals("Please wait...") || msg
/* 122 */       .equals("Opening settings...") || msg
/* 123 */       .equals("Ticket Machine now rolling...") || msg
/* 124 */       .contains("has found a ") || msg
/* 125 */       .equals("The game starts in 15 seconds!") || msg
/* 126 */       .startsWith("You are already in ") || msg
/* 127 */       .endsWith("(Lucky! Harvesting Season Perk)") || msg
/* 128 */       .endsWith("(w/ Avarice Perk)") || msg
/* 129 */       .equals("Teaming with the Murderer is not allowed!") || msg
/* 130 */       .startsWith("Survived ") || msg
/* 131 */       .equals("YOU GOT LUCKY! You will receive DOUBLE EXP this game!") || (msg
/* 132 */       .contains("Bed Wars XP") && cfg.v.hideBedwarsExpMessage) || msg
/* 133 */       .startsWith("MIDAS GIFT") || msg
/* 134 */       .contains("Gain XP and coins by » CLICKING HERE! «") || msg
/* 135 */       .contains("You cannot return items to another team's Shopkeeper!") || msg
/* 136 */       .contains("You cannot carry any more Comfy Pillows!") || (msg
/* 137 */       .contains("[NPC]") && GamemodeUtil.bedwarsLobby) || (msg
/* 138 */       .contains("Sending you to") && cfg.v.hideSendToMessage) || (msg
/* 139 */       .contains("You must wait") && msg.contains("!") && cfg.v.hideWaitMessage) || msg
/* 140 */       .contains("You are now carrying x1 Comfy Pillows, bring it back to your base!") || (msg
/* 141 */       .startsWith("Deposited") && msg.contains("into Ender Chest!")) || msg
/* 142 */       .equals("Command Failed: This command is on cooldown! Try again in about a second!"))) {
/*     */ 
/*     */       
/* 145 */       event.setCanceled(true);
/* 146 */       if (cfg.v.debugMessages) {
/* 147 */         Meowtils.addMessage(EnumChatFormatting.YELLOW + "[ChatCleaner]: " + EnumChatFormatting.GRAY + "Cancelled message: " + EnumChatFormatting.WHITE + msg);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 152 */     if (cfg.v.failedQueueMessage && !msg.contains(":"))
/* 153 */       if (msg.startsWith("Something went wrong trying to")) {
/* 154 */         Meowtils.addMessage(EnumChatFormatting.RED + "Failed to queue: " + EnumChatFormatting.GRAY + "Hypixel Error");
/* 155 */       } else if (msg.equals("Please don't spam the command!") || msg.equals("Command Failed: This command is on cooldown! Try again in about a second!")) {
/* 156 */         Meowtils.addMessage(EnumChatFormatting.RED + "Failed to queue: " + EnumChatFormatting.GRAY + "Cooldown");
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/hypixel/ChatCleaner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */