/*     */ package wtf.tatp.meowtils.modules.skywars;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import wtf.tatp.meowtils.DelayedTask;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.ColorComponent;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.gui.values.BrightnessValue;
/*     */ import wtf.tatp.meowtils.gui.values.ColorValue;
/*     */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*     */ import wtf.tatp.meowtils.gui.values.SaturationValue;
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ 
/*     */ public class CooldownHUD
/*     */   extends Module {
/*     */   private NumberValue scale;
/*     */   private ColorValue rgb;
/*     */   private SaturationValue saturation;
/*  31 */   private static int cooldown = 0; private BrightnessValue brightness; private BooleanValue hide; private ColorComponent color;
/*     */   private static boolean ready = false;
/*     */   private static boolean thrownPearl = false;
/*  34 */   private static int tickCounter = 0;
/*  35 */   private String lastActionbar = "";
/*  36 */   private static String itemName = "";
/*     */   public CooldownHUD() {
/*  38 */     super("CooldownHUD", "cooldownHUDKey", "cooldownHUD", Module.Category.Skywars);
/*  39 */     tooltip("Display item cooldowns on screen.");
/*  40 */     this.color = new ColorComponent("cooldownHUDRed", "cooldownHUDGreen", "cooldownHUDBlue");
/*  41 */     addColor(this.rgb = new ColorValue("Text color", this.color));
/*  42 */     addSaturation(this.saturation = new SaturationValue(this.color));
/*  43 */     addBrightness(this.brightness = new BrightnessValue(this.color));
/*  44 */     addValue(this.scale = new NumberValue("Scale", 0.5D, 1.5D, 0.05D, null, "cooldownHUDScale", float.class));
/*  45 */     addBoolean(this.hide = new BooleanValue("Hide after cooldown", "cooldownHUDHideWhenReady"));
/*     */   }
/*     */   @SubscribeEvent
/*     */   public void onRenderTick(TickEvent.RenderTickEvent event) {
/*  49 */     Minecraft mc = Minecraft.func_71410_x();
/*  50 */     if (mc.field_71441_e == null || mc.field_71439_g == null || event.phase != TickEvent.Phase.END)
/*  51 */       return;  if (!GamemodeUtil.skywarsGame)
/*  52 */       return;  if (mc.field_71462_r != null)
/*  53 */       return;  if ((ready || thrownPearl) && cfg.v.cooldownHUDHideWhenReady)
/*  54 */       return;  float scale = cfg.v.cooldownHUDScale;
/*     */     
/*  56 */     if (!itemName.isEmpty()) {
/*  57 */       String displayCooldown = ready ? (EnumChatFormatting.GREEN + "Ready") : (thrownPearl ? (EnumChatFormatting.RED + "Thrown") : (EnumChatFormatting.RED.toString() + cooldown + "s"));
/*  58 */       int color = (new Color(cfg.v.cooldownHUDRed, cfg.v.cooldownHUDGreen, cfg.v.cooldownHUDBlue)).getRGB();
/*     */ 
/*     */       
/*  61 */       if (!cfg.v.smoothFont) {
/*  62 */         GlStateManager.func_179094_E();
/*  63 */         GlStateManager.func_179152_a(scale, scale, scale);
/*  64 */         mc.field_71466_p.func_175063_a(itemName + displayCooldown, cfg.v.cooldownHUDxPos / scale, cfg.v.cooldownHUDyPos / scale, color);
/*  65 */         GlStateManager.func_179121_F();
/*     */       } else {
/*  67 */         Meowtils.fontRenderer.drawScaledStringWithShadow(itemName + displayCooldown, cfg.v.cooldownHUDxPos, cfg.v.cooldownHUDyPos, color, scale * 10.0F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   @SubscribeEvent
/*     */   public void onChatReceived(ClientChatReceivedEvent event) {
/*  73 */     String message = event.message.func_150260_c();
/*     */     
/*  75 */     if (event.type == 2) {
/*  76 */       this.lastActionbar = message;
/*  77 */     } else if (event.type == 0 && message.equals("The game starts in 1 second!")) {
/*  78 */       if (this.lastActionbar.contains("Enderman")) {
/*  79 */         new DelayedTask(() -> startCooldown("Corrupt Pearl: ", 30), 20);
/*  80 */       } else if (this.lastActionbar.contains("Enderchest")) {
/*  81 */         new DelayedTask(() -> startCooldown("Enderchest: ", 60), 20);
/*  82 */       } else if (this.lastActionbar.contains("End Lord")) {
/*  83 */         new DelayedTask(() -> startCooldown("End Lord: ", 30), 20);
/*  84 */       } else if (this.lastActionbar.contains("Cryomancer")) {
/*  85 */         new DelayedTask(() -> startCooldown("Ice Bridge: ", 30), 20);
/*  86 */       } else if (this.lastActionbar.contains("Chronobreaker")) {
/*  87 */         new DelayedTask(() -> startCooldown("Echo: ", 8), 20);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   @SubscribeEvent
/*     */   public void onClientTick(TickEvent.ClientTickEvent event) {
/*  93 */     if (event.phase != TickEvent.Phase.END)
/*     */       return; 
/*  95 */     if (cooldown > 0) {
/*  96 */       tickCounter++;
/*  97 */       if (tickCounter >= 20) {
/*  98 */         cooldown--;
/*  99 */         tickCounter = 0;
/*     */         
/* 101 */         if (cooldown <= 0) {
/* 102 */           cooldown = 0;
/* 103 */           ready = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   @SubscribeEvent
/*     */   public void onMouseInput(InputEvent.MouseInputEvent event) {
/* 110 */     Minecraft mc = Minecraft.func_71410_x();
/* 111 */     if (mc.field_71439_g == null || mc.field_71441_e == null)
/*     */       return; 
/* 113 */     if (Mouse.isButtonDown(1)) {
/* 114 */       ItemStack held = mc.field_71439_g.func_70694_bm();
/*     */       
/* 116 */       if (held != null && held.func_77973_b() == Items.field_151079_bi && held.func_77962_s() && itemName.equals("Corrupt Pearl: ")) {
/* 117 */         if (ready) {
/* 118 */           thrownPearl = true;
/* 119 */           ready = false;
/*     */         } 
/* 121 */       } else if (held != null && held.func_77973_b() == Items.field_151113_aN && itemName.equals("Echo: ") && 
/* 122 */         ready) {
/* 123 */         ready = false;
/* 124 */         startCooldown("Echo: ", 40);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void startCooldown(String name, int seconds) {
/* 131 */     itemName = name;
/* 132 */     cooldown = seconds;
/* 133 */     ready = false;
/* 134 */     tickCounter = 0;
/*     */   }
/*     */   public static void clear() {
/* 137 */     cooldown = 0;
/* 138 */     ready = false;
/* 139 */     thrownPearl = false;
/* 140 */     tickCounter = 0;
/* 141 */     itemName = "";
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/skywars/CooldownHUD.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */