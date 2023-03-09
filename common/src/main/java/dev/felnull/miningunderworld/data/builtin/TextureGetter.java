package dev.felnull.miningunderworld.data.builtin;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.felnull.miningunderworld.util.MUUtils;
import dev.felnull.otyacraftengine.resources.PlatformResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.stream.IntStream;

public class TextureGetter extends PlatformResourceReloadListener<TextureGetter.Loader> {

    private static final TextureGetter INSTANCE = new TextureGetter();
    private static final ResourceLocation TEXTURE = MUUtils.modLoc("texture");
    public static byte[] crystalTexture;//加工元のテクスチャ

    public static TextureGetter getInstance() {
        return INSTANCE;
    }

    public void init() {
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, this);
    }

    @Override
    protected TextureGetter.Loader prepare(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        return new TextureGetter.Loader(resourceManager, profilerFiller);
    }

    @Override
    protected void apply(@NotNull TextureGetter.Loader loader, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return TEXTURE;
    }

    public static class Loader {

        protected Loader(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
            profilerFiller.startTick();
            resourceManager
                    .listResources("textures/block", loc -> loc.getPath().contains("crystal_base"))
                    .forEach((loc, resource) -> {
                        try {
                            crystalTexture = resource.open().readAllBytes();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}
