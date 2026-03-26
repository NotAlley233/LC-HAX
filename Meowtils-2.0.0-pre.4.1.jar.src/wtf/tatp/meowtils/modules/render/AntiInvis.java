/*    */ package wtf.tatp.meowtils.modules.render;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraftforge.client.event.RenderPlayerEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.gui.ColorComponent;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.OpacityValue;
/*    */ import wtf.tatp.meowtils.modules.advanced.AntiBot;
/*    */ 
/*    */ public class AntiInvis
/*    */   extends Module {
/*    */   private ColorComponent color;
/*    */   private OpacityValue opacity;
/*    */   
/*    */   public AntiInvis() {
/* 19 */     super("Anti-Invis", "antiInvisKey", "antiInvis", Module.Category.Render);
/* 20 */     tooltip("Renders invisible players semi-transparent instead.");
/* 21 */     addOpacity(this.opacity = new OpacityValue("Opacity", "antiInvisOpacity", this.color));
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onRenderPlayer(RenderPlayerEvent.Pre event) {
/* 26 */     EntityPlayer player = event.entityPlayer;
/* 27 */     float opacity = cfg.v.antiInvisOpacity / 100.0F;
/*    */     
/* 29 */     if (!shouldBeTransparent(player))
/*    */       return; 
/* 31 */     if (player.func_82150_aj()) {
/* 32 */       player.func_82142_c(false);
/* 33 */       player.getEntityData().func_74757_a("Meowtils_wasInvisible", true);
/*    */     } 
/*    */     
/* 36 */     if (opacity < 1.0F) {
/* 37 */       GL11.glEnable(3042);
/* 38 */       GL11.glBlendFunc(770, 771);
/* 39 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, opacity);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onRenderPlayerPost(RenderPlayerEvent.Post event) {
/* 46 */     EntityPlayer player = event.entityPlayer;
/* 47 */     float opacity = cfg.v.antiInvisOpacity / 100.0F;
/*    */     
/* 49 */     if (player.getEntityData().func_74767_n("Meowtils_wasInvisible")) {
/* 50 */       player.func_82142_c(true);
/* 51 */       player.getEntityData().func_74757_a("Meowtils_wasInvisible", false);
/*    */     } 
/*    */     
/* 54 */     if (opacity < 1.0F) {
/* 55 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 56 */       GL11.glDisable(3042);
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean shouldBeTransparent(EntityPlayer player) {
/* 61 */     if (cfg.v.debugMessages) return true; 
/* 62 */     return (player != this.mc.field_71439_g && player
/* 63 */       .func_82150_aj() && 
/* 64 */       !AntiBot.isBot(player));
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/render/AntiInvis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */