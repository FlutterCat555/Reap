package dev.fluttercat.reap.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.fluttercat.reap.item.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

@Mixin(Player.class)
public abstract class PlayerEntityMixin {

    @Shadow
    protected abstract boolean isEquipped(Item item);

    @Inject(at = @At("HEAD"), method = "shouldShowName", cancellable = true)
	private void init(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!this.isEquipped(ModItems.HOOD));
    }



//    @Override
//    public boolean shouldRenderName() {
//        return true;
//    }
}