package mightydanp.industrialcore.common.jsonconfig.generation.orevein;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import mightydanp.industrialcore.common.handler.generation.OreGenerationHandler;
import mightydanp.industrialapi.common.jsonconfig.JsonConfigMultiFile;
import mightydanp.industrialcore.common.world.gen.feature.OreVeinGenFeatureConfig;
import mightydanp.industrialtech.common.IndustrialTech;
import net.minecraft.CrashReport;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class OreVeinRegistry extends JsonConfigMultiFile<OreVeinGenFeatureConfig> {

    @Override
    public void initiate() {
        setJsonFolderName("ore_vein");
        setJsonFolderLocation(IndustrialTech.mainJsonConfig.getFolderLocation() + "/generation/");
        buildJson();
        loadExistJson();
        super.initiate();
    }

    @Override
    public void register(OreVeinGenFeatureConfig config) {
        if (registryMap.containsKey(config.name)) {
            throw new IllegalArgumentException("ore vein with name(" + config.name + "), already exists.");
        } else {
            registryMap.put(config.name, config);
        }
    }

    public void buildJson() {
        for (OreVeinGenFeatureConfig oreVein : registryMap.values()) {
            JsonObject jsonObject = getJsonObject(oreVein.name);
            if (jsonObject.size() == 0) {
                this.saveJsonObject(oreVein.name, toJsonObject(oreVein));
            }
        }
    }

    public void loadExistJson() {
        Path path = Paths.get(this.getJsonFolderLocation() + "/" + this.getJsonFolderName());

        if (path.toFile().listFiles() != null) {
            for (final File file : Objects.requireNonNull(path.toFile().listFiles())) {
                if (file.getName().contains(".json")) {
                    JsonObject jsonObject = getJsonObject(file.getName());

                    if (!registryMap.containsValue(getFromJsonObject(jsonObject))) {
                        OreVeinGenFeatureConfig OreVein = getFromJsonObject(jsonObject);

                        registryMap.put(OreVein.name, OreVein);
                        OreGenerationHandler.addRegistryOreGeneration(OreVein);
                    } else {
                        IndustrialTech.LOGGER.fatal("[{}] could not be added to ore vein list because a ore vein already exist!!", file.getAbsolutePath());
                    }
                }
            }
        } else {
            IndustrialTech.LOGGER.warn(new CrashReport("ore vein json configs are empty [" + getJsonFolderLocation() + "/" + getJsonFolderName() + "]", new Throwable()));
        }
    }

    @Override
    public OreVeinGenFeatureConfig getFromJsonObject(JsonObject jsonObjectIn) {
        String name = jsonObjectIn.get("name").getAsString();
        int rarity = jsonObjectIn.get("rarity").getAsInt();
        int minHeight = jsonObjectIn.get("min_height").getAsInt();
        int maxHeight = jsonObjectIn.get("max_height").getAsInt();
        int minRadius = jsonObjectIn.get("min_radius").getAsInt();
        int minNumberOfSmallOreLayers = jsonObjectIn.get("min_number_of_small_ore_layers").getAsInt();

        JsonArray dimensionsJson = jsonObjectIn.getAsJsonArray("dimensions");
        List<String> dimensionsList = new ArrayList<>();
        dimensionsJson.forEach((jsonElement) -> {
            String dimension = jsonElement.getAsString();
            dimensionsList.add(dimension);
        });

        JsonArray validBiomesJson = jsonObjectIn.getAsJsonArray("valid_biomes");
        List<String> validBiomesList = new ArrayList<>();
        validBiomesJson.forEach((jsonElement) -> {
            String biome = jsonElement.getAsString();
            validBiomesList.add(biome);
        });

        JsonArray invalidBiomesJson = jsonObjectIn.getAsJsonArray("invalid_biomes");
        List<String> invalidBiomesList = new ArrayList<>();
        invalidBiomesJson.forEach((jsonElement) -> {
            String biome = jsonElement.getAsString();
            invalidBiomesList.add(biome);
        });


        JsonArray veinBlocksJson = jsonObjectIn.getAsJsonArray("vein_blocks_and_chances");
        List<Pair<String, Integer>> veinBlockChances = new ArrayList<>();

        veinBlocksJson.forEach((jsonElement) -> {
            JsonObject object = jsonElement.getAsJsonObject();
            veinBlockChances.add(new Pair<>(object.get("vein_block").getAsString(), object.get("vein_block_chance").getAsInt()));
        });

        return new OreVeinGenFeatureConfig(name, rarity, minHeight, maxHeight, minRadius, minNumberOfSmallOreLayers, dimensionsList, validBiomesList, invalidBiomesList, veinBlockChances);
    }

    public JsonObject toJsonObject(OreVeinGenFeatureConfig config) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", config.name);
        jsonObject.addProperty("rarity", config.rarity);
        jsonObject.addProperty("min_height", config.minHeight);
        jsonObject.addProperty("max_height", config.maxHeight);
        jsonObject.addProperty("min_radius", config.minRadius);
        jsonObject.addProperty("min_number_of_small_ore_layers", config.minNumberOfSmallOreLayers);

        JsonArray dimensions = new JsonArray();
        {
            config.dimensions.forEach(dimensions::add);
        }
        jsonObject.add("dimensions", dimensions);

        JsonArray validBiomes = new JsonArray();
        {
            config.validBiomes.forEach(validBiomes::add);
        }
        jsonObject.add("valid_biomes", validBiomes);

        JsonArray invalid_biomes = new JsonArray();
        {
            config.invalidBiomes.forEach(invalid_biomes::add);
        }
        jsonObject.add("invalid_biomes", invalid_biomes);

        JsonArray veinBlocks = new JsonArray();
        {
            JsonObject veinBlocksArray = new JsonObject();
            {
                for (int i = 0; i < config.blocksAndChances.size(); i++) {
                    veinBlocksArray.addProperty("vein_block", config.blocksAndChances.get(i).getFirst());
                    veinBlocksArray.addProperty("vein_block_chance", config.blocksAndChances.get(i).getSecond());
                }
                veinBlocks.add(veinBlocksArray);
            }
        }
        jsonObject.add("vein_blocks_and_chances", veinBlocks);

        return jsonObject;
    }
}