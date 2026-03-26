/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
/*    */ import net.minecraft.item.ItemArmor;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({LayerArmorBase.class})
/*    */ public abstract class MixinLayerArmorBase_NoArmorDye<T extends ModelBase>
/*    */ {
/*    */   @Redirect(method = {"renderLayer"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemArmor;getColor(Lnet/minecraft/item/ItemStack;)I"))
/*    */   private int redirectLeatherColor(ItemArmor item, ItemStack stack) {
/* 21 */     if (cfg.v.noArmorDye && GamemodeUtil.skywarsGame && item.func_82812_d() == ItemArmor.ArmorMaterial.LEATHER && (cfg.v.noArmorDyeMode.equals("Model") || cfg.v.noArmorDyeMode.equals("Both"))) {
/* 22 */       return 10511680;
/*    */     }
/* 24 */     return item.func_82814_b(stack);
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinLayerArmorBase_NoArmorDye.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */