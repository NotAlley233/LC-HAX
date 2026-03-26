/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
/*    */ import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
/*    */ import org.spongepowered.asm.launch.MixinBootstrap;
/*    */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*    */ import org.spongepowered.asm.mixin.Mixins;
/*    */ 
/*    */ @MCVersion("1.8.9")
/*    */ public class MixinLoader
/*    */   implements IFMLLoadingPlugin {
/*    */   public MixinLoader() {
/* 14 */     MixinBootstrap.init();
/* 15 */     Mixins.addConfiguration("mixins.meowtils.json");
/* 16 */     MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getASMTransformerClass() {
/* 21 */     return new String[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public String getModContainerClass() {
/* 26 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSetupClass() {
/* 31 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void injectData(Map<String, Object> data) {}
/*    */ 
/*    */   
/*    */   public String getAccessTransformerClass() {
/* 39 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */