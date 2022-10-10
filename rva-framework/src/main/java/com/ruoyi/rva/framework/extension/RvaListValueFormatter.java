package com.ruoyi.rva.framework.extension;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.domain.RvaViewproperty;

public interface RvaListValueFormatter {

    String formatValue (RvaView list, RvaMap<String, Object> row, RvaMap req, RvaViewproperty prop, String value);
}
