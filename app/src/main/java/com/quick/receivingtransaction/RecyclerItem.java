package com.quick.receivingtransaction;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 11/2/2017.
 */

class RecyclerItem extends RecyclerView.Adapter<RecyclerItem.ViewHplderItem> {
    ArrayList<DataRowRcv> mDataRowRcv;
    Context mContext;
    ItemClickListener mItemClickListen;
    dbHelp helper;

    public RecyclerItem(Context context, ArrayList<DataRowRcv> dataRowRcv, ItemClickListener listener) {
        mContext = context;
        mItemClickListen = listener;
        mDataRowRcv = dataRowRcv;
        helper = new dbHelp(mContext);
    }

    interface ItemClickListener {
        void OnItemClick(int position, View itemView);
    }

    @Override
    public int getItemCount() {
        return mDataRowRcv.size();
    }

    @Override
    public ViewHplderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_list_item_rcv, parent, false);
        return new ViewHplderItem(view);
    }

    @Override
    public void onBindViewHolder(final ViewHplderItem holder, final int position) {
        final DataRowRcv DataRowRcv = mDataRowRcv.get(position);
        holder.tv_ItemCode  .setText(DataRowRcv.M_C6_SEGMENT1);
        holder.tv_QtyOrder  .setText(DataRowRcv.M_C10_QUANTITY_ORDERED);
        holder.tv_QtyReceive    .setText(DataRowRcv.M_C11_QUANTITY_RECEIVED);
        holder.tv_uom     .setText(DataRowRcv.M_C1_PRIMARY_UNIT_OF_MEASURE);
        holder.tv_Location  .setText(DataRowRcv.M_C7_ORGANIZATION_ID_CODE);
        holder.tv_description  .setText(DataRowRcv.M_C17_DESCRIPTION);
        holder.tv_note  .setText(DataRowRcv.M_C18_NOTE);
        holder.tv_QtyReceive2  .setText(DataRowRcv.M_C22_QTY_RECEIVE);
        holder.tv_flag  .setText(DataRowRcv.M_C21_SELECTED);
        holder.tv_req.setText(DataRowRcv.M_C19_REQUESTOR);
        holder.tv_urut.setText(DataRowRcv._id);

        holder.cb_pilih.setOnCheckedChangeListener(null);

        holder.cb_pilih.setChecked(DataRowRcv.isSelected(false));

        holder.cb_pilih.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    helper.updateFlag("Sudah Centang",DataRowRcv.M_C6_SEGMENT1,DataRowRcv.M_C7_ORGANIZATION_ID_CODE,DataRowRcv.M_C3_PO_LINE_ID,DataRowRcv.M_C4_LINE_ORGANIZATION_ID_ID,DataRowRcv._id);
                    mDataRowRcv.get(position).setSelected("Sudah Centang");
                    mDataRowRcv.get(position).isSelected(true);
                    holder.cb_pilih.setChecked(DataRowRcv.isSelected(true));
                }
                else {
                    helper.updateFlag("N",DataRowRcv.M_C6_SEGMENT1,DataRowRcv.M_C7_ORGANIZATION_ID_CODE,DataRowRcv.M_C3_PO_LINE_ID,DataRowRcv.M_C4_LINE_ORGANIZATION_ID_ID,DataRowRcv._id);
                    mDataRowRcv.get(position).setSelected("N");
                    mDataRowRcv.get(position).isSelected(false);
                    holder.cb_pilih.setChecked(DataRowRcv.isSelected(false));
                }
            }
        });


    }

    class ViewHplderItem extends RecyclerView.ViewHolder implements AdapterView.OnClickListener {
        CardView cv_data;
        TextView tv_urut,tv_ItemCode,tv_QtyOrder,tv_QtyReceive,tv_uom,tv_Location,tv_QtyReceive2, tv_description, tv_note, tv_flag, tv_req;
        CheckBox cb_pilih;

        public ViewHplderItem(View view) {
            super(view);
            cv_data = (CardView) view.findViewById(R.id.cv_data);
            tv_urut = (TextView) view.findViewById(R.id.tv_urut);

            tv_ItemCode = (TextView) view.findViewById(R.id.tv_ItemCode);
            tv_QtyOrder = (TextView) view.findViewById(R.id.tv_QtyOrder);
            tv_QtyReceive = (TextView) view.findViewById(R.id.tv_QtyReceive);
            tv_uom = (TextView) view.findViewById(R.id.tv_uom);
            tv_Location = (TextView) view.findViewById(R.id.tv_Location);
            tv_QtyReceive2 = (TextView) view.findViewById(R.id.tv_QtyReceive2);
            tv_description = view.findViewById(R.id.tv_DescR);
            tv_note = view.findViewById(R.id.tv_Note);
            cb_pilih = (CheckBox) view.findViewById(R.id.cb_pilih_item);
            tv_flag = view.findViewById(R.id.tv_flag);
            tv_req = view.findViewById(R.id.tv_Req);

            cv_data.setOnClickListener(this);

            this.setIsRecyclable(false);
        }

        @Override
        public void onClick(View v) {
            mItemClickListen.OnItemClick(getAdapterPosition(), v);
        }
    }

//    void showMessage() {
//        mDataRowRcv.clear();
//        mDataRowRcv.addAll(getDataSet());
//        mDataRowRcv.notifyDataSetChanged();
//    }
}
