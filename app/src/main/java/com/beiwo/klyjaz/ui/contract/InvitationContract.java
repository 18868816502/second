package com.beiwo.klyjaz.ui.contract;


import com.beiwo.klyjaz.base.BasePresenter;
import com.beiwo.klyjaz.base.BaseView;
import com.beiwo.klyjaz.entity.Invitation;

import java.util.List;

public interface InvitationContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {
        void showInvitationCode(String code);

        void showInvitations(List<Invitation.Row> list);
    }
}
