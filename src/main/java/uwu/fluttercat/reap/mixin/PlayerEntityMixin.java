package uwu.fluttercat.reap.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uwu.fluttercat.reap.item.ModItems;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Shadow
    protected abstract boolean isEquipped(Item item);

    @Inject(at = @At("HEAD"), method = "shouldRenderName", cancellable = true)
	private void init(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!this.isEquipped(ModItems.HOOD));
    }



//    @Override
//    public boolean shouldRenderName() {
//        return true;
//    }
}