package com.knowledge.domain.output;


import com.common.Enum.FormatNameEnum;
import com.knowledge.model.MaterialList;

public class MaterialListOutput extends MaterialList {

    private String materialFormName;

    public String getMaterialFormName() {
        StringBuilder stringBuilder = new StringBuilder("");
        if (getMaterialForm() != null) {
            String materialForm = getMaterialForm();
            String[] split = materialForm.split(",");
            if (split.length > 0) {
                for (String str : split) {
                    if (str.equals(FormatNameEnum.ORIGINAL.getCode().toString())) {
                        stringBuilder.append(FormatNameEnum.ORIGINAL.getDesc() + ",");

                    } else if (str.equals(FormatNameEnum.COPY.getCode().toString())) {
                        stringBuilder.append(FormatNameEnum.COPY.getDesc() + ",");

                    } else if (str.equals(FormatNameEnum.ORIGINAL_OR_COPY.getCode().toString())) {
                        stringBuilder.append(FormatNameEnum.ORIGINAL_OR_COPY.getDesc() + ",");

                    } else if (str.equals(FormatNameEnum.PAPER.getCode().toString())) {
                        stringBuilder.append(FormatNameEnum.PAPER.getDesc() + ",");

                    } else if (str.equals(FormatNameEnum.ELECTRONIC_DOCUMENT.getCode().toString())) {
                        stringBuilder.append(FormatNameEnum.ELECTRONIC_DOCUMENT.getDesc() + ",");

                    } else if (str.equals(FormatNameEnum.NO_SUBMIT.getCode().toString())) {
                        stringBuilder.append(FormatNameEnum.NO_SUBMIT.getDesc() + ",");

                    } else if (str.equals(FormatNameEnum.DATA_SCARCITY.getCode().toString())) {
                        stringBuilder.append(FormatNameEnum.DATA_SCARCITY.getDesc() + ",");

                    } else if (str.equals(FormatNameEnum.SCAN_ORIGINAL.getCode().toString())) {
                        stringBuilder.append(FormatNameEnum.SCAN_ORIGINAL.getDesc() + ",");

                    } else {
                        stringBuilder.append(FormatNameEnum.PDF.getDesc() + ",");
                    }
                }
            }
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}
