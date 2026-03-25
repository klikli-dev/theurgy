# AGENTS.md

## General
- CREATE CONVENTIONAL COMMITS FOR EACH ATOMIC CHANGE.
- CREATE A BRANCH FOR EACH FEATURE OR FIX YOU WORK ON, then create a pull request when ready.
- Read `gradle.properties` to find the current minecraft version used.

## Minecraft source lookups
- Prefer the `minecraft-dev` MCP tools for all vanilla lookups, diffs, and signature checks.
- Always use **Mojmaps** when querying Minecraft code with `minecraft-dev`.
- Use `minecraft-dev` version comparison and source lookup before editing code.

## Mapping and version rules
- Minecraft no longer uses obfuscation. If anything requests a mapping, use mojmaps.

## Build and validation
- Use the Gradle wrapper from repo root.
- Common checks:
  - `./gradlew.bat compileJava`
  - `./gradlew.bat runClient`
  - `./gradlew.bat runServer`
  - `./gradlew.bat runClientData`
  - `./gradlew.bat runGameTestServer`
- Keep changes minimal and validate edited code before finishing.

## Repo conventions
- Follow the REUSE standard for SPDX license file headers.
- Generated resources live in `src/generated/resources`; main assets/data live in `src/main/resources`.
- Several integrations are intentionally excluded in `build.gradle`; do not re-enable them.
