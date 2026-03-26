/*     */ package wtf.tatp.meowtils;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import java.io.File;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.event.ClickEvent;
/*     */ import net.minecraft.event.HoverEvent;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.fml.common.Loader;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import net.minecraftforge.fml.common.versioning.ComparableVersion;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.util.Prefix;
/*     */ 
/*     */ public class MeowtilsUpdater {
/*  29 */   private final Minecraft mc = Minecraft.func_71410_x(); private static final String API_LATEST = "https://api.github.com/repos/femboytatp/meowtils/releases/latest";
/*     */   private boolean verified = false;
/*     */   
/*     */   public MeowtilsUpdater() {
/*  33 */     MinecraftForge.EVENT_BUS.register(this);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onTick(TickEvent.ClientTickEvent event) {
/*  38 */     if (this.verified || this.mc.field_71439_g == null || this.mc.field_71441_e == null)
/*     */       return; 
/*  40 */     Meowtils.addMessage(EnumChatFormatting.GRAY + "Verifying " + EnumChatFormatting.DARK_PURPLE.toString() + EnumChatFormatting.BOLD + "Meowtils" + EnumChatFormatting.GRAY + " version...");
/*  41 */     this.verified = true;
/*     */     
/*  43 */     (new Thread(this::checkForUpdate, "Meowtils-Updater")).start();
/*     */     
/*  45 */     MinecraftForge.EVENT_BUS.unregister(this);
/*     */   }
/*     */   
/*     */   private void checkForUpdate() {
/*     */     try {
/*  50 */       JsonObject release = fetchLatest();
/*  51 */       if (release == null)
/*     */         return; 
/*  53 */       String latestTag = release.get("tag_name").getAsString();
/*  54 */       ComparableVersion current = new ComparableVersion("2.0.0-pre.4.1");
/*  55 */       ComparableVersion latest = new ComparableVersion(latestTag);
/*     */       
/*  57 */       if (current.compareTo(latest) >= 0) {
/*  58 */         Meowtils.addMessage(EnumChatFormatting.GREEN + "Already using latest " + EnumChatFormatting.DARK_PURPLE.toString() + EnumChatFormatting.BOLD + "Meowtils" + EnumChatFormatting.GREEN + " version: " + EnumChatFormatting.GRAY + "2.0.0-pre.4.1");
/*     */         
/*     */         return;
/*     */       } 
/*  62 */       JsonArray assets = release.getAsJsonArray("assets");
/*  63 */       if (assets.size() == 0) {
/*  64 */         Meowtils.addMessage(EnumChatFormatting.RED + "Failed to verify version.");
/*     */         
/*     */         return;
/*     */       } 
/*  68 */       String url = null, name = null;
/*  69 */       for (JsonElement e : assets) {
/*  70 */         JsonObject asset = e.getAsJsonObject();
/*  71 */         if (asset.get("name").getAsString().endsWith(".jar")) {
/*  72 */           url = asset.get("browser_download_url").getAsString();
/*  73 */           name = asset.get("name").getAsString();
/*     */           break;
/*     */         } 
/*     */       } 
/*  77 */       if (url == null)
/*     */         return; 
/*  79 */       Meowtils.addMessage(EnumChatFormatting.DARK_PURPLE.toString() + EnumChatFormatting.BOLD + "Meowtils" + EnumChatFormatting.GREEN + " update " + EnumChatFormatting.GRAY + latestTag + EnumChatFormatting.GREEN + " is available!");
/*     */       
/*  81 */       if (cfg.v.autoUpdate) {
/*  82 */         Meowtils.addMessage(EnumChatFormatting.GREEN + "Downloading update...");
/*  83 */         download(url, name);
/*     */       } else {
/*  85 */         ChatComponentText msg = new ChatComponentText(Prefix.getPrefix() + EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "Click to download update!");
/*  86 */         msg.func_150255_a((new ChatStyle()).func_150241_a(new ClickEvent(ClickEvent.Action.OPEN_URL, url)).func_150209_a(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (IChatComponent)new ChatComponentText(EnumChatFormatting.YELLOW + url))));
/*  87 */         this.mc.field_71439_g.func_145747_a((IChatComponent)msg);
/*     */       } 
/*  89 */     } catch (Exception e) {
/*  90 */       Meowtils.addMessage(EnumChatFormatting.RED + "Failed to verify update.");
/*  91 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void download(String url, String name) {
/*     */     try {
/*  97 */       File cacheDirectory = new File(this.mc.field_71412_D, "meowtils/auto_update");
/*  98 */       if (!cacheDirectory.exists()) cacheDirectory.mkdirs();
/*     */       
/* 100 */       File target = new File(cacheDirectory, name);
/* 101 */       FileUtils.copyURLToFile(new URL(url), target);
/*     */       
/* 103 */       meowtilsAutoUpdater(target);
/* 104 */       Meowtils.addMessage(EnumChatFormatting.GREEN + "Done!" + EnumChatFormatting.GRAY.toString() + EnumChatFormatting.ITALIC + " Update will be applied next launch.");
/* 105 */     } catch (Exception e) {
/* 106 */       Meowtils.addMessage(EnumChatFormatting.RED + "Failed to download update.");
/* 107 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void meowtilsAutoUpdater(File newJar) {
/* 112 */     File autoUpdateJar = new File(this.mc.field_71412_D, "meowtils/auto_update/MeowtilsAutoUpdate.jar");
/* 113 */     if (!autoUpdateJar.exists()) {
/*     */       try {
/* 115 */         FileUtils.copyURLToFile(new URL("https://github.com/femboytatp/MeowtilsAutoUpdate/releases/latest/download/MeowtilsAutoUpdate.jar"), autoUpdateJar);
/* 116 */       } catch (Exception exception) {}
/*     */     }
/*     */     
/* 119 */     File oldJar = Loader.instance().activeModContainer().getSource();
/* 120 */     Runtime.getRuntime().addShutdownHook(new Thread(() -> {
/*     */             try {
/*     */               String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
/*     */               (new ProcessBuilder(new String[] { javaBin, "-jar", autoUpdateJar.getAbsolutePath(), oldJar.getAbsolutePath(), newJar.getAbsolutePath() })).start();
/* 124 */             } catch (Exception exception) {}
/*     */           }));
/*     */   }
/*     */   
/*     */   private JsonObject fetchLatest() throws Exception {
/* 129 */     HttpURLConnection conn = (HttpURLConnection)(new URL("https://api.github.com/repos/femboytatp/meowtils/releases/latest")).openConnection();
/* 130 */     conn.setRequestProperty("User-Agent", "Meowtils-Updater");
/* 131 */     conn.setConnectTimeout(10000);
/* 132 */     conn.setReadTimeout(10000);
/*     */     
/* 134 */     try (InputStreamReader reader = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)) {
/* 135 */       return (new JsonParser()).parse(reader).getAsJsonObject();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/MeowtilsUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */