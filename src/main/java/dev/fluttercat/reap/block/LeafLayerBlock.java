package dev.fluttercat.reap.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class LeafLayerBlock extends Block {
    public static final MapCodec<LeafLayerBlock> CODEC = simpleCodec(LeafLayerBlock::new);
    public static final int MAX_LAYERS = 8;
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
    private static final VoxelShape[] SHAPES_BY_LAYERS = Block.boxes(8, layers -> Block.column(16.0, 0.0, layers * 2));

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier handler, boolean bl) {
        if(state.getValue(LAYERS)>=6) {
            Vec3 vec3d = new Vec3(0.9, 0.9F, 0.9);
            entity.makeStuckInBlock(state, vec3d);
        }
    }

    @Override
    public MapCodec<LeafLayerBlock> codec() {
        return CODEC;
    }

    public LeafLayerBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, 1));
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return true;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES_BY_LAYERS[state.getValue(LAYERS)];
    }

    @Override
    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
        return SHAPES_BY_LAYERS[state.getValue(LAYERS)];
    }

    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES_BY_LAYERS[state.getValue(LAYERS)];
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue(LAYERS) == 8 ? 0.2F : 1.0F;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.below());
        if (blockState.is(BlockTags.CANNOT_SUPPORT_SNOW_LAYER)) {
            return false;
        } else {
            return blockState.is(BlockTags.SUPPORT_OVERRIDE_SNOW_LAYER)
                    ? true
                    : Block.isFaceFull(blockState.getCollisionShape(world, pos.below()), Direction.UP) || blockState.is(this) && (Integer)blockState.getValue(LAYERS) == 8;
        }
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        int i = (Integer)state.getValue(LAYERS);
        if (!context.getItemInHand().is(this.asItem()) || i >= 8) {
            return false;
        } else {
            return context.replacingClickedOnBlock() ? context.getClickedFace() == Direction.UP : true;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (blockState.is(this)) {
            int i = (Integer)blockState.getValue(LAYERS);
            return blockState.setValue(LAYERS, Math.min(8, i + 1));
        } else {
            return super.getStateForPlacement(ctx);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }
}
