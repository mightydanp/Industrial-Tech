package mightydanp.techcore.common.datagen;

import mightydanp.techcore.common.material.TCMaterial;
import mightydanp.techcore.common.jsonconfig.materialflag.DefaultMaterialFlag;
import mightydanp.techcore.common.libs.Ref;
import mightydanp.techcore.common.jsonconfig.materialflag.MaterialFlagCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

/**
 * Created by MightyDanp on 10/2/2020.
 */

public class GenBlockStates extends BlockStateProvider {


    public GenBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Ref.mod_id, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //TechAscension.configSync.material.getFirst().registryMap.values().forEach(material -> materialHandlerHelper((ITMaterial) material));
    }

    private void materialHandlerHelper(TCMaterial material) {
        for(MaterialFlagCodec flag : material.materialFlags){
            if(flag == DefaultMaterialFlag.ORE.getCodec()){
                for(RegistryObject<Block> blockRegistered : material.oreList) {
                    Block oreBlock = blockRegistered.get();
                    VariantBlockStateBuilder builder = getVariantBuilder(oreBlock);
                    String modId = Objects.requireNonNull(oreBlock.getRegistryName()).toString().split(":")[0];
                    String oreName = oreBlock.getRegistryName().toString().split(":")[1];
                    String stoneVariant = oreBlock.getRegistryName().toString().split(":")[1].split("_")[0];
                    ModelFile ore = models().withExistingParent("block/ore/" + stoneVariant + "_ore", modId + ":block/ore/state/ore").texture("particle", "minecraft:block/" + stoneVariant).texture("sourceblock", "minecraft:block/" + stoneVariant);
                    simpleBlock(oreBlock , ore);

                }
            }

            if(flag == DefaultMaterialFlag.GEM.getCodec()){
                for(RegistryObject<Block> blockRegistered : material.oreList) {
                    Block block = blockRegistered.get();
                    VariantBlockStateBuilder builder = getVariantBuilder(block);
                    String modId = Objects.requireNonNull(block.getRegistryName()).toString().split(":")[0];
                    String oreName = block.getRegistryName().toString().split(":")[1];
                    String stoneVariant = block.getRegistryName().toString().split(":")[1].split("_")[0];
                    ModelFile ore = models().withExistingParent("block/gem/" + stoneVariant + "_gem", modId + ":block/ore/state/gem").texture("particle", "minecraft:block/" + stoneVariant).texture("sourceblock", "minecraft:block/" + stoneVariant);
                    simpleBlock(block , ore);
                }
            }

            if(flag == DefaultMaterialFlag.ORE.getCodec() || flag == DefaultMaterialFlag.GEM.getCodec()){
                for(RegistryObject<Block> blockRegistered : material.smallOreList) {
                    Block block = blockRegistered.get();
                    VariantBlockStateBuilder builder = getVariantBuilder(block);
                    String modId = Objects.requireNonNull(block.getRegistryName()).toString().split(":")[0];
                    String oreName = block.getRegistryName().toString().split(":")[1];
                    String stoneVariant = block.getRegistryName().toString().split(":")[1].split("_")[1];
                    ModelFile ore = models().withExistingParent("block/small_ore/" + "small_"+ stoneVariant + "_ore", modId + ":block/ore/state/small_ore").texture("particle", "minecraft:block/" + stoneVariant).texture("sourceblock", "minecraft:block/" + stoneVariant);
                    simpleBlock(block , ore);
                }

                for(RegistryObject<Block> blockRegistered : material.denseOreList) {
                    Block block = blockRegistered.get();
                    VariantBlockStateBuilder builder = getVariantBuilder(block);
                    String modId = Objects.requireNonNull(block.getRegistryName()).toString().split(":")[0];
                    String oreName = block.getRegistryName().toString().split(":")[1];
                    String stoneVariant = block.getRegistryName().toString().split(":")[1].split("_")[1];
                    ModelFile ore = models().withExistingParent("block/dense_ore/" + "dense_"+ stoneVariant + "_ore", modId + ":block/ore/state/dense_ore").texture("particle", "minecraft:block/" + stoneVariant).texture("sourceblock", "minecraft:block/" + stoneVariant);
                    simpleBlock(block , new ConfiguredModel(ore));
                }
            }

            if(flag == DefaultMaterialFlag.FLUID.getCodec() || flag == DefaultMaterialFlag.GAS.getCodec()){
                Block block = material.fluidBlock.get();

                ModelFile ore = models().withExistingParent("block/fluid/" + material.name, "block/lava").texture("particle", block.getRegistryName() + "_still");
                simpleBlock(block , new ConfiguredModel(ore));
            }
        }
    }

    /*
    private void stoneLayerHandlerHelper(StoneLayerHandler stoneLayer){
        for(StoneLayerFlagsEnum flag : stoneLayer.flags) {
            if (flag == StoneLayerFlagsEnum.thinSlab) {
                Block thinSlab = stoneLayer.thin_slab_block.get();
                VariantBlockStateBuilder builder = getVariantBuilder(thinSlab);
                String modId = thinSlab.getRegistryName().toString().split(":")[0];
                String oreName = thinSlab.getRegistryName().toString().split(":")[1];
                //String stoneVariant = thinSlab.getRegistryName().toString().split(":")[1].split("_")[1];
                ModelFile ore = models().withExistingParent("block/stone_layer/thin_slab/" + "thin_" + stoneLayer.name + "_slab", modId + ":block/stone_layer/state/thin_slab").texture("particle", "minecraft:block/" + stoneLayer.name).texture("texture", "minecraft:block/" + stoneLayer.name);

                simpleBlock(thinSlab, new ConfiguredModel(ore));
            }
        }
    }

     */

}