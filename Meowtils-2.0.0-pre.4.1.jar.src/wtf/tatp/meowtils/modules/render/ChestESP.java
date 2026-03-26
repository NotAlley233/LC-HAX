/*     */ package wtf.tatp.meowtils.modules.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.client.event.RenderWorldLastEvent;
/*     */ import net.minecraftforge.event.entity.player.PlayerInteractEvent;
/*     */ import net.minecraftforge.event.world.WorldEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.ColorComponent;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.gui.values.BrightnessValue;
/*     */ import wtf.tatp.meowtils.gui.values.ColorValue;
/*     */ import wtf.tatp.meowtils.gui.values.SaturationValue;
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ import wtf.tatp.meowtils.util.Render;
/*     */ 
/*     */ public class ChestESP
/*     */   extends Module
/*     */ {
/*     */   private ColorValue rgb;
/*     */   private SaturationValue saturation;
/*     */   private BrightnessValue brightness;
/*     */   private ColorValue rgbOpened;
/*  33 */   private final List<BlockPos> highlightedChestPositions = new CopyOnWriteArrayList<>(); private ColorComponent color; private ColorComponent openedColor; private ArrayValue renderMode; private ArrayValue mode; private BooleanValue skywarsOnly;
/*     */   public ChestESP() {
/*  35 */     super("ChestESP", "chestESPKey", "chestESP", Module.Category.Render);
/*  36 */     tooltip("Highlights chests");
/*  37 */     this.color = new ColorComponent("chestESP_red", "chestESP_green", "chestESP_blue");
/*  38 */     this.openedColor = new ColorComponent("chestESPOpened_red", "chestESPOpened_green", "chestESPOpened_blue");
/*  39 */     addColor(this.rgb = new ColorValue("Chest color", this.color));
/*  40 */     addSaturation(this.saturation = new SaturationValue(this.color));
/*  41 */     addBrightness(this.brightness = new BrightnessValue(this.color));
/*  42 */     addColor(this.rgbOpened = new ColorValue("Opened color", this.openedColor));
/*  43 */     addArray(this.mode = new ArrayValue("Mode", Arrays.asList(new String[] { "Opened", "Normal", "Both" }, ), "chestESPMode"));
/*  44 */     addArray(this.renderMode = new ArrayValue("Render", Arrays.asList(new String[] { "Full", "Outline" }, ), "chestESPRenderMode"));
/*  45 */     addBoolean(this.skywarsOnly = new BooleanValue("Skywars only", "chestESPSkywarsOnly"));
/*     */   }
/*     */   @SubscribeEvent
/*     */   public void onInteract(PlayerInteractEvent event) {
/*  49 */     if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
/*  50 */       return;  if (event.entityPlayer != this.mc.field_71439_g)
/*     */       return; 
/*  52 */     BlockPos pos = event.pos;
/*  53 */     TileEntity tile = this.mc.field_71441_e.func_175625_s(pos);
/*     */     
/*  55 */     if (tile instanceof net.minecraft.tileentity.TileEntityChest && 
/*  56 */       !this.highlightedChestPositions.contains(pos)) {
/*  57 */       this.highlightedChestPositions.add(pos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onWorldUnload(WorldEvent.Unload event) {
/*  64 */     this.highlightedChestPositions.clear();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onWorldRender(RenderWorldLastEvent event) {
/*  69 */     if (cfg.v.chestESPSkywarsOnly && !GamemodeUtil.skywarsGame && !GamemodeUtil.skywarsMiniGame)
/*  70 */       return;  boolean full = cfg.v.chestESPRenderMode.equals("Full");
/*  71 */     boolean outline = cfg.v.chestESPRenderMode.equals("Outline");
/*  72 */     int r = cfg.v.chestESP_red;
/*  73 */     int g = cfg.v.chestESP_green;
/*  74 */     int b = cfg.v.chestESP_blue;
/*  75 */     String mode = cfg.v.chestESPMode;
/*     */     
/*  77 */     for (TileEntity tile : this.mc.field_71441_e.field_147482_g) {
/*  78 */       Color fillColor, outlineColor; if (!(tile instanceof net.minecraft.tileentity.TileEntityChest))
/*  79 */         continue;  BlockPos pos = tile.func_174877_v();
/*     */       
/*  81 */       if (mode.equals("Opened") && !this.highlightedChestPositions.contains(pos)) {
/*     */         continue;
/*     */       }
/*     */       
/*  85 */       AxisAlignedBB box = this.mc.field_71441_e.func_180495_p(pos).func_177230_c().func_180646_a((World)this.mc.field_71441_e, pos);
/*  86 */       if (box == null)
/*  87 */         continue;  box = Render.getAdjustedBox(box, this.mc.field_71441_e.func_180495_p(pos).func_177230_c());
/*     */       
/*  89 */       boolean opened = this.highlightedChestPositions.contains(pos);
/*     */ 
/*     */       
/*  92 */       if (mode.equals("Opened")) {
/*  93 */         fillColor = new Color(r, g, b, 120);
/*  94 */         outlineColor = new Color(r, g, b, 255);
/*  95 */       } else if (mode.equals("Normal")) {
/*  96 */         fillColor = new Color(r, g, b, 120);
/*  97 */         outlineColor = new Color(r, g, b, 255);
/*  98 */       } else if (mode.equals("Both")) {
/*  99 */         if (opened) {
/* 100 */           fillColor = new Color(cfg.v.chestESPOpened_red, cfg.v.chestESPOpened_green, cfg.v.chestESPOpened_blue, 120);
/* 101 */           outlineColor = new Color(cfg.v.chestESPOpened_red, cfg.v.chestESPOpened_green, cfg.v.chestESPOpened_blue, 255);
/*     */         } else {
/* 103 */           fillColor = new Color(r, g, b, 120);
/* 104 */           outlineColor = new Color(r, g, b, 255);
/*     */         } 
/*     */       } else {
/*     */         continue;
/*     */       } 
/*     */       
/* 110 */       Render.draw3DBlockBox(box, full, fillColor, outline, outlineColor, 0.06D, 0.06D, 0.06D);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/render/ChestESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */