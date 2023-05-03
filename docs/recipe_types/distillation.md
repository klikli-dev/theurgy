---
sidebar_position: 20
---

# Distillation Recipes

## Properties

* `type` - **ResourceLocation**, the recipe type. Must be `theurgy:distillation`.
* `distillation_time` - **Integer**, The number of ticks the distillation process takes.
* `ingredient` - **Forge Ingredient**, The item to be distilled.
  * the item
    * `tag` **Tag ResourceLocation**, the tag accepted as input.
    * or `item` **Item ResourceLocation**, the item accepted as input.
* `ingredient_count` - **Integer**, The amount of input ingredient required for the recipe. Defaults to 1 if not specified.
* `result` **ItemStack** representing the result of the distillation process.
  * `count` **Integer**, the output count.
  * `item` **Item ResourceLocation**, the output item.

## Example Recipe

```json
{
  "type": "theurgy:distillation",
  "distillation_time": 200,
  "ingredient": {
    "tag": "forge:stone"
  },
  "ingredient_count": 10,
  "result": {
    "count": 1,
    "item": "theurgy:mercury_shard"
  }
}
```