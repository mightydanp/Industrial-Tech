package mightydanp.industrialtech.api.common.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

/**
 * Created by MightyDanp on 3/3/2021.
 */
public class RandomlyOnSurfaceGenFeature extends Feature<RandomlyOnSurfaceGenFeatureConfig> {
    public RandomlyOnSurfaceGenFeature(Codec<RandomlyOnSurfaceGenFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(ISeedReader iSeedReaderIn, ChunkGenerator chunkGeneratorIn, Random randomIn, BlockPos blockPosIn, RandomlyOnSurfaceGenFeatureConfig randomlyOnSurfaceGenFeatureConfigIn) {
        boolean canSpawn = false;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        int groundHeight = chunkGeneratorIn.getSpawnHeight();
        int x = blockPosIn.getX();
        int z = blockPosIn.getZ();
        for (int xx = 0; xx <= 16; xx++) {
            int x2 = xx + x;
            for (int zz = 0; zz <= 16; zz++) {
                int z2 = zz + z;
                for (int yy = 0; yy <= 180; yy++) {
                    blockpos$mutable.set(x2, yy, z2);
                    BlockState blockState = iSeedReaderIn.getBlockState(blockpos$mutable);
                    BlockState blockStateDown = iSeedReaderIn.getBlockState(blockpos$mutable.below());
                    //BlockState blockThatCanBePlace = canReplaceStone(randomlyOnSurfaceGenFeatureConfigIn, blockStateDown);
                    if (randomIn.nextInt(5000) < randomlyOnSurfaceGenFeatureConfigIn.rarity) {
                        if(randomlyOnSurfaceGenFeatureConfigIn.soilBlocks.contains(blockStateDown) && blockState == Blocks.AIR.defaultBlockState()){
                            iSeedReaderIn.setBlock(blockpos$mutable, randomlyOnSurfaceGenFeatureConfigIn.blocks, 2);
                            canSpawn = true;
                            //System.out.println(blockpos$mutable.getX() + " " + blockpos$mutable.getY() + " " + blockpos$mutable.getZ() + " " + "/" + randomlyOnSurfaceGenFeatureConfigIn.blocks.getBlock().toString().split(":")[1]);
                        }
                    }
                }
            }
        }
        return canSpawn;
    }
}
