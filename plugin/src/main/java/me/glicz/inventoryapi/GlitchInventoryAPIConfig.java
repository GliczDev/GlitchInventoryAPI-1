package me.glicz.inventoryapi;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GlitchInventoryAPIConfig {

    private boolean verifyInventoryIdOnClose;
}
