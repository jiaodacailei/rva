package com.ruoyi.rva.be.controller.view;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.domain.RvaViewproperty;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RvaListExportExcelView extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RvaMap rvaMap = new RvaMap(model);
        RvaView viewData = (RvaView) rvaMap.get("viewData");
        List<RvaViewproperty> properties = viewData.getProperties();

        List<Map<String, Object>> listData = rvaMap.getList("listData");

        //自定义文件名称
        String excelName = viewData.getName() + ".xlsx";
        String Agent = request.getHeader("User-Agent");
        if (null != Agent) {
            Agent = Agent.toLowerCase();
            //针对火狐乱码的处理
            if (Agent.indexOf("firebox") != -1) {
                response.setHeader("content-disposition", String.format("attachment;filename*=utf-8'zh_cn'%s", URLEncoder.encode(excelName, "utf-8")));
            } else {
                response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(excelName, "utf-8"));
            }
        }

        response.setContentType("application/ms-excel; charset=UTF-8");
        Sheet sheet = workbook.createSheet("sheet1");
        sheet.setDefaultColumnWidth(30);
        fill(sheet, properties, listData);
        workbook.write(response.getOutputStream());

    }


    protected void fill(Sheet sheet, List<RvaViewproperty> properties, List<Map<String, Object>> listData) {
        //创建行的头部
        Row header = sheet.createRow(0);
        int i = 0;
        properties = properties.stream().filter(p ->
                !RvaViewproperty.TYPE_BUTTON.equals(p.getType()) && !RvaViewproperty.TYPE_HIDDEN.equals(p.getType())).collect(Collectors.toList());

        for (RvaViewproperty viewproperty : properties) {
            String name = viewproperty.getName();
            header.createCell(i).setCellValue(name);
            i++;

        }
        for (int i1 = 0; i1 < listData.size(); i1++) {
            Map<String, Object> row = listData.get(i1);
            Row r = sheet.createRow(i1 + 1
            );
            int n = 0;
            for (RvaViewproperty viewproperty : properties) {
                Object val = row.get(viewproperty.getId());
                //隐藏(hidden)
                String type = viewproperty.getType();

                if (val == null) {
                    val = "";
                }
                if (RvaViewproperty.TYPE_DICTIONARY.equals(type)) {
                    String dictName = viewproperty.getDictName(val.toString());
                    r.createCell(n).setCellValue(dictName);
                } else {
                    r.createCell(n).setCellValue(val.toString());
                }
                n++;

            }
        }
    }
}
