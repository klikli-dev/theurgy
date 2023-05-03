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