/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({FMLHandshakeMessage.ModList.class})
/*    */ public abstract class MixinModList
/*    */ {
/*    */   @Shadow(remap = false)
/*    */   private Map<String, String> modTags;
/*    */   
/*    */   @Inject(method = {"<init>(Ljava/util/List;)V"}, at = {@At("RETURN")}, remap = false)
/*    */   public void onInit(CallbackInfo ci) {
/* 24 */     if (Minecraft.func_71410_x().func_71387_A()) {
/*    */       return;
/*    */     }
/* 27 */     if (cfg.v.modID.equals("None")) {
/*    */       return;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 33 */     if (cfg.v.modID.equals("Meowtils"))
/* 34 */       this.modTags.remove("meowtils"); 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinModList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */