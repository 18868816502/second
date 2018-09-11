package com.beihui.market.injection.module;

import com.beihui.market.ui.contract.PersonalCenterContact;

import dagger.Module;
import dagger.Provides;

/**
 * @name loanmarket
 * @class name：com.beihui.market.injection.module
 * @class describe
 * @anthor A
 * @time 2018/9/11 17:52
 */
@Module
public class PersonalCenterModule {

    private PersonalCenterContact.View view;

    public PersonalCenterModule(PersonalCenterContact.View view) {
        this.view = view;
    }

    @Provides
    public PersonalCenterContact.View provideView(){
        return view;
    }
}
