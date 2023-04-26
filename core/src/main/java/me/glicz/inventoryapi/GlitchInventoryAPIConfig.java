package me.glicz.inventoryapi;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Accessors(chain = true, fluent = true)
public class GlitchInventoryAPIConfig {

    @SerializedName("verify-inventory-id-on-close")
    private boolean verifyInventoryIdOnClose;
    @SerializedName("remove-viewer-item-on-item-set")
    private boolean removeViewerItemOnItemSet = true;
    @SerializedName("synchronize-handling-packets")
    private boolean synchronizeHandlingPackets = true;
}
