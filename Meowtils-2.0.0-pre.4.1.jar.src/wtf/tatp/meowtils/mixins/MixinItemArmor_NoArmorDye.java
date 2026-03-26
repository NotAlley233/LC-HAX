/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import net.minecraft.item.ItemArmor;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({ItemArmor.class})
/*    */ public abstract class MixinItemArmor_NoArmorDye
/*    */ {
/*    */   @Inject(method = {"getColorFromItemStack"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void onGetColorFromItemStack(ItemStack stack, int renderPass, CallbackInfoReturnable<Integer> cir) {
/* 20 */     ItemArmor self = (ItemArmor)this;
/*    */     
/* 22 */     if (cfg.v.noArmorDye && GamemodeUtil.skywarsGame && self.func_82812_d() == ItemArmor.ArmorMaterial.LEATHER && (cfg.v.noArmorDyeMode.equals("Item") || cfg.v.noArmorDyeMode.equals("Both")))
/* 23 */       cir.setReturnValue(Integer.valueOf(10511680)); 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinItemArmor_NoArmorDye.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */