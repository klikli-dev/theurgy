---
sidebar_position: 30
---

# Accumulation Recipes


## Properties

* `type` - **ResourceLocation**, the recipe type. Must be `theurgy:accumulation`.
- `evaporant` - **Theurgy FluidIngredient**, the fluid that will be evaporated to create the result fluid stack.
  - `fluid` **Fluid ResourceLocation**, the fluid accepted as input.
   * the fluid
    * `tag` **Tag ResourceLocation**, the tag accepted as input.
    * or `fluid` **Fluid ResourceLocation**, the fluid accepted as input.
  - `amount` **Integer**, the amount of fluid required / to consume.
- `solute` - **Forge Ingredient**, an optional solid ingredient to be added to the evaporant.
  - `tag` **Tag ResourceLocation**, the tag accepted as input.
  - or `item` **Item ResourceLocation**, the item accepted as input.
- `result` - **FluidStack**, the fluid stack resulting from the accumulation process.
  - `fluid` **Fluid ResourceLocation**, the output fluid.
  - `amount` **Integer**, the output amount.
- `accumulation_time` - **Integer**, the number of ticks the accumulation process takes. Default is 200 ticks.

## Example Recipe

```json
{
  "type": "theurgy:accumulation",
  "accumulation_time": 200,
  "evaporant": {
    "amount": 1000,
    "tag": "minecraft:water"
  },
  "result": {
    "Amount": 1000,
    "FluidName": "theurgy:sal_ammoniac"
  },
  "solute": {
    "tag": "forge:gems/sal_ammoniac"
  }
}
```