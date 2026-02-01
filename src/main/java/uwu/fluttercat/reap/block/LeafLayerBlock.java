package uwu.fluttercat.reap.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class LeafLayerBlock extends Block {
    public static final MapCodec<LeafLayerBlock> CODEC = createCodec(LeafLayerBlock::new);
    public static final int MAX_LAYERS = 8;
    public static final IntProperty LAYERS = Properties.LAYERS;
    private static final VoxelShape[] SHAPES_BY_LAYERS = Block.createShapeArray(8, layers -> Block.createColumnShape(16.0, 0.0, layers * 2));

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler, boolean bl) {
        if(state.get(LAYERS)>=6) {
            Vec3d vec3d = new Vec3d(0.9, 0.9F, 0.9);
            entity.slowMovement(state, vec3d);
        }
    }

    @Override
    public MapCodec<LeafLayerBlock> getCodec() {
        return CODEC;
    }

    public LeafLayerBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LAYERS, 1));
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return true;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES_BY_LAYERS[state.get(LAYERS)];
    }

    @Override
    protected VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return SHAPES_BY_LAYERS[state.get(LAYERS)];
    }

    @Override
    protected VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES_BY_LAYERS[state.get(LAYERS)];
    }

    @Override
    protected boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    protected float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return state.get(LAYERS) == 8 ? 0.2F : 1.0F;
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.down());
        if (blockState.isIn(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON)) {
            return false;
        } else {
            return blockState.isIn(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON)
                    ? true
                    : Block.isFaceFullSquare(blockState.getCollisionShape(world, pos.down()), Direction.UP) || blockState.isOf(this) && (Integer)blockState.get(LAYERS) == 8;
        }
    }

    @Override
    protected boolean canReplace(BlockState state, ItemPlacementContext context) {
        int i = (Integer)state.get(LAYERS);
        if (!context.getStack().isOf(this.asItem()) || i >= 8) {
            return false;
        } else {
            return context.canReplaceExisting() ? context.getSide() == Direction.UP : true;
        }
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (blockState.isOf(this)) {
            int i = (Integer)blockState.get(LAYERS);
            return blockState.with(LAYERS, Math.min(8, i + 1));
        } else {
            return super.getPlacementState(ctx);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }
}
