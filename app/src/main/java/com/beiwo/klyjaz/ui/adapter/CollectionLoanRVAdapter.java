package com.beiwo.klyjaz.ui.adapter;


import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.LoanProduct;
import com.chad.library.adapter.base.BaseViewHolder;

public class CollectionLoanRVAdapter extends LoanRVAdapter {

    public CollectionLoanRVAdapter() {
        super(R.layout.list_item_collection_product);

    }

    @Override
    protected void convert(BaseViewHolder helper, LoanProduct.Row item) {
        super.convert(helper, item);
        helper.addOnClickListener(R.id.delete);
        helper.addOnClickListener(R.id.base_container);
    }
}
