---
name: minecraft-neo-dev
description: Guidance for Minecraft mod development with NeoForge. Integrates the minecraft-dev-mcp server for decompilation, source analysis, and mixin validation. Use when developing NeoForge mods or working with Minecraft source code.
---

# Minecraft NeoForge Mod Development Skill

## Overview

This skill provides guidance for Minecraft mod development with NeoForge. It focuses on using the core `minecraft-dev-mcp` server to provide tooling for source code access, decompilation, and analysis using official Mojang mappings.

## Available MCP Server

### minecraft-dev-mcp

Core Minecraft development tools for source code access, decompilation, and analysis.

---

## Core Workflows

### Initial Setup Workflow

**When starting ANY Minecraft development task:**

1. **Identify target version:**
```
list_minecraft_versions → See available/cached Minecraft versions

```


2. **Decompile target version (if needed):**
```
decompile_minecraft_version (version, mapping: "mojmap", force: false)

```



### Understanding Mappings

**Mapping Types (in priority order for NeoForge):**

* **mojmap** - Official Mojang names (STANDARD for NeoForge)
* **official** - Obfuscated names (a, b, c, etc.) or de-obfuscated in newer versions

**When to use each:**

* Development: Use **mojmap** (Matches NeoForge standard)
* Reading vanilla code: Use **mojmap**
* Deobfuscating: From **official** to **mojmap**

**Future-Proofing Note:**
Starting with experimental snapshots after 1.21.11, Minecraft is releasing de-obfuscated builds.

* Official releases will be immediately readable.
* **mojmap** remains the standard argument for consistency in tools.
* Legacy versions will still require deobfuscation.

**Current State (1.21.11 and earlier):** Official = obfuscated, use mojmap for development.
**Future State (experimental snapshots+):** Official = de-obfuscated, still use mojmap logic for consistency.

---

## De-Obfuscated Minecraft Releases

### The Transition

Starting with experimental snapshots after version 1.21.11, Minecraft is releasing **de-obfuscated builds**.

**What This Means:**

* Official Minecraft JARs will ship with human-readable class/method/field names.
* Reduced need for deobfuscation tools for newer versions.

### Impact on Development Workflow

**For New Versions (De-obfuscated):**

* Official mappings will be immediately useful.
* **mojmap** is still valuable for consistent naming conventions and tool compatibility.

**For Legacy Versions (1.21.11 and earlier):**

* Still require traditional deobfuscation.
* Use **mojmap** as primary mappings.

### MCP Server Compatibility

**The minecraft-dev-mcp server is already future-proof:**

* Handles both obfuscated and de-obfuscated official mappings.
* Automatically detects mapping format.
* Tools work identically regardless of obfuscation state.

### Recommended Practices

**For Maximum Compatibility:**

```
# Use mojmap as primary for NeoForge development
decompile_minecraft_version (version: "1.21.11", mapping: "mojmap")
decompile_minecraft_version (version: "1.22-experimental", mapping: "mojmap")

# Both work identically, server handles the difference

```

### Version Detection

**The tools automatically handle version type:**

```
# Works for both obfuscated and de-obfuscated
get_minecraft_source (version, className, mapping: "mojmap")

```

**You never need to specify obfuscation state manually.**

---

## Common Development Tasks

### 1. Understanding Minecraft Source Code

**Workflow:**

```
Step 1: Find the class
  → search_minecraft_code (version, query: "Entity", searchType: "class", mapping: "mojmap")

Step 2: Get source code
  → get_minecraft_source (version, className: "net.minecraft.world.entity.Entity", mapping: "mojmap")

Step 3: Get documentation context
  → get_documentation (className: "Entity")

```

**Best practices:**

* ALWAYS use **mojmap** mappings for NeoForge development.
* Check Minecraft source directly to understand vanilla logic.
* Look for related classes using search results.

### 2. Creating Mixins

**Essential workflow:**

```
Step 1: Understand the target
  → get_minecraft_source (version, className: "[target]", mapping: "mojmap")

Step 2: Write mixin code
  [User writes mixin based on source]

Step 3: Validate mixin
  → analyze_mixin (source: "[mixin code]", mcVersion: "[version]", mapping: "mojmap")

Step 4: Fix issues
  [Review validation results, make corrections]
  → analyze_mixin (source: "[updated code]", mcVersion: "[version]", mapping: "mojmap")

```

**Critical rules:**

* Mixins MUST use **mojmap** mappings for NeoForge.
* Validate EVERY mixin before suggesting it's complete.
* Check injection points exist in target class.
* Verify method signatures match exactly.

**When validation fails:**

* Check if target class/method exists in that version.
* Verify mapping is correct (mojmap).
* Look for renamed methods between versions.

### 3. Analyzing Existing Mods

**For understanding:**

```
Step 1: Extract mod metadata
  → analyze_mod_jar (jarPath: "[path]", includeAllClasses: false, includeRawMetadata: true)

Step 2: Examine mixins (if present)
  → analyze_mixin (source: "[jar path]", mcVersion: "[version]", mapping: "mojmap")

Step 3: Decompile/remap if needed
  → remap_mod_jar (inputJar, outputJar, mcVersion, toMapping: "mojmap")

```

### 4. Version Migration & Porting

**Comparing Minecraft versions:**

```
Step 1: Get high-level overview
  → compare_versions (fromVersion, toVersion, mapping: "mojmap", category: "all")

Step 2: Detailed API changes
  → compare_versions_detailed (fromVersion, toVersion, mapping: "mojmap", 
     packages: ["net.minecraft.world.entity", "net.minecraft.world.level"], maxClasses: 500)

Step 3: Check registry changes
  → get_registry_data (version: "[old]", registry: "blocks")
  → get_registry_data (version: "[new]", registry: "blocks")

```

**Breaking changes checklist:**

* Class renames/moves.
* Method signature changes.
* Field type changes.
* Registry ID changes.

---

## Advanced Workflows

### Large-Scale Code Search

**When you need to find patterns across entire codebase:**

```
Step 1: Index the version (one-time, enables fast search)
  → index_minecraft_version (version, mapping: "mojmap")

Step 2: Fast full-text search
  → search_indexed (query: "entity damage", version, mapping: "mojmap", 
     types: ["method", "field"], limit: 100)

Alternative: Direct search (slower, no index needed)
  → search_minecraft_code (version, query: "damage", searchType: "all", 
     mapping: "mojmap", limit: 50)

```

### Finding Mappings

**Use case: You have an obfuscated name and need the mojmap name (legacy versions):**

```
find_mapping (symbol: "a", version: "1.21.11", sourceMapping: "official", targetMapping: "mojmap")

```

**Use case: De-obfuscated releases (1.22+ experimental):**

```
# Official names are now readable, but still verify against mojmap
find_mapping (symbol: "Entity", version: "1.22", sourceMapping: "official", targetMapping: "mojmap")

```

### Registry Data Analysis

**For blocks, items, entities, etc.:**

```
# Get all registries
get_registry_data (version, registry: undefined)

# Get specific registry
get_registry_data (version, registry: "blocks")
get_registry_data (version, registry: "items")

```

---

## Documentation Strategy

### When to Search Documentation

**Minecraft Source - Priority 1:**

```
# When you need to see how vanilla does it
get_minecraft_source (version, className: "BlockItem", mapping: "mojmap")
search_minecraft_code (version, query: "registerBlock", searchType: "method", mapping: "mojmap")

```

**External Documentation:**
Since there is no specific MCP for NeoForge documentation, rely on the official NeoForge website or GitHub wiki for API specifics, while using `minecraft-dev-mcp` for vanilla logic analysis.

---

## Error Handling & Troubleshooting

### Common Issues

**"Decompiled source not found"**
→ Run `decompile_minecraft_version` first.

**"Mixin validation failed"**
→ Check target class exists: `get_minecraft_source`.
→ Verify method signatures match exactly.
→ Ensure using **mojmap** mappings.

**"Invalid mapping type"**
→ NeoForge uses **mojmap**.
→ Check available mappings with `find_mapping`.

### Validation Best Practices

**ALWAYS validate before suggesting code is complete:**

* Mixins: `analyze_mixin`
* Version compatibility: `compare_versions`
* Registry IDs: `get_registry_data`

---

## Tool Selection Decision Tree

```
User wants to...

├─ Understand Minecraft code
│  ├─ Find a class → search_minecraft_code (searchType: "class")
│  ├─ See implementation → get_minecraft_source
│  └─ Find all usages → search_indexed (after indexing)
│
├─ Write a mixin
│  ├─ Get target class → get_minecraft_source
│  └─ Validate mixin → analyze_mixin
│
├─ Analyze a mod
│  ├─ Analyze jar → analyze_mod_jar
│  └─ Validate code → analyze_mixin
│
├─ Update for new version
│  ├─ High-level changes → compare_versions
│  ├─ Detailed API diff → compare_versions_detailed
│  └─ Registry changes → get_registry_data (both versions)
│
└─ Understand mappings
   ├─ Translate obfuscated → find_mapping
   ├─ See all mappings → List available in tool
   └─ Convert between systems → find_mapping


```

---

## Output Formatting for Users

### When Providing Mixin Code

**ALWAYS include:**

1. Full mixin class with imports
2. Validation command they should run
3. Explanation of what it does
4. Warning about testing

**Template:**

```java
// Your mixin code here

```

**Validation:**

```
analyze_mixin (source: "[above code]", mcVersion: "1.21.11", mapping: "mojmap")

```

### When Showing Version Differences

**Provide:**

1. Summary of changes
2. Breaking changes highlighted
3. Recommended actions

---

## Performance Considerations

### Indexing Strategy

**When to index:**

* User will do extensive searching.
* Working on large-scale refactoring.

**Index command:**

```
index_minecraft_version (version, mapping: "mojmap")

```

### Caching Awareness

**These are cached (fast after first run):**

* `decompile_minecraft_version`
* `get_minecraft_source`
* `get_registry_data`

**These are not cached (always fresh):**

* `search_minecraft_code` (unless indexed)
* `analyze_mixin`
* `compare_versions`

---

## Version Compatibility Matrix

### Minecraft Version Support

**Check available versions:**

```
list_minecraft_versions → See what's available

```

### Mapping Version Notes

* **mojmap**: Version-specific, official Mojang names (STANDARD for NeoForge).
* **official**:
* **Legacy (≤1.21.11)**: Raw obfuscated (a, b, c).
* **Modern (1.22+ experimental)**: De-obfuscated, human-readable names.



### Version Categories

**Obfuscated Era (≤1.21.11):**

* Requires deobfuscation for development.
* Official mappings are obfuscated.
* **mojmap** essential for readability.

**De-obfuscated Era (1.22+ experimental snapshots):**

* Official releases are human-readable.
* **mojmap** still recommended for tool consistency.

### Cross-Version Development

**When working across obfuscated/de-obfuscated boundary:**

```
# Use mojmap for consistent names across versions
decompile_minecraft_version (version: "1.21.11", mapping: "mojmap")
decompile_minecraft_version (version: "1.22-experimental", mapping: "mojmap")

# Compare versions safely
compare_versions_detailed (
  fromVersion: "1.21.11",    # Obfuscated
  toVersion: "1.22",          # De-obfuscated
  mapping: "mojmap"           # Consistent across both
)

```

**Best Practice:** Always use mojmap for NeoForge development regardless of target version's obfuscation state.

---

## Critical Reminders

### ALWAYS Remember

1. **NeoForge = mojmap mappings** - *standard across all versions*
2. **Validate before declaring complete** (mixins)
3. **Use appropriate search scope** (indexed vs direct)
4. **Provide validation commands** (so user can verify)
5. **Normalize paths** (tools handle WSL/Windows automatically)
6. **MCP server is future-proof** (handles both obfuscated and de-obfuscated releases automatically)

### NEVER Do

1. Don't suggest Yarn for NeoForge development.
2. Don't skip mixin validation.
3. Don't assume classes exist without checking.
4. Don't provide partial validation steps.
5. Don't worry about obfuscation state - the server handles it.

### De-Obfuscated Era Awareness

**When user mentions version 1.22+ or experimental snapshots:**

* Note that these may be de-obfuscated releases.
* Recommend **mojmap** anyway for consistency.
* No workflow changes needed - tools auto-detect format.

---

## Resource URIs

### Minecraft Source Resources

**Pattern:** `minecraft://source/{version}/{mapping}/{className}`

**Example:**

```
minecraft://source/1.21.1/mojmap/net.minecraft.world.entity.Entity

```

**Also available:**

* `minecraft://mappings/{version}/{mapping}`
* `minecraft://registry/{version}/{registryType}`
* `minecraft://versions/list`
* `minecraft://index/{version}/{mapping}`

---

## Final Checklist for Mod Development

Before suggesting a solution is complete, verify:

* [ ] Target Minecraft version confirmed
* [ ] Correct mapping type used (mojmap for NeoForge)
* [ ] Source code examined for vanilla implementation
* [ ] Mixin validated with analyze_mixin
* [ ] Version compatibility checked
* [ ] Testing instructions provided

---

## Quick Reference Commands

**Initial Setup:**

```bash
list_minecraft_versions
decompile_minecraft_version (version: "1.21.11", mapping: "mojmap")

```

**Development:**

```bash
get_minecraft_source (version, className, mapping: "mojmap")
analyze_mixin (source, mcVersion, mapping: "mojmap")

```

**Analysis:**

```bash
analyze_mod_jar (jarPath)
compare_versions (fromVersion, toVersion, mapping: "mojmap")
search_minecraft_code (version, query, searchType: "all", mapping: "mojmap")

```

This skill provides coverage of Minecraft NeoForge mod development workflows using the core MCP tools.