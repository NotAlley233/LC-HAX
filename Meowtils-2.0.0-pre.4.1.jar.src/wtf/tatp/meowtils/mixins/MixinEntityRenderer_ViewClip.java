/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import net.minecraft.client.renderer.EntityRenderer;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.ModifyVariable;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({EntityRenderer.class})
/*    */ public class MixinEntityRenderer_ViewClip
/*    */ {
/*    */   @ModifyVariable(method = {"orientCamera"}, at = @At("STORE"), ordinal = 0)
/*    */   private double modifyCameraDistance(double original) {
/* 17 */     return cfg.v.viewClip ? 10000.0D : original;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinEntityRenderer_ViewClip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */