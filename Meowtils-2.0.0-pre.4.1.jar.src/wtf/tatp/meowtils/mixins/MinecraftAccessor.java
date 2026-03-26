package wtf.tatp.meowtils.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SideOnly(Side.CLIENT)
@Mixin({Minecraft.class})
public interface MinecraftAccessor {
  @Accessor("timer")
  Timer getTimer();
  
  @Accessor("leftClickCounter")
  void setLeftClickCounter(int paramInt);
}


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MinecraftAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */