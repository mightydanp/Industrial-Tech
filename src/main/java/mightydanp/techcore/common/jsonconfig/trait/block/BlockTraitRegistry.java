package mightydanp.techcore.common.jsonconfig.trait.block;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import mightydanp.techapi.common.jsonconfig.JsonConfigMultiFile;
import mightydanp.techascension.common.TechAscension;
import net.minecraft.CrashReport;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BlockTraitRegistry extends JsonConfigMultiFile<BlockTraitCodec> {

    @Override
    public void initiate() {
        setJsonFolderName("trait/block");
        setJsonFolderLocation(TechAscension.mainJsonConfig.getFolderLocation());

        buildJson();
        loadExistingJsons();
        super.initiate();
    }

    @Override
    public void register(BlockTraitCodec codec) {
        String registry = codec.registry().split(":")[1];
        if (registryMap.containsKey(registry))
            throw new IllegalArgumentException(BlockTraitCodec.codecName + " for registry block(" + registry + "), already exists.");
        registryMap.put(registry, codec);
    }

    public BlockTraitCodec getBlockTraitByName(String name) {
        return registryMap.get(name);
    }

    public Set<BlockTraitCodec> getAllBlockTrait() {
        return new HashSet<>(registryMap.values());
    }

    public void buildJson(){
        for(BlockTraitCodec codec : registryMap.values()) {
            String registry = codec.registry().split(":")[1];
            JsonObject jsonObject = getJsonObject(registry);

            if (jsonObject.size() == 0) {
                this.saveJsonObject(registry, toJsonObject(codec));
            }
        }
    }

    public void loadExistingJsons(){
        Path path = Paths.get(this.getJsonFolderLocation() + "/" + this.getJsonFolderName());

        if(path.toFile().listFiles() != null) {
            for (final File file : Objects.requireNonNull(path.toFile().listFiles())) {
                if (file.getName().contains(".json")) {
                    JsonObject jsonObject = getJsonObject(file.getName());

                    if (!registryMap.containsValue(fromJsonObject(jsonObject))) {
                        BlockTraitCodec codec = fromJsonObject(jsonObject);
                        String registry = codec.registry().split(":")[1];

                        registryMap.put(registry, codec);
                    } else {
                        TechAscension.LOGGER.fatal("[{}] could not be added to " + BlockTraitCodec.codecName + " because a " + BlockTraitCodec.codecName + " already exist!!", file.getAbsolutePath());
                    }
                }
            }
        } else {
            TechAscension.LOGGER.warn(new CrashReport(BlockTraitCodec.codecName + " json configs are empty [" + getJsonFolderLocation() + "/" + getJsonFolderName() + "]", new Throwable()));
        }
    }

    @Override
    public BlockTraitCodec fromJsonObject(JsonObject jsonObjectIn){
        return BlockTraitCodec.CODEC.decode(JsonOps.INSTANCE, jsonObjectIn).getOrThrow(false,(a) -> TechAscension.LOGGER.throwing(new Error("There is something wrong with one of your " + BlockTraitCodec.codecName + ", please fix this"))).getFirst();
    }

    public JsonObject toJsonObject(BlockTraitCodec codec) {
        return BlockTraitCodec.CODEC.encodeStart(JsonOps.INSTANCE, codec).get().left().orElseThrow(() -> TechAscension.LOGGER.throwing(new Error("There is something wrong with one of your " + BlockTraitCodec.codecName + ", please fix this"))).getAsJsonObject();
    }
}