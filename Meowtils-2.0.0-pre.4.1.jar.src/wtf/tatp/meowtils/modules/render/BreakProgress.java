/*     */ package wtf.tatp.meowtils.modules.render;
/*     */ import java.awt.Color;
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.client.event.RenderWorldLastEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.ColorComponent;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.gui.values.BrightnessValue;
/*     */ import wtf.tatp.meowtils.gui.values.ColorValue;
/*     */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*     */ import wtf.tatp.meowtils.gui.values.SaturationValue;
/*     */ import wtf.tatp.meowtils.mixins.PlayerControllerMPAccessor;
/*     */ import wtf.tatp.meowtils.util.ColorUtil;
/*     */ 
/*     */ public class BreakProgress extends Module {
/*     */   private NumberValue scale;
/*     */   private BooleanValue dynamicColor;
/*     */   private ArrayValue mode;
/*     */   private ColorValue rgb;
/*     */   private SaturationValue saturation;
/*     */   
/*     */   public BreakProgress() {
/*  37 */     super("BreakProgress", "breakProgressKey", "breakProgress", Module.Category.Render);
/*  38 */     tooltip("Displays a countdown until block is broken.");
/*  39 */     addArray(this.mode = new ArrayValue("Mode", Arrays.asList(new String[] { "Percentage", "Time" }, ), "breakProgressMode"));
/*  40 */     this.color = new ColorComponent("breakProgress_red", "breakProgress_green", "breakProgress_blue");
/*  41 */     addColor(this.rgb = new ColorValue("Text color", this.color));
/*  42 */     addSaturation(this.saturation = new SaturationValue(this.color));
/*  43 */     addBrightness(this.brightness = new BrightnessValue(this.color));
/*  44 */     addValue(this.scale = new NumberValue("Scale", 0.5D, 1.5D, 0.05D, null, "breakProgressScale", float.class));
/*  45 */     addBoolean(this.dynamicColor = new BooleanValue("Dynamic color", "breakProgressDynamicColor"));
/*     */   }
/*     */   private BrightnessValue brightness; private ColorComponent color; private float progress; private BlockPos block; private String progressStr;
/*     */   @SubscribeEvent
/*     */   public void onTick(TickEvent.ClientTickEvent event) {
/*  50 */     if (this.mc.field_71439_g == null || this.mc.field_71441_e == null)
/*  51 */       return;  if (this.mc.field_71476_x == null || this.mc.field_71476_x.field_72313_a != MovingObjectPosition.MovingObjectType.BLOCK) {
/*  52 */       resetVariables();
/*     */       
/*     */       return;
/*     */     } 
/*  56 */     this.progress = ((PlayerControllerMPAccessor)this.mc.field_71442_b).getCurBlockDamageMP();
/*  57 */     if (this.progress == 0.0F) {
/*  58 */       resetVariables();
/*     */       
/*     */       return;
/*     */     } 
/*  62 */     this.block = this.mc.field_71476_x.func_178782_a();
/*  63 */     updateProgressString();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderWorld(RenderWorldLastEvent event) {
/*  68 */     if (this.progress == 0.0F || this.block == null)
/*     */       return; 
/*  70 */     double x = this.block.func_177958_n() + 0.5D - (this.mc.func_175598_ae()).field_78730_l;
/*  71 */     double y = this.block.func_177956_o() + 0.5D - (this.mc.func_175598_ae()).field_78731_m;
/*  72 */     double z = this.block.func_177952_p() + 0.5D - (this.mc.func_175598_ae()).field_78728_n;
/*     */     
/*  74 */     int r = cfg.v.breakProgress_red;
/*  75 */     int g = cfg.v.breakProgress_green;
/*  76 */     int b = cfg.v.breakProgress_blue;
/*  77 */     int color = !cfg.v.breakProgressDynamicColor ? (new Color(r, g, b)).getRGB() : ((this.progress > 0.9F) ? ColorUtil.getRGBFromFormatting(EnumChatFormatting.DARK_RED) : ((this.progress > 0.8F) ? ColorUtil.getRGBFromFormatting(EnumChatFormatting.RED) : ((this.progress > 0.7F) ? ColorUtil.getRGBFromFormatting(EnumChatFormatting.GOLD) : ((this.progress > 0.6F) ? ColorUtil.getRGBFromFormatting(EnumChatFormatting.YELLOW) : (new Color(r, g, b)).getRGB()))));
/*  78 */     float scale = cfg.v.breakProgressScale;
/*     */     
/*  80 */     GlStateManager.func_179094_E();
/*  81 */     GlStateManager.func_179137_b(x, y, z);
/*  82 */     GlStateManager.func_179114_b(-(this.mc.func_175598_ae()).field_78735_i, 0.0F, 1.0F, 0.0F);
/*  83 */     GlStateManager.func_179114_b(((this.mc.field_71474_y.field_74320_O == 2) ? -1 : true) * (this.mc.func_175598_ae()).field_78732_j, 1.0F, 0.0F, 0.0F);
/*  84 */     GlStateManager.func_179152_a(-0.02F, -0.02F, -0.02F);
/*     */     
/*  86 */     GlStateManager.func_179132_a(false);
/*  87 */     GlStateManager.func_179097_i();
/*  88 */     GL11.glEnable(3042);
/*     */     
/*  90 */     if (!cfg.v.smoothFont) {
/*  91 */       GlStateManager.func_179094_E();
/*  92 */       GlStateManager.func_179152_a(scale, scale, scale);
/*  93 */       this.mc.field_71466_p.func_175063_a(this.progressStr, -this.mc.field_71466_p.func_78256_a(this.progressStr) / 2.0F / scale, -3.0F / scale, color);
/*  94 */       GlStateManager.func_179121_F();
/*     */     } else {
/*  96 */       Meowtils.fontRenderer.drawScaledStringWithShadow(this.progressStr, -Meowtils.fontRenderer.getStringWidth(this.progressStr, 10.0F) / 2.0F, -3.0F, color, scale * 10.0F);
/*     */     } 
/*     */     
/*  99 */     GL11.glDisable(3042);
/* 100 */     GlStateManager.func_179126_j();
/* 101 */     GlStateManager.func_179132_a(true);
/* 102 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   private void updateProgressString() {
/* 106 */     if (cfg.v.breakProgressMode.equals("Percentage")) {
/* 107 */       this.progressStr = (int)(this.progress * 100.0F) + "%";
/* 108 */     } else if (cfg.v.breakProgressMode.equals("Time")) {
/* 109 */       IBlockState state = this.mc.field_71441_e.func_180495_p(this.block);
/* 110 */       Block blockObj = state.func_177230_c();
/* 111 */       float blockHardness = blockObj.func_176195_g((World)this.mc.field_71441_e, this.block);
/*     */       
/* 113 */       if (blockHardness > 0.0F) {
/* 114 */         float breakSpeed = blockObj.func_180647_a((EntityPlayer)this.mc.field_71439_g, (World)this.mc.field_71441_e, this.block);
/* 115 */         if (breakSpeed > 0.0F) {
/* 116 */           int ticks = (int)Math.ceil(((1.0F - this.progress) / breakSpeed));
/* 117 */           double timeLeft = ticks / 20.0D;
/* 118 */           this.progressStr = String.format("%.1f", new Object[] { Double.valueOf(timeLeft) }).replace('.', ',') + "s";
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void resetVariables() {
/* 125 */     this.progress = 0.0F;
/* 126 */     this.block = null;
/* 127 */     this.progressStr = "";
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/render/BreakProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */