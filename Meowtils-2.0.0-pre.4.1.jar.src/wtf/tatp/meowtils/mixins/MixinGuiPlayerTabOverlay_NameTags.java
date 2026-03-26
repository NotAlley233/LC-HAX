/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiPlayerTabOverlay;
/*    */ import net.minecraft.client.network.NetworkPlayerInfo;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ import wtf.tatp.meowtils.util.NameUtil;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({GuiPlayerTabOverlay.class})
/*    */ public class MixinGuiPlayerTabOverlay_NameTags
/*    */ {
/*    */   @Inject(method = {"getPlayerName"}, at = {@At("RETURN")}, cancellable = true)
/*    */   public void injectIconsIntoTab(NetworkPlayerInfo info, CallbackInfoReturnable<String> cir) {
/* 19 */     cir.setReturnValue(NameUtil.appendTabName(info, (String)cir.getReturnValue()));
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinGuiPlayerTabOverlay_NameTags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */