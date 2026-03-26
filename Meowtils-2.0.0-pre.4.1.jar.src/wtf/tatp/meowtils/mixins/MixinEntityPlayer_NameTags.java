/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.modules.hypixel.Denicker;
/*    */ import wtf.tatp.meowtils.util.BlacklistUtil;
/*    */ import wtf.tatp.meowtils.util.NickDetection;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({EntityPlayer.class})
/*    */ public abstract class MixinEntityPlayer_NameTags
/*    */   extends EntityLivingBase {
/*    */   public MixinEntityPlayer_NameTags(World worldIn) {
/* 24 */     super(worldIn);
/*    */   }
/*    */   
/*    */   @Inject(method = {"getDisplayName"}, at = {@At("RETURN")}, cancellable = true)
/*    */   public void injectIconsIntoNametag(CallbackInfoReturnable<IChatComponent> cir) {
/* 29 */     EntityPlayer player = (EntityPlayer)this;
/*    */     
/* 31 */     IChatComponent original = (IChatComponent)cir.getReturnValue();
/* 32 */     String icon = BlacklistUtil.getFormattedIcon(player.func_110124_au().toString(), player.func_70005_c_());
/* 33 */     boolean isNicked = NickDetection.isNicked(player);
/* 34 */     String nickIcon = (isNicked && cfg.v.nickReveal) ? Denicker.NICK_ICON : null;
/*    */     
/* 36 */     if (icon == null && nickIcon == null)
/*    */       return; 
/* 38 */     if (original instanceof ChatComponentText && original.func_150253_a().isEmpty()) {
/*    */       
/* 40 */       ChatComponentText text = (ChatComponentText)original;
/* 41 */       String modified = ((icon != null) ? icon : "") + text.func_150265_g() + ((nickIcon != null) ? (" " + nickIcon) : "");
/* 42 */       cir.setReturnValue((new ChatComponentText(modified)).func_150255_a(text.func_150256_b()));
/*    */     } else {
/*    */       
/* 45 */       ChatComponentText wrapper = new ChatComponentText("");
/* 46 */       if (icon != null) wrapper.func_150258_a(icon); 
/* 47 */       wrapper.func_150257_a(original.func_150259_f());
/* 48 */       if (nickIcon != null) wrapper.func_150258_a(" " + nickIcon); 
/* 49 */       cir.setReturnValue(wrapper);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinEntityPlayer_NameTags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */