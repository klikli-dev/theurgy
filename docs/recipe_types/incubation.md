---
sidebar_position: 50
---

# Incubation Recipes

## Properties

* `type` - **ResourceLocation**, the recipe type. Must be `theurgy:incubation`.
* `incubation_time` - **Integer**, the number of ticks the incubation process takes.
* `mercury` - **Forge Ingredient**, the mercury ingredient.
  * `tag` **Tag ResourceLocation**, the tag accepted as input.
  * or `item` **Item ResourceLocation**, the item accepted as input.
* `salt` - **Forge Ingredient**, the salt ingredient.
  * `tag` **Tag ResourceLocation**, the tag accepted as input.
  * or `item` **Item ResourceLocation**, the item accepted as input.
* `sulfur` - **Forge Ingredient**, the sulfur ingredient.
  * `tag` **Tag ResourceLocation**, the tag accepted as input.
  * or `item` **Item ResourceLocation**, the item accepted as input.
* `result` **RecipeResult** representing the result of the incubation process.  
  RecipeResults are much like ItemStack results, except they also support `tag` instead of just `item`.  
  When a tag is specified, the result will be the first item in the tag, or of AlmostUnified is installed, the preferred item in the tag.  
  * the item 
    * `tag` **Tag ResourceLocation**, the tag to produce.
    * or `item` **Item ResourceLocation**, the item to produce.
  * `count` **Integer**, the output count.
  * `nbt` **Compound NBT**, the NBT data to be applied to the output item.

## Example Recipe

```json
{
  "type": "theurgy:incubation",
  "incubation_time": 200,
  "mercury": {
    "item": "theurgy:mercury_shard"
  },
  "result": {
    "count": 1,
    "item": "minecraft:wheat"
  },
  "salt": {
    "item": "theurgy:alchemical_salt_crops"
  },
  "sulfur": {
    "item": "theurgy:alchemical_sulfur_wheat"
  }
}
```