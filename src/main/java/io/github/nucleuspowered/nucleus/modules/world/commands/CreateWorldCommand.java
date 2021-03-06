/*
 * This file is part of Nucleus, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleus.modules.world.commands;

import io.github.nucleuspowered.nucleus.Util;
import io.github.nucleuspowered.nucleus.argumentparsers.DifficultyArgument;
import io.github.nucleuspowered.nucleus.internal.annotations.Permissions;
import io.github.nucleuspowered.nucleus.internal.annotations.RegisterCommand;
import io.github.nucleuspowered.nucleus.internal.command.CommandBase;
import io.github.nucleuspowered.nucleus.internal.permissions.SuggestedLevel;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.GeneratorType;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldCreationSettings;
import org.spongepowered.api.world.difficulty.Difficulty;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

/**
 * Creates world.
 *
 * Command Usage: /world create [name] [dimension] [generator] [gamemode]
 * [difficulty] Permission: nucleus.world.create.base
 */
@Permissions(root = "world", suggestedLevel = SuggestedLevel.ADMIN)
@RegisterCommand(value = {"create"}, subcommandOf = WorldCommand.class)
public class CreateWorldCommand extends CommandBase<CommandSource> {

    private final String name = "name";
    private final String dimension = "dimension";
    private final String generator = "generator";
    private final String gamemode = "gamemode";
    private final String difficulty = "difficulty";

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[] {GenericArguments.onlyOne(GenericArguments.string(Text.of(name))),
                GenericArguments.onlyOne(GenericArguments.catalogedElement(Text.of(dimension), CatalogTypes.DIMENSION_TYPE)),
                GenericArguments.onlyOne(GenericArguments.catalogedElement(Text.of(generator), CatalogTypes.GENERATOR_TYPE)),
                GenericArguments.onlyOne(GenericArguments.catalogedElement(Text.of(gamemode), CatalogTypes.GAME_MODE)),
                GenericArguments.onlyOne(new DifficultyArgument(Text.of(difficulty)))};
    }

    @Override
    public CommandResult executeCommand(final CommandSource src, CommandContext args) throws Exception {
        String nameInput = args.<String>getOne(name).get();
        DimensionType dimensionInput = args.<DimensionType>getOne(dimension).get();
        GeneratorType generatorInput = args.<GeneratorType>getOne(generator).get();
        GameMode gamemodeInput = args.<GameMode>getOne(gamemode).get();
        Difficulty difficultyInput = args.<Difficulty>getOne(difficulty).get();

        src.sendMessage(Util.getTextMessageWithFormat("command.world.create.begin", nameInput));

        WorldCreationSettings worldSettings = Sponge.getRegistry().createBuilder(WorldCreationSettings.Builder.class).name(nameInput).enabled(true)
                .loadsOnStartup(true).keepsSpawnLoaded(true).dimension(dimensionInput).generator(generatorInput).gameMode(gamemodeInput).build();

        Optional<WorldProperties> worldProperties = Sponge.getGame().getServer().createWorldProperties(worldSettings);

        if (worldProperties.isPresent()) {
            Optional<World> world = Sponge.getGame().getServer().loadWorld(worldProperties.get());

            if (world.isPresent()) {
                world.get().getProperties().setDifficulty(difficultyInput);
                src.sendMessage(Util.getTextMessageWithFormat("command.world.create.success", nameInput));
            } else {
                src.sendMessage(Util.getTextMessageWithFormat("command.world.create.fail", nameInput));
            }
        } else {
            src.sendMessage(Util.getTextMessageWithFormat("command.world.create.fail", nameInput));
        }

        return CommandResult.success();
    }
}
