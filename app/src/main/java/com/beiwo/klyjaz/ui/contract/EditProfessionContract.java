package com.beiwo.klyjaz.ui.contract;


import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;
import com.beiwo.klyjaz.entity.Profession;

import java.util.List;

public interface EditProfessionContract {

    interface Presenter extends BasePresenter {

        void updateProfession(Profession profession);
    }

    interface View extends BaseView<Presenter> {
        void showProfession(List<Profession> professions);

        void showUpdateSuccess(String msg);
    }
}
