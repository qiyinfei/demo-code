package com.tmindtech.api.demoserver.example.model;

public class LabelInfo {
    public String saleOrder; // 出库批次号

    public Number seqNo; // 包裹的序号，例如 1,2,3

    public String uuidCode; // 唯一码

    public LabelInfo() {
    }

    public LabelInfo(String saleOrder, Number seqNo, String uuidCode) {
        this.saleOrder = saleOrder;
        this.seqNo = seqNo;
        this.uuidCode = uuidCode;
    }

    @Override
    public String toString() {
        return "LabelInfo{" +
                "saleOrder='" + saleOrder + '\'' +
                ", seqNo=" + seqNo +
                ", uuidCode='" + uuidCode + '\'' +
                '}';
    }
}
