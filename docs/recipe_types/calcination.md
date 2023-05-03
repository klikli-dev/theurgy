---
sidebar_position: 10
---

# Calcination Recipes

## Properties

* `type` - **ResourceLocation**, the recipe type. Must be `theurgy:calcination`.
* `calcination_time` - **Integer**, The number of ticks the calcination process takes.
* `ingredient` - **Forge Ingredient**, The item to calcinate.
  * the item
    * `tag` **Tag ResourceLocation**, the tag accepted as input.
    * or `item` **Item ResourceLocation**, the item accepted as input.
* `result` **ItemStack** representing the result of the calcination process.
  * `count` **Integer**, the output count.
  * `item` **Item ResourceLocation**, the output item.

## Example Recipe

```json
{
  "type": "theurgy:calcination",
  "calcination_time": 200,
  "ingredient": {
    "tag": "forge:crops"
  },
  "result": {
    "count": 1,
    "item": "theurgy:alchemical_salt_crops"
  }
}
```