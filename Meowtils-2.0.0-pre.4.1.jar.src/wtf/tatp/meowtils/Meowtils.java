/*     */ package wtf.tatp.meowtils;
/*     */ import java.awt.Font;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.fml.common.Mod.EventHandler;
/*     */ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*     */ import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.font.FontManager;
/*     */ import wtf.tatp.meowtils.font.FontRenderer;
/*     */ import wtf.tatp.meowtils.gui.ClickGUI;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.handlers.KeybindHandler;
/*     */ import wtf.tatp.meowtils.handlers.MapClearer;
/*     */ import wtf.tatp.meowtils.handlers.PartyHandler;
/*     */ import wtf.tatp.meowtils.handlers.QueueHandler;
/*     */ import wtf.tatp.meowtils.util.DeathTracker;
/*     */ import wtf.tatp.meowtils.util.NotifyUtil;
/*     */ import wtf.tatp.meowtils.util.Prefix;
/*     */ import wtf.tatp.meowtils.util.RegisterCommand;
/*     */ import wtf.tatp.meowtils.util.Wrapper;
/*     */ import wtf.tatp.meowtils.util.anticheat.AntiCheatData;
/*     */ 
/*     */ @Mod(modid = "meowtils", name = "Meowtils", version = "2.0.0-pre.4.1")
/*     */ public class Meowtils {
/*  36 */   public static final Logger LOGGER = LogManager.getLogger("Meowtils"); public static final String MODID = "meowtils"; public static final String MODNAME = "Meowtils";
/*     */   public static final String VERSION = "2.0.0-pre.4.1";
/*     */   public static FontRenderer fontRenderer;
/*  39 */   public static final File MEOWTILS_DIR = new File((Minecraft.func_71410_x()).field_71412_D, "meowtils");
/*  40 */   public static final File CUSTOM_CAPE_DIR = new File((Minecraft.func_71410_x()).field_71412_D, "meowtils/custom_cape");
/*  41 */   public static final File ITEM_DIR = new File((Minecraft.func_71410_x()).field_71412_D, "meowtils/items");
/*  42 */   public static final File ITEMBLACKLIST = new File((Minecraft.func_71410_x()).field_71412_D, "meowtils/items/itemblacklist.json");
/*  43 */   public static final File ITEMSAFELIST = new File((Minecraft.func_71410_x()).field_71412_D, "meowtils/items/itemsafelist.json");
/*  44 */   public static final File AUTO_UPDATE_DIR = new File((Minecraft.func_71410_x()).field_71412_D, "meowtils/auto_update");
/*     */   
/*     */   private static ClickGUI clickGUI;
/*     */   public static Minecraft mc;
/*     */   public static Meowtils INSTANCE;
/*  49 */   public static final String onMessage = EnumChatFormatting.WHITE + ":" + EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + " ON";
/*  50 */   public static final String offMessage = EnumChatFormatting.WHITE + ":" + EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + " OFF";
/*     */   
/*     */   public static boolean nullCheck() {
/*  53 */     Minecraft mc = Minecraft.func_71410_x();
/*  54 */     return (mc != null && mc.field_71441_e != null && mc.field_71439_g != null && mc
/*     */ 
/*     */       
/*  57 */       .func_147114_u() != null && mc.field_71474_y != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String addMessage(String message) {
/*  62 */     if (!nullCheck()) return message; 
/*  63 */     (Minecraft.func_71410_x()).field_71439_g.func_145747_a((IChatComponent)new ChatComponentText(Prefix.getPrefix() + EnumChatFormatting.RESET + message));
/*  64 */     return message;
/*     */   }
/*     */   public static String debugMessage(String message) {
/*  67 */     if (!cfg.v.debugMessages) return null; 
/*  68 */     if (!nullCheck()) return message; 
/*  69 */     (Minecraft.func_71410_x()).field_71439_g.func_145747_a((IChatComponent)new ChatComponentText(Prefix.getPrefix() + EnumChatFormatting.RESET + message));
/*  70 */     return message;
/*     */   }
/*     */   public static String addCleanMessage(String message) {
/*  73 */     if (!nullCheck()) return message; 
/*  74 */     (Minecraft.func_71410_x()).field_71439_g.func_145747_a((IChatComponent)new ChatComponentText(message));
/*  75 */     return message;
/*     */   }
/*     */   public static String sendMessage(String message) {
/*  78 */     if (!nullCheck()) return message; 
/*  79 */     (Minecraft.func_71410_x()).field_71439_g.func_71165_d("/pc " + ColorUtil.unformattedText(Prefix.getPrefix()) + "» " + message);
/*  80 */     return message;
/*     */   }
/*     */   public static String sendCleanMessage(String message) {
/*  83 */     if (!nullCheck()) return message; 
/*  84 */     (Minecraft.func_71410_x()).field_71439_g.func_71165_d(message);
/*  85 */     return message;
/*     */   }
/*     */   
/*     */   static {
/*  89 */     INSTANCE = new Meowtils();
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void preInit(FMLPreInitializationEvent event) {
/*  94 */     cfg.initFile(new File(event.getModConfigurationDirectory(), "meowtils.json"));
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void init(FMLInitializationEvent event) {
/*  99 */     cfg.load();
/*     */     
/* 101 */     Font font = FontManager.loadTTF("/assets/meowtils/fonts/font.ttf", 32.0F);
/* 102 */     fontRenderer = new FontRenderer(font);
/*     */     
/* 104 */     RegisterCommand.registerCommands();
/*     */ 
/*     */     
/* 107 */     KeybindHandler.init();
/* 108 */     MinecraftForge.EVENT_BUS.register(this);
/* 109 */     MinecraftForge.EVENT_BUS.register(new GamemodeUtil());
/* 110 */     MinecraftForge.EVENT_BUS.register(new KeybindHandler());
/* 111 */     MinecraftForge.EVENT_BUS.register(new QueueHandler());
/* 112 */     MinecraftForge.EVENT_BUS.register(new BlacklistHandler());
/* 113 */     MinecraftForge.EVENT_BUS.register(new PlayerRemovedHandler());
/* 114 */     MinecraftForge.EVENT_BUS.register(new AntiCheatData());
/* 115 */     MinecraftForge.EVENT_BUS.register(new DeathTracker());
/* 116 */     MinecraftForge.EVENT_BUS.register(new MapClearer());
/* 117 */     MinecraftForge.EVENT_BUS.register(new MeowtilsUpdater());
/* 118 */     MinecraftForge.EVENT_BUS.register(new TeamUtil());
/* 119 */     MinecraftForge.EVENT_BUS.register(new PartyHandler());
/* 120 */     MinecraftForge.EVENT_BUS.register(new MeowtilsAlert());
/* 121 */     MinecraftForge.EVENT_BUS.register(new NotifyUtil());
/*     */ 
/*     */     
/* 124 */     clickGUI = new ClickGUI();
/*     */     
/* 126 */     if (!MEOWTILS_DIR.exists()) {
/* 127 */       MEOWTILS_DIR.mkdirs();
/*     */     }
/* 129 */     if (!AUTO_UPDATE_DIR.exists()) {
/* 130 */       AUTO_UPDATE_DIR.mkdirs();
/*     */     }
/* 132 */     if (!CUSTOM_CAPE_DIR.exists()) {
/* 133 */       CUSTOM_CAPE_DIR.mkdirs();
/*     */     }
/* 135 */     if (!ITEM_DIR.exists()) {
/* 136 */       ITEM_DIR.mkdirs();
/*     */     }
/* 138 */     if (!ITEMBLACKLIST.exists()) {
/* 139 */       ITEMBLACKLIST.getParentFile().mkdirs();
/*     */       try {
/* 141 */         ITEMBLACKLIST.createNewFile();
/* 142 */       } catch (IOException e) {
/* 143 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */     
/* 147 */     if (!ITEMSAFELIST.exists()) {
/* 148 */       ITEMSAFELIST.getParentFile().mkdirs();
/*     */       try {
/* 150 */         ITEMSAFELIST.createNewFile();
/* 151 */       } catch (IOException e) {
/* 152 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 157 */     mc = Minecraft.func_71410_x();
/*     */   }
/*     */   
/*     */   public static ClickGUI getClickGUI() {
/* 161 */     return clickGUI;
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void keyInput(InputEvent.KeyInputEvent event) {
/* 167 */     if (Wrapper.getPlayer() != null) {
/* 168 */       if (!Keyboard.getEventKeyState()) {
/*     */         return;
/*     */       }
/*     */       
/* 172 */       int key = Keyboard.getEventKey();
/*     */       
/* 174 */       if (key == 0 || key < 0 || key > 256) {
/*     */         return;
/*     */       }
/*     */       
/* 178 */       for (Module mod : ModuleManager.getModules()) {
/* 179 */         if (mod.getKey() == key)
/* 180 */           mod.setState(!mod.getState()); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Meowtils getInstance() {
/* 186 */     return INSTANCE;
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/Meowtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */