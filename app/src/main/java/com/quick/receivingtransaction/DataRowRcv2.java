package com.quick.receivingtransaction;

public class DataRowRcv2 {
    String M_C1_receipt_num     ;
    String M_C2_po_number             ;
    String M_C3_item          ;
    String M_C4_item_desc          ;
    String M_C5_requestor          ;
    String M_C6_qty       ;
    String M_C7_uom   ;
    String M_C8_tgl_dibuat          ;
    String M_C9_tgl_diterima        ;
    String M_C10_shippement        ;
    String M_C11_status_barang       ;
    String M_C12_location        ;
    String M_C13_comments        ;
    String _id                        ;

    void setData(String C1_receipt_num,
                 String C2_po_number,
                 String C3_item,
                 String C4_item_desc,
                 String C5_requestor,
                 String C6_qty,
                 String C7_uom,
                 String C8_tgl_dibuat,
                 String C9_tgl_diterima,
                 String C10_shippement,
                 String C11_status_barang,
                 String C12_location,
                 String C13_comments,
                 String id){
        M_C1_receipt_num  = C1_receipt_num ;
        M_C2_po_number           = C2_po_number          ;
        M_C3_item        = C3_item       ;
        M_C4_item_desc        = C4_item_desc       ;
        M_C5_requestor          = C5_requestor         ;
        M_C6_qty    = C6_qty   ;
        M_C7_uom = C7_uom;
        M_C8_tgl_dibuat       = C8_tgl_dibuat      ;
        M_C9_tgl_diterima       = C9_tgl_diterima      ;
        M_C10_shippement     = C10_shippement    ;
        M_C11_status_barang    = C11_status_barang    ;
        M_C12_location     = C12_location    ;
        M_C13_comments     = C13_comments    ;
        _id       = id    ;
    }
}
