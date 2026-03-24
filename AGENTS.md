# AGENTS.md

## General
- Read `gradle.properties` to find the current minecraft version used.
- Create a commit for every atomic change you make.

## Minecraft source lookups
- Prefer the `minecraft-dev` MCP tools for all vanilla lookups, diffs, and signature checks.
- Always use **Mojmaps** when querying Minecraft code with `minecraft-dev`.
- Use `minecraft-dev` version comparison and source lookup before editing code.

## Mapping and version rules
- For Minecraft versions **above `1.21.11`**, Minecraft source code is distributed without obfuscation, so there are no mapping names. Use the official Mojmap names for code, access transformers, and documentation.
- After `1.21.11`, Minecraft also moved to the new versioning scheme beginning with **`26.1`**. 
- 
## Build and validation
- Use the Gradle wrapper from repo root.
- Common checks:
  - `./gradlew.bat compileJava`
  - `./gradlew.bat runClient`
  - `./gradlew.bat runServer`
  - `./gradlew.bat runClientData`
- Keep changes minimal and validate edited code before finishing.

## Repo conventions
- Follow the REUSE standard for SPDX license file headers.
- Generated resources live in `src/generated/resources`; main assets/data live in `src/main/resources`.
- Several integrations are intentionally excluded in `build.gradle`; do not re-enable them.

## GIT Worflow
- Do not create branches, use only the current checked out branch for all work.
- Use conventional commit messages, and keep commits focused on a single change or fix.
- Commit early and commit often.
