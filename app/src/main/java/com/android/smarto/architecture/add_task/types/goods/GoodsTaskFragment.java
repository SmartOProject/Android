package com.android.smarto.architecture.add_task.types.goods;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.smarto.R;
import com.android.smarto.architecture.base.BaseFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsTaskFragment extends BaseFragment implements IGoodsTaskFragment {

    @BindView(R.id.goods_task_edit_text) EditText mGoodsName;
    @BindView(R.id.goods_group_spinner) MaterialSpinner mGroupSpinner;
    @BindView(R.id.fab_goods_add_group) FloatingActionButton mAddGroupBtn;

    @Inject
    GoodsTaskPresenter<IGoodsTaskFragment> mGoodsTaskPresenter;

    public GoodsTaskFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.btn_add_goods_task)
    void onClick(){
        mGoodsTaskPresenter.onAddGoodsTask(mGoodsName.getText().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goods_task, container, false);

        ButterKnife.bind(this, v);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mGroupSpinner.setBackground(mContext.getDrawable(R.drawable.spinner_background));
        }
        mGroupSpinner.setOnItemSelectedListener((view, position, id, item) ->
                mGoodsTaskPresenter.onSpinnerItemClick(item.toString()));

        mGoodsTaskPresenter.onSpinnerSetup();

        mAddGroupBtn.setOnClickListener(e -> createDialog());

        return v;
    }

    /**
     * Need to refactor this method.
     */
    private void createDialog() {
        AlertDialog.Builder alert =
                new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.add_group_alert));
        final EditText edittext = new EditText(getActivity());
        alert.setTitle("Enter the name of new group");

        alert.setView(edittext);

        alert.setPositiveButton("OK", (dialog, whichButton) ->
            mGoodsTaskPresenter.onPositiveGroupDialogClick(edittext.getText().toString()));

        alert.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss());

        alert.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mGoodsTaskPresenter.onAttach(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGoodsTaskPresenter.onDetach();
    }

    @Override
    public void showNoGroupToAddTaskError() {
        Toast.makeText(mContext, R.string.add_common_task_group_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyTaskDescriptionError() {
        Toast.makeText(mContext,
                R.string.task_description_empty_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void updateSpinnerItems(List<String> groupNames) {
        mGroupSpinner.setItems(groupNames);
    }
}
