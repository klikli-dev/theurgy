---
sidebar_position: 40
---

# Liquefaction Recipes

## Properties

* `type` - **ResourceLocation**, the recipe type. Must be `theurgy:liquefaction`.
* `liquefaction_time` - **Integer**, The number of ticks the liquefaction process takes.
* `ingredient` - **Forge Ingredient**, The item to be liquefied.
  * the item
    * `tag` **Tag ResourceLocation**, the tag accepted as input.
    * or `item` **Item ResourceLocation**, the item accepted as input.
* `solvent` - **Theurgy FluidIngredient**, The fluid to be used as a solvent to liquefy the input item.
  * the fluid
    * `tag` **Tag ResourceLocation**, the tag accepted as input.
    * or `fluid` **Fluid ResourceLocation**, the fluid accepted as input.
  * `amount` **Integer**, the amount of fluid required to liquefy the input item.
* `result` **ItemStack** representing the result of the liquefaction process.
  * `count` **Integer**, the output count.
  * `item` **Item ResourceLocation**, the output item.
  * `nbt` **Compound NBT**, the NBT data to be applied to the output item.
    * `theurgy:sulfur.source.id` **ResourceLocation**, the ID of the input item or tag (prefixed with `#`).
      * this is used to render the source item on the sulfur item icon.
      * if missing, the recipe loader will attempt to infer the source item from the `ingredient` property.


:::tip

If you want to add a new sulfur for your modpack you can use the item id `theurgy:alchemical_sulfur_generic` (see below)

:::


## Example Recipe

```json
{
  "type": "theurgy:liquefaction",
  "ingredient": {
    "tag": "minecraft:logs"
  },
  "liquefaction_time": 200,
  "result": {
    "count": 1,
    "item": "theurgy:alchemical_sulfur_logs",
    "nbt": {
      "theurgy:sulfur.source.id": "#minecraft:logs"
    }
  },
  "solvent": {
    "amount": 10,
    "fluid": "theurgy:sal_ammoniac"
  }
}
```

Same example using the generic sulfur: 
```json
{
  "type": "theurgy:liquefaction",
  "ingredient": {
    "tag": "minecraft:logs"
  },
  "liquefaction_time": 200,
  "result": {
    "count": 1,
    "item": "theurgy:alchemical_sulfur_generic",
    "nbt": {
      "theurgy:sulfur.source.id": "#minecraft:logs"
    }
  },
  "solvent": {
    "amount": 10,
    "fluid": "theurgy:sal_ammoniac"
  }
}
```

## Generic Sulfur

Theurgy provides a generic sulfur item that can be used to provide (unlimited) sulfurs for items not handled by theurgy by default.
In order to make sure the item is displayed correctly it needs a tag as ingredient and as sulfur source id. If no tag exists you should create one for that purpose.

*In the game, "under the hood" hidden from players there will be just one item "Generic Sulfur", but in the creative menu and in JEI the players will see multiple sulfurs, one per recipe that is provided for the generic sulfur, differentiated based on the sulfur source id NBT.*

Additionally you need to specify a translation for the tag, this translation will be displayed as part of the item name.
E.g. `minecraft:logs` will be translated by placing `"tag.minecraft.logs": "Logs"` in the lang file, and `forge:ingots/iron` would be `"tag.forge.ingots.iron": "Iron Ingots"`
That means, you prepend `tag.` and then place the ID of the tag, but replace `:` and `/` with `.`  

