package com.quick.receivingtransaction;

/**
 * Created by Admin on 11/4/2017.
 */

public class DataRowRcv {
    String M_C1_PRIMARY_UNIT_OF_MEASURE     ;
    String M_C2_PO_HEADER_ID             ;
    String M_C3_PO_LINE_ID           ;
    String M_C4_LINE_ORGANIZATION_ID_ID          ;
    String M_C5_ORGANIZATION_CODE           ;
    String M_C6_SEGMENT1       ;
    String M_C7_ORGANIZATION_ID_CODE    ;
    String M_C8_ORGANIZATION_ID          ;
    String M_C9_CLOSED_CODE         ;
    String M_C10_QUANTITY_ORDERED        ;
    String M_C11_QUANTITY_RECEIVED       ;
    String M_C12_LINE_NUM        ;
    String M_C13_SHIPMENT_NUM        ;
    String M_C14_DESTINATION_ORGANIZATION_ID        ;
    String M_C15_ORG_ID        ;
    String M_C16_INVENTORY_ITEM_ID       ;
    String M_C17_DESCRIPTION      ;
    String M_C18_NOTE       ;
    String M_C19_REQUESTOR       ;
    String M_C20_LOCATION_ID       ;
    String M_C21_SELECTED       ;
    String M_C22_QTY_RECEIVE       ;
    String _id                        ;

    void setData(String C1_PRIMARY_UNIT_OF_MEASURE,
                 String C2_PO_HEADER_ID,
                 String C3_PO_PO_LINE_ID,
                 String C4_LINE_ORGANIZATION_ID_ID,
                 String C5_ORGANIZATION_CODE,
                 String C6_SEGMENT1,
                 String C7_ORGANIZATION_ID_CODE,
                 String C8_ORGANIZATION_ID,
                 String C9_CLOSED_CODE,
                 String C10_QUANTITY_ORDERED,
                 String C11_QUANTITY_RECEIVED,
                 String C12_LINE_NUM,
                 String C13_SHIPMENT_NUM,
                 String C14_DESTINATION_ORGANIZATION_ID,
                 String C15_ORG_ID,
                 String C16_INVENTORY_ITEM_ID,
                 String C17_DESCRIPTION ,
                 String C18_NOTE,
                 String C19_REQUESTOR,
                 String C20_LOCATION_ID,
                 String C21_SELECTED,
                 String C22_QTY_RECEIVE ,
                 String id){
        M_C1_PRIMARY_UNIT_OF_MEASURE  = C1_PRIMARY_UNIT_OF_MEASURE ;
        M_C2_PO_HEADER_ID           = C2_PO_HEADER_ID          ;
        M_C3_PO_LINE_ID        = C3_PO_PO_LINE_ID       ;
        M_C4_LINE_ORGANIZATION_ID_ID        = C4_LINE_ORGANIZATION_ID_ID       ;
        M_C5_ORGANIZATION_CODE          = C5_ORGANIZATION_CODE         ;
        M_C6_SEGMENT1    = C6_SEGMENT1   ;
        M_C7_ORGANIZATION_ID_CODE = C7_ORGANIZATION_ID_CODE;
        M_C8_ORGANIZATION_ID       = C8_ORGANIZATION_ID      ;
        M_C9_CLOSED_CODE       = C9_CLOSED_CODE      ;
        M_C10_QUANTITY_ORDERED     = C10_QUANTITY_ORDERED    ;
        M_C11_QUANTITY_RECEIVED     = C11_QUANTITY_RECEIVED    ;
        M_C12_LINE_NUM     = C12_LINE_NUM    ;
        M_C13_SHIPMENT_NUM     = C13_SHIPMENT_NUM    ;
        M_C14_DESTINATION_ORGANIZATION_ID     = C14_DESTINATION_ORGANIZATION_ID    ;
        M_C15_ORG_ID     = C15_ORG_ID   ;
        M_C16_INVENTORY_ITEM_ID     = C16_INVENTORY_ITEM_ID    ;
        M_C17_DESCRIPTION   = C17_DESCRIPTION   ;
        M_C18_NOTE    = C18_NOTE   ;
        M_C19_REQUESTOR   = C19_REQUESTOR   ;
        M_C20_LOCATION_ID   = C20_LOCATION_ID  ;
        M_C21_SELECTED    = C21_SELECTED  ;
        M_C22_QTY_RECEIVE    = C22_QTY_RECEIVE   ;
        _id       = id    ;
    }

    void setSelected(String flag){
        M_C21_SELECTED= flag;
    }

    public String getSelected(){
        return M_C21_SELECTED;
    }

    public boolean isSelected(boolean selected) {
        return selected;
    }
}

