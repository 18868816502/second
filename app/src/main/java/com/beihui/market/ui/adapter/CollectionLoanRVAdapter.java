package com.beihui.market.ui.adapter;


import com.beihui.market.R;
import com.beihui.market.entity.LoanProduct;
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
