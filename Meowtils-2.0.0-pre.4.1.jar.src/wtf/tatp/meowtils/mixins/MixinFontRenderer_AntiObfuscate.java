/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.ModifyVariable;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({FontRenderer.class})
/*    */ public class MixinFontRenderer_AntiObfuscate
/*    */ {
/*    */   @ModifyVariable(method = {"renderString"}, at = @At("HEAD"), ordinal = 0, argsOnly = true)
/*    */   private String modifyRenderString(String string) {
/* 17 */     if (string == null) return null;
/*    */     
/* 19 */     if (cfg.v.antiObfuscate) {
/* 20 */       string = string.replaceAll("§k", "");
/*    */     }
/*    */     
/* 23 */     return string;
/*    */   }
/*    */   
/*    */   @ModifyVariable(method = {"getStringWidth"}, at = @At("HEAD"), ordinal = 0, argsOnly = true)
/*    */   private String modifyStringWidth(String string) {
/* 28 */     if (string == null) return null;
/*    */     
/* 30 */     if (cfg.v.antiObfuscate) {
/* 31 */       string = string.replaceAll("§k", "");
/*    */     }
/*    */     
/* 34 */     return string;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinFontRenderer_AntiObfuscate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */