package dev.fluttercat.reap.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SculkChargeParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NailItem extends Item {
    public NailItem(Properties properties) {
        super(properties);
    }

    @Override
    public void hurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {
        Level world = mob.level();
        if(mob.position().y+1<attacker.position().y) {
            if (!world.isClientSide()) {
                Vec3 move = attacker.getDeltaMovement();
                attacker.setDeltaMovement(move.x, 0.5, move.z);
                attacker.hurtMarked = true;
                attacker.setIgnoreFallDamageFromCurrentImpulse(true, attacker.position());
                ((ServerLevel) world).sendParticles(ParticleTypes.SWEEP_ATTACK,mob.getX()/2+attacker.getX()/2,mob.getY()/2+attacker.getY()/2+1,mob.getZ()/2+attacker.getZ()/2,1,0,0,0,3); //i still dont get this but intellij fixed it??
            }
        }
        super.hurtEnemy(itemStack,mob,attacker);
    }
}
