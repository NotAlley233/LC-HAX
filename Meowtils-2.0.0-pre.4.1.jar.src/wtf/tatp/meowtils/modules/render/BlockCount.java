/*    */ package wtf.tatp.meowtils.modules.render;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.gui.ColorComponent;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ import wtf.tatp.meowtils.gui.values.BrightnessValue;
/*    */ import wtf.tatp.meowtils.gui.values.ColorValue;
/*    */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*    */ import wtf.tatp.meowtils.gui.values.SaturationValue;
/*    */ import wtf.tatp.meowtils.util.PlaySound;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BlockCount
/*    */   extends Module
/*    */ {
/*    */   private BooleanValue alert;
/*    */   private BooleanValue sound;
/*    */   private NumberValue threshold;
/*    */   private NumberValue scale;
/*    */   
/*    */   public BlockCount() {
/* 31 */     super("BlockCount", "blockCountKey", "blockCount", Module.Category.Render);
/* 32 */     tooltip("Displays block count on screen");
/* 33 */     this.color = new ColorComponent("blockCount_red", "blockCount_green", "blockCount_blue");
/* 34 */     addColor(this.rgb = new ColorValue("Text color", this.color));
/* 35 */     addSaturation(this.saturation = new SaturationValue(this.color));
/* 36 */     addBrightness(this.brightness = new BrightnessValue(this.color));
/* 37 */     addValue(this.scale = new NumberValue("Scale", 0.5D, 1.5D, 0.05D, null, "blockCountScale", float.class));
/* 38 */     addValue(this.threshold = new NumberValue("Alert threshold", 1.0D, 16.0D, 1.0D, "blocks", "blockCountThreshold", int.class));
/* 39 */     addBoolean(this.alert = new BooleanValue("Alert when low", "blockCountAlert"));
/* 40 */     addBoolean(this.sound = new BooleanValue("Ping sound", "blockCountSound"));
/*    */   }
/*    */   private ColorValue rgb; private SaturationValue saturation; private BrightnessValue brightness; private ColorComponent color; private static boolean alerted = false;
/*    */   @SubscribeEvent
/*    */   public void onRenderTick(TickEvent.RenderTickEvent event) {
/* 45 */     Minecraft mc = Minecraft.func_71410_x();
/* 46 */     if (mc.field_71439_g == null || mc.field_71441_e == null || event.phase != TickEvent.Phase.END)
/* 47 */       return;  if (mc.field_71439_g.func_70694_bm() == null)
/* 48 */       return;  if (mc.field_71462_r != null)
/* 49 */       return;  int currentStack = (mc.field_71439_g.func_70694_bm()).field_77994_a;
/*    */     
/* 51 */     float scale = cfg.v.blockCountScale;
/* 52 */     int threshold = cfg.v.blockCountThreshold;
/* 53 */     int x = cfg.v.blockCount_x;
/* 54 */     int y = cfg.v.blockCount_y;
/* 55 */     int color = (currentStack <= threshold) ? (new Color(170, 0, 0)).getRGB() : (new Color(cfg.v.blockCount_red, cfg.v.blockCount_green, cfg.v.blockCount_blue)).getRGB();
/*    */     
/* 57 */     if (mc.field_71439_g.func_70694_bm().func_77973_b() instanceof net.minecraft.item.ItemBlock) {
/* 58 */       if (!cfg.v.smoothFont) {
/* 59 */         GlStateManager.func_179094_E();
/* 60 */         GlStateManager.func_179152_a(scale, scale, scale);
/* 61 */         mc.field_71466_p.func_175063_a(String.valueOf(currentStack), x / scale, y / scale, color);
/* 62 */         GlStateManager.func_179121_F();
/*    */       } else {
/* 64 */         Meowtils.fontRenderer.drawScaledStringWithShadow(String.valueOf(currentStack), x, y, color, scale * 10.0F);
/*    */       } 
/*    */       
/* 67 */       if (currentStack == threshold && !alerted) {
/* 68 */         if (cfg.v.blockCountAlert) {
/* 69 */           String end = (cfg.v.blockCountThreshold == 1) ? " block left!" : " blocks left!";
/* 70 */           Meowtils.addMessage(EnumChatFormatting.RED + "You only have " + currentStack + end);
/*    */         } 
/* 72 */         if (cfg.v.blockCountSound) {
/* 73 */           PlaySound.getInstance().playPingSoundDeep();
/*    */         }
/* 75 */         alerted = true;
/* 76 */       } else if (currentStack > threshold) {
/* 77 */         alerted = false;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/render/BlockCount.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */