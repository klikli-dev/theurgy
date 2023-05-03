---
sidebar_position: 5
---

# Datagen

If you are at least passingly familiar with java and minecraft modding, then the easiest way to add recipes to theurgy is to use the theurgy datagen.
You can find Recipe Providers for all Theurgy recipe types here: https://github.com/klikli-dev/theurgy/tree/version/1.19.4/src/main/java/com/klikli_dev/theurgy/datagen

Simply clone the theurgy repository, add your own datagen code based on the existing example code, run the gradle task `runData` and copy the results from `src/generated/resources/...` into your datapack.