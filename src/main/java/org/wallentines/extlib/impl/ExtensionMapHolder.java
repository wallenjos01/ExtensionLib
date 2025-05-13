package org.wallentines.extlib.impl;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface ExtensionMapHolder {

    ExtensionMap getExtensionMap();

    void setExtensionMap(ExtensionMap registry);

}
