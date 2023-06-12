---
sidebar_position: 20
---

# Crafting Recipes

Divination rods that are inteded to be attuned manually by shift-clicking the target block can use a regular minecraft recipe (e.g. `minecraft:shaped`).

For balancing reasons Theurgy also offers pre-attuned divination rods that can only find the block they have been crafted for. This allows to provide fine-tuned balancing for more expensive ores (or other blocks) to find.

## Result NBT Settings

The following NBT settings can be set on the result item of the recipe (see also below for a full example):

```json
"result": {
  "nbt": {
    ...
  }
}
```

### `theurgy:divination.setting.allow_attuning` (Boolean)

Setting this to `false` is what disables manual attuning. See below for usage.

### `theurgy:divination.linked_block_id` (Block ResourceLocation)

Set this to the block ID of the block you want the divination rod to find. 
E.g.: `minecraft:iron_ore` or even `minecraft:dirt`.

## Sulfur Source ID

Instead of manually setting the `theurgy:divination.linked_block_id` in the output nbt it can also be sourced from an ingredient used in the recipe.
Any ingredient with the `theurgy:sulfur.source.id` NBT tag will be used to set the `theurgy:divination.linked_block_id` in the result item NBT tag.

Theurgy automatically sets that tag when sulfur is crafted from any item (as in the below example), but it works with any item thath as that same tag.

The ingredient type does not matter for Theurgy, however it needs to be an ingredient that supports the `nbt` key, e.g. `forge:partial_nbt` or `forge:nbt`. 

:::tip

In almost all cases you will want to use `forge:partial_nbt`, as it ignores any NBT tags not specified in the recipe (such as count, damage, ...)

:::

### ID Translation

In some cases the `theurgy:sulfur.source.id` might not match the desired block id. If sulfur is crafted from an iron ingot, the source will be `minecraft:iron_ingot` but the desired block id is `minecraft:iron_ore`. Theurgy has a generic translation logic that will handle most items that follow vanilla naming conventions. 

If you end up with a Divination rod with the wrong linked block id, you have two options:

1. Use the `theurgy:divination.linked_block_id` NBT tag to set the correct block id manually, as seen above
2. In the `theurgy-server.toml` config file (Beware: You should provide this in `/defaultconfigs` folder so all new worlds start with it) you can provide a mapping in `sulfurSourceToBlockMapping` in the `[recipes]` category.  

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
      "theurgy:divination.linked_block_id": "minecraft:diamond_ore",
      "theurgy:divination.setting.allow_attuning": false,
      "theurgy:divination.setting.tier": "minecraft:stone",
      "theurgy:divination.setting.allowed_blocks_tag": "theurgy:divination_rod_t4_allowed_blocks",
      "theurgy:divination.setting.range": 96,
      "theurgy:divination.setting.duration": 40,
      "theurgy:divination.setting.max_damage": 10
    }
  }
}
```