package me.glicz.inventoryapi;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Accessors(chain = true, fluent = true)
public class GlitchInventoryAPIConfig {

    private boolean verifyInventoryIdOnClose;
    private boolean removeViewerItemOnItemSet, synchronizeItemClickAction = true;
}
