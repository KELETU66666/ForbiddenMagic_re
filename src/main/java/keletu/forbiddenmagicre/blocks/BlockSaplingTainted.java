package keletu.forbiddenmagicre.blocks;

import keletu.forbiddenmagicre.forbiddenmagicre;
import keletu.forbiddenmagicre.init.ModBlocks;
import keletu.forbiddenmagicre.init.ModItems;
import keletu.forbiddenmagicre.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.common.lib.SoundsTC;

import java.util.Random;

import static fox.spiteful.lostmagic.LostMagic.tab;

public class BlockSaplingTainted extends BlockBush implements IGrowable, IHasModel {

    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);

    protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D,
            0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    public BlockSaplingTainted() {
        super();
        setRegistryName("sapling_tainted");
        setUnlocalizedName("sapling_tainted");
        setCreativeTab(tab);
        setDefaultState(this.blockState.getBaseState().withProperty(STAGE, Integer.valueOf(0)));
        setHardness(0.0F);

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public SoundType getSoundType() {
        return SoundsTC.GORE;
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        IStateMapper custom_mapper = (new StateMap.Builder()).ignore(new IProperty[] { STAGE }).build();
        ModelLoader.setCustomStateMapper(this, custom_mapper);
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
                                            float hitZ, int meta, EntityLivingBase placer) {
        return this.getStateFromMeta(meta).withProperty(STAGE, 0);
    }

    @Override
    public Material getMaterial(IBlockState state) {
        return ThaumcraftMaterials.MATERIAL_TAINT;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return SAPLING_AABB;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            super.updateTick(worldIn, pos, state, rand);

            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
                this.grow(worldIn, rand, pos, state);
            }
        }
    }

    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) {
            return;
        }
        WorldGenerator worldgenerator = new WorldGenTaintedTree(true);
        int i = 0;
        int j = 0;

        IBlockState iblockstate2 = Blocks.AIR.getDefaultState();

        worldIn.setBlockState(pos, iblockstate2, 4);

        if (!worldgenerator.generate(worldIn, rand, pos.add(i, 0, j))) {
            worldIn.setBlockState(pos, state, 4);
        }
    }

    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return (double) worldIn.rand.nextFloat() < 0.45D;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        if (state.getValue(STAGE) == 0) {
            worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
        } else {
            this.generateTree(worldIn, pos, state, rand);
        }
    }

    @Override
    public net.minecraftforge.common.EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world, BlockPos pos) {
        return net.minecraftforge.common.EnumPlantType.Plains;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(STAGE, meta);
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(STAGE);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { STAGE });
    }

    @Override
    public void registerModels() {
        forbiddenmagicre.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}