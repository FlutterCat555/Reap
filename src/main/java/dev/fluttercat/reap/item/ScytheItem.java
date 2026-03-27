package dev.fluttercat.reap.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;


public class ScytheItem extends HoeItem {

    public ScytheItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Level world = target.level();
        if (!world.isClientSide()) {
            double flip = attacker.getDeltaMovement().y() / -2;
            double dx = (attacker.getX() - target.getX()) / 4;
            double dz = (attacker.getZ() - target.getZ()) / 4;
            target.push(dx, flip, dz);
            target.hurtMarked = true;
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

            if (block instanceof CropBlock) {
                if(((CropBlock) block).isMaxAge(state)){
                    if(!world.isClientSide()) {
                        world.destroyBlock(pos, true, context.getPlayer());
                        world.setBlockAndUpdate(pos, block.defaultBlockState());
                    }
                    return InteractionResult.SUCCESS;
                }
            } else if (state.is(BlockTags.REPLACEABLE) || state.is(BlockTags.FLOWERS)) {
                int toDamage = 0;
                if (!world.isClientSide()) {
                    for (int x = 2; x >= -2; --x) {
                        for (int z = -2; z <= 2; ++z) {
                            BlockPos targeted = pos.offset(x, 0, z);
                            BlockState targetedState = world.getBlockState(targeted);
                            if (targetedState.is(BlockTags.REPLACEABLE) || targetedState.is(BlockTags.FLOWERS)) {
                                world.destroyBlock(targeted, true, context.getPlayer());
                                toDamage++;
                            }
                        }
                    }
                    if(context.getPlayer()!=null) {
                        context.getItemInHand().hurtAndBreak(toDamage, context.getPlayer(), context.getHand().asEquipmentSlot());
                    }
                }
                world.addParticle(ParticleTypes.SWEEP_ATTACK, pos.getX(),pos.getY()+0.5,pos.getZ(),0,0,0);
                world.playSound(context.getPlayer(), pos, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            } else {
                InteractionResult staticResult = super.useOn(context);
                if(!world.isClientSide()) {
                    return staticResult;
                }
                return staticResult;
            }
        return InteractionResult.PASS;
    }
}
