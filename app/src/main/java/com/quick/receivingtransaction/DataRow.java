package com.quick.receivingtransaction;

public class DataRow {
    String M_C1_RECEIPT_NUM     ;
    String M_C2_ITEM             ;
    String M_C3_ITEM_DESC           ;
    String M_C4_REQUESTOR          ;
    String M_C5_QTY             ;
    String M_C6_UOM       ;
    String M_C7_STATUS_BARANG    ;
    String M_C8_LOCATION          ;
    String M_C9_COMMENTS          ;
    String M_C10_FLAG        ;
    String _id                        ;

    void setData(String C1_RECEIPT_NUM,
                 String C2_ITEM,
                 String C3_ITEM_DESC,
                 String C4_REQUESTOR,
                 String C5_QTY,
                 String C6_UOM,
                 String C7_STATUS_BARANG,
                 String C8_LOCATION,
                 String C9_COMMENTS,
                 String C10_FLAG,
                 String id){
        M_C1_RECEIPT_NUM  = C1_RECEIPT_NUM ;
        M_C2_ITEM           = C2_ITEM          ;
        M_C3_ITEM_DESC        = C3_ITEM_DESC       ;
        M_C4_REQUESTOR        = C4_REQUESTOR       ;
        M_C5_QTY          = C5_QTY         ;
        M_C6_UOM    = C6_UOM   ;
        M_C7_STATUS_BARANG = C7_STATUS_BARANG;
        M_C8_LOCATION       = C8_LOCATION      ;
        M_C9_COMMENTS       = C9_COMMENTS      ;
        M_C10_FLAG     = C10_FLAG    ;
        _id       = id    ;
    }

}
