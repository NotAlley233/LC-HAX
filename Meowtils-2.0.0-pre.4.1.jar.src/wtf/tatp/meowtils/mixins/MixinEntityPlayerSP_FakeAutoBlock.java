/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.modules.render.FakeAutoBlock;
/*    */ import wtf.tatp.meowtils.util.User32Util;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({EntityPlayerSP.class})
/*    */ public abstract class MixinEntityPlayerSP_FakeAutoBlock
/*    */   extends AbstractClientPlayer {
/*    */   public MixinEntityPlayerSP_FakeAutoBlock(World worldIn, GameProfile playerProfile) {
/* 23 */     super(worldIn, playerProfile);
/*    */   }
/*    */ 
/*    */   
/*    */   @Inject(method = {"onUpdate"}, at = {@At("HEAD")})
/*    */   public void onUpdate(CallbackInfo ci) {}
/*    */ 
/*    */   
/*    */   public int func_71052_bv() {
/* 32 */     ItemStack heldItem = func_70694_bm();
/* 33 */     boolean fakeAutoblock = (cfg.v.fakeAutoBlock && FakeAutoBlock.isSword(heldItem) && ((cfg.v.blockhitOnly && User32Util.holdingRightClick()) || (!cfg.v.blockhitOnly && this.field_82175_bq) || (cfg.v.fakeAutoBlockKillauraOnly && FakeAutoBlock.isKillaura())));
/*    */     
/* 35 */     return fakeAutoblock ? 10 : super.func_71052_bv();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean func_70632_aY() {
/* 40 */     ItemStack heldItem = func_70694_bm();
/* 41 */     boolean fakeAutoblock = (cfg.v.fakeAutoBlock && FakeAutoBlock.isSword(heldItem) && ((cfg.v.blockhitOnly && User32Util.holdingRightClick()) || (!cfg.v.blockhitOnly && this.field_82175_bq) || (cfg.v.fakeAutoBlockKillauraOnly && FakeAutoBlock.isKillaura())));
/*    */     
/* 43 */     return (fakeAutoblock || super.func_70632_aY());
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinEntityPlayerSP_FakeAutoBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */