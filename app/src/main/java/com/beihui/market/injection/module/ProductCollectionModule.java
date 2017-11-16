package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.ProductCollectionContract;

import dagger.Module;
import dagger.Provides;

@Module
public class ProductCollectionModule {

    private ProductCollectionContract.View view;

    public ProductCollectionModule(ProductCollectionContract.View view) {
        this.view = view;
    }

    @Provides
    ProductCollectionContract.View providesProductCollectionContractView() {
        return view;
    }
}
