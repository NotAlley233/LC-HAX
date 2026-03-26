/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.util.CapeCache;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({AbstractClientPlayer.class})
/*    */ public abstract class MixinAbstractClientPlayer_Cape
/*    */   extends EntityPlayer {
/*    */   public MixinAbstractClientPlayer_Cape(World world, GameProfile profile) {
/* 22 */     super(world, profile);
/*    */   }
/*    */   
/*    */   @Inject(method = {"getLocationCape"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void onGetLocationCape(CallbackInfoReturnable<ResourceLocation> cir) {
/* 27 */     if (this instanceof net.minecraft.client.entity.EntityPlayerSP && cfg.v.capeSelector) {
/* 28 */       ResourceLocation cape = CapeCache.getCape(cfg.v.selectedCape);
/* 29 */       if (cape != null)
/* 30 */         cir.setReturnValue(cape); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinAbstractClientPlayer_Cape.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */