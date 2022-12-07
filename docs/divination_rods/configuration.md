---
sidebar_position: 10
---

# Configuration

Divination rods allow players to find ores and other blocks in the world.

## Result NBT Settings

The configuration of how exaclty a divination rod behaves is done in the divination rod NBT tag, which means that the configuration can be provided in the crafting recipe by supplying NBT for the result item:

```json
"result": {
  "nbt": {
    ...
  }
}
```

* `theurgy:divination.setting.tier` (Tier ResourceLocation)

The tier of the divination rod. This uses the forge tier system, which is an expanded version of the minecraft tier system.  

The tier determines which blocks the rod can attune to and find: only if the block is in the same tier or lower than the rod, it can be attuned. 
This means it works exactly like tiers for tools such as the pickaxe. 

* `theurgy:divination.setting.allowed_blocks_tag` (Tag ResourceLocation)

A tag that defines which blocks the divination rod is allowed to attune to and find. Any blocks not matching this tag will be ignored.

* `theurgy:divination.setting.range` (Integer)

The range of the divination rod in blocks. This is the maximum distance the divination rod will scan for blocks. Too high values may cause performance issues on the client while using the rod.

Suggested Value: `96`

* `theurgy:divination.setting.duration` (Integer)

The duration of the divination "charging" effect in ticks. This is the time the divination rod has to scan for blocks (client-side). Too low values may cause performance issues on the client while using the rod.

Suggested Value: `40`

* `theurgy:divination.setting.max_damage` (Integer)

The durability of the divination rod. Only scans will consume durability, just visualising the scan results will not. For balancing reasons this should likely be a low value, such as `10`.

### More info on Tier and Allowed Blocks Tag

A divination rod can only be attuned to a shift-clicked block, if both the Tier of the rod is sufficient (higher than or equal to block tier) and the block is in the allowed blocks tag. 

The default theurgy allowed blocks tags look like this:

```json 
{
  "replace": false,
  "values": [
    "#forge:ores",
    "#forge:sand",
    "#forge:stone",
    "#minecraft:logs",
    "minecraft:sugar_cane"
  ]
}
```

If this is combined with the Tier `minecraft:stone`, as in the example below, it is a classic use case to cover some useful vanilla resources, as well as all ores up to iron. 

Depending on your use case you can make the rod more or less versatile by changing the tier and/or adding/removing blocks from the allowed blocks tag.

The default allowed blocks tags can be found in `/resources/data/theurgy/tags/blocks/` and are named `divination_rod_[t1/t2/t3/t4]_allowed_blocks.json` based on their tier, e.g. `divination_rod_t4_allowed_blocks.json` 


## Pre-Attuned Divination Rods

See [Crafting Recipes](./crafting_recipes.md) for more information on how to create pre-attuned divination rods.

## Sample Recipe 

```json
{
  "type": "theurgy:divination_rod",
  "key": {
    "G": {
      "tag": "forge:gems/emerald"
    },
    "R": {
      "tag": "forge:rods/wooden"
    },
    "S": {
      "type": "forge:partial_nbt",
      "item": "theurgy:alchemical_sulfur",
      "nbt": {
        "theurgy:sulfur.source.id": "minecraft:diamond"
      }
    }
  },
  "pattern": [
    "RGR",
    "RSR",
    " R "
  ],
  "result": {
    "item": "theurgy:divination_rod_t4",
    "nbt": {
      "theurgy:divination.setting.tier": "minecraft:stone",
      "theurgy:divination.setting.allowed_blocks_tag": "theurgy:divination_rod_t4_allowed_blocks",
      "theurgy:divination.setting.range": 96,
      "theurgy:divination.setting.duration": 40,
      "theurgy:divination.setting.max_damage": 10
    }
  }
}
```