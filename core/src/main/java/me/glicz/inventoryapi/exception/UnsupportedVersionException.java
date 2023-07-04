package me.glicz.inventoryapi.exception;

import me.glicz.inventoryapi.nms.NMSInitializer;

public class UnsupportedVersionException extends RuntimeException {

    public UnsupportedVersionException() {
        super("Your server is running an unsupported version - %s!".formatted(NMSInitializer.getVersion()));
    }
}
