package uwu.fluttercat.reap.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ScytheItem extends HoeItem {

    public ScytheItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = target.getEntityWorld();
        if (!world.isClient()) {
            double flip = attacker.getVelocity().getY() / -4;
            double dx = (attacker.getX() - target.getX()) / 4;
            double dz = (attacker.getZ() - target.getZ()) / 4;
            target.addVelocity(dx, flip, dz);
            target.velocityModified = true;
            target.velocityDirty = true;
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

            if (block instanceof CropBlock) {
                int age = state.get(CropBlock.AGE);
                if (age == 7) {
                    if(!world.isClient()) {
                        world.breakBlock(pos, true, context.getPlayer());
                        world.setBlockState(pos, block.getDefaultState());
                    }
                    return ActionResult.SUCCESS;
                }
            } else if (state.isIn(BlockTags.REPLACEABLE) || state.isIn(BlockTags.FLOWERS)) {
                int toDamage = 0;
                if (!world.isClient()) {
                    for (int x = 2; x >= -2; --x) {
                        for (int z = -2; z <= 2; ++z) {
                            BlockPos targeted = pos.add(x, 0, z);
                            BlockState targetedState = world.getBlockState(targeted);
                            if (targetedState.isIn(BlockTags.REPLACEABLE) || targetedState.isIn(BlockTags.FLOWERS)) {
                                world.breakBlock(targeted, true, context.getPlayer());
                                toDamage++;
                            }
                        }
                    }
                    if(context.getPlayer()!=null) {
                        context.getStack().damage(toDamage, context.getPlayer(), context.getHand().getEquipmentSlot());
                    }
                }
                world.addParticleClient(ParticleTypes.SWEEP_ATTACK, pos.getX(),pos.getY()+0.5,pos.getZ(),0,0,0);
                world.playSound(context.getPlayer(), pos, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return ActionResult.SUCCESS;
            } else {
                ActionResult staticResult = super.useOnBlock(context);
                if(!world.isClient()) {
                    return staticResult;
                }
                return staticResult;
            }
        return ActionResult.PASS;
    }
}
