/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.klikli_dev.theurgy.util;

import net.minecraft.util.math.BlockPos;

import java.util.stream.Stream;

public class Math3DUtil {
    //region Static Methods

    /**
     * Gets all block positions in a box of the given radius around the given pos.
     *
     * @param pos    the block pos.
     * @param radius the size of the box
     * @return an iterable of block positions, including the given position.
     */
    public static Stream<BlockPos> getBlockPosInBox(BlockPos pos, int radius) {
        return BlockPos.getAllInBox(pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
                pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius).map(BlockPos::toImmutable);
    }
    //endregion Static Methods
}
