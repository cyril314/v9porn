package com.u9porn.ui.axgle;


import androidx.fragment.app.Fragment;

import com.u9porn.data.db.entity.Category;
import com.u9porn.ui.basemain.BaseMainFragment;

/**
 * A simple {@link Fragment} subclass.
 * @author megoc
 */
public class MainAxgleFragment extends BaseMainFragment {


    @Override
    public int getCategoryType() {
        return Category.TYPE_AXGLE;
    }

    public MainAxgleFragment() {
        // Required empty public constructor
    }

    public static MainAxgleFragment getInstance() {
        return new MainAxgleFragment();
    }
}
