package com.ruoyi.rva.framework.extension;

public interface RvaObjectDeleteExtension {

    String BEAN_FREFIX = "rva.object.delete.extension.";

    void preDelete (String objectId);
}
