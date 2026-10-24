# Zenworks

A growing collection of ZenScript utility methods for CraftTweaker, for Minecraft 1.12.2.

I add a method when one of my own scripts needs it, so the methods are a mixed set and do not follow one theme. Other script authors may find them useful too.

Zenworks adds methods to CraftTweaker's entity types. It adds no items, blocks, recipes, commands, or configuration, and changes nothing in the game unless a script calls one of its methods.

- Player-facing description: [MOD-PAGE.md](MOD-PAGE.md)
- Changes by release: [CHANGELOG.md](CHANGELOG.md)
- Method catalog: [docs/methods/index.md](docs/methods/index.md), with exact signatures, examples, and the CraftTweaker way for needs CraftTweaker already meets
- For coding agents that write scripts: [docs/agent-guide.md](docs/agent-guide.md)

## Identity and requirements

- Mod ID: `zenworks`
- Requires CraftTweaker `4.1.20` or newer (declared as `required-after:crafttweaker@[4.1.20,)`; developed and tested against `4.1.20.715`).
- Install on both the client and the server.
- Runs on Forge and on Cleanroom.

## Scope

Zenworks holds focused utilities that answer a concrete scripting need, each with a recorded check for an existing equivalent in CraftTweaker. Status, stack, mark, and cooldown systems, and equipment sets, are out of scope.

The catalog under `docs/methods/` is the single source of the examples: `gradlew exampleScript` writes them to `run/scripts/zenworks-examples.zs` for the development runtime (it also runs before `runClient` and `runServer`), where right-clicking an entity with a stick runs every example. `CatalogIndexTest` keeps the index and the entries in step.

## License

MIT, see [LICENSE](LICENSE). The build files come from the CleanroomMC ForgeDevEnv template; see [THIRD-PARTY-NOTICES.md](THIRD-PARTY-NOTICES.md).
