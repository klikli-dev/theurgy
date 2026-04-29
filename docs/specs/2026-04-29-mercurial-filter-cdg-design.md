<!-- SPDX-FileCopyrightText: 2026 klikli-dev -->
<!-- SPDX-License-Identifier: MIT -->

# Mercurial filter CDG integration design

## Goal

Replace Theurgy's mercurial attribute and list filter implementation with the code-defined-gui implementation while keeping Theurgy-owned registry entries, item models, menu types, and screen classes in place where needed for future styling.

## Scope

- Replace Theurgy attribute/list filter item classes with CDG item classes in Theurgy's item registry.
- Keep existing Theurgy item IDs and item models.
- Keep Theurgy menu type registrations and screen registrations.
- Convert Theurgy attribute/list filter menus into thin subclasses over CDG menu implementations.
- Convert Theurgy attribute/list filter screens into thin subclasses over CDG screen implementations.
- Switch runtime filter state and serialization to CDG's format.
- Add code-defined-gui as a required runtime dependency in mod metadata and release metadata.

## Out of scope

- Migration of existing Theurgy filter item data to CDG state.
- Preserving Theurgy's old filter item classes or old filter menu state format.
- Adopting CDG's menu type registrations or screen registrations directly.

## Architecture

### Screen ownership

Theurgy keeps `AttributeFilterScreen` and `ListFilterScreen`, but each becomes a child class of the corresponding CDG screen class:

- `com.klikli_dev.codedefinedgui.gui.filter.AttributeFilterScreen`
- `com.klikli_dev.codedefinedgui.gui.filter.ListFilterScreen`

The first implementation should keep these subclasses minimal and only override behavior when needed for Theurgy-specific styling later.

### Menu ownership

Theurgy keeps its own registered menu types, but the Theurgy menu classes become child classes of the corresponding CDG menu classes:

- `com.klikli_dev.codedefinedgui.filter.attribute.AttributeFilterMenu`
- `com.klikli_dev.codedefinedgui.filter.list.ListFilterMenu`

These Theurgy subclasses should remain thin wrappers whose main responsibility is preserving Theurgy menu registration and construction flow.

### Item ownership

Theurgy no longer keeps dedicated filter item classes for the mercurial attribute and list filters. Instead, Theurgy's item registry entries instantiate the CDG filter item classes directly. This preserves Theurgy item IDs and item models while moving filter item behavior to CDG.

### State ownership

Filter contents and filter configuration switch fully to CDG's state accessors and serialization format. Old Theurgy filter state is not migrated. Existing worlds/items using the old Theurgy data format may lose or reset saved filter configuration after the swap.

## Ownership split

### Theurgy-owned

- item registry entries and IDs for the two filter items
- item models
- menu type registrations
- screen classes
- screen registration wiring

### CDG-owned

- filter item classes used by the Theurgy registry entries
- filter menu behavior and state handling
- filter GUI behavior, widgets, and base layout logic
- attribute/list filter serialization format

### Removed from Theurgy

- `AttributeFilterItem`
- `ListFilterItem`
- Theurgy-specific save/load logic for these two filters
- Theurgy-specific button handling and widget composition for these two screens, except future styling overrides in the child screens

## Integration flow

1. Theurgy item registry entries for the two mercurial filters are updated to create CDG filter item instances.
2. Theurgy menu type registrations remain unchanged, but their factories construct Theurgy subclasses of the CDG menu classes.
3. Theurgy screen registration remains unchanged, but the registered screens are Theurgy subclasses of the CDG screen classes.
4. Opening a Theurgy mercurial filter still enters Theurgy-owned registration points, but the actual filter logic and UI behavior come from CDG.
5. Theurgy item models continue to resolve as before because the registry entries remain Theurgy-owned.

## Dependency changes

### Runtime metadata

`src/main/templates/META-INF/neoforge.mods.toml` gains a required dependency on `codedefinedgui` using `${code_defined_gui_version}`.

### Build and release metadata

`.github/workflows/build_and_publish.yaml` is updated so published release metadata declares `codedefinedgui(required)` alongside the other required dependencies.

## Validation

Validation should confirm:

- both Theurgy mercurial filter items still exist under their current IDs
- their item models still render correctly
- opening each item uses Theurgy-registered menu types and Theurgy screen subclasses
- the screen behavior comes from CDG base screens
- list filter state saves and reloads using CDG behavior
- attribute filter rule selection and save/reload use CDG behavior
- release metadata and `mods.toml` both mark CDG as required

## Risks

- Existing item data using Theurgy's old format will not migrate.
- Constructor and registration mismatches may appear if Theurgy factories do not line up with CDG menu/item constructor expectations.
- Small layout or slot index differences may need minor subclass adjustments to preserve Theurgy registration while using CDG behavior.

## Recommendation

Implement the swap as thin Theurgy adapters over CDG for screens and menus, with direct CDG item instances in Theurgy's registry. This minimizes Theurgy-owned logic while preserving the styling hooks you want.
