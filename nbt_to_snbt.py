import nbtlib
import sys

def convert_nbt_to_snbt(nbt_path):
    try:
        nbt_file = nbtlib.load(nbt_path)
        # Use str() which often returns SNBT in nbtlib
        print(nbt_file)
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    convert_nbt_to_snbt(
        "j:/Projects/Minecraft/theurgy-26.1/src/main/resources/data/theurgy/structure/caloric_flux_emitter_test.nbt"
    )
