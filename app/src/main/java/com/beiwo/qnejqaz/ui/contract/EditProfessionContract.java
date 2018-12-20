package com.beiwo.qnejqaz.ui.contract;


import com.beiwo.qnejqaz.base.BasePresenter;
import com.beiwo.qnejqaz.base.BaseView;
import com.beiwo.qnejqaz.entity.Profession;

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
