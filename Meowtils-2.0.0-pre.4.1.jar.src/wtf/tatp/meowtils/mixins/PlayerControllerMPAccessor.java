package wtf.tatp.meowtils.mixins;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SideOnly(Side.CLIENT)
@Mixin({PlayerControllerMP.class})
public interface PlayerControllerMPAccessor {
  @Accessor("curBlockDamageMP")
  float getCurBlockDamageMP();
  
  @Accessor("blockHitDelay")
  void setBlockHitDelay(int paramInt);
  
  @Accessor("blockHitDelay")
  int getBlockHitDelay();
}


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/PlayerControllerMPAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */