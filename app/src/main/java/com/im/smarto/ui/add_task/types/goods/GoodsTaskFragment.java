package com.im.smarto.ui.add_task.types.goods;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.im.smarto.Constants;
import com.im.smarto.R;
import com.im.smarto.ui.base.BaseFragment;
import com.melnykov.fab.FloatingActionButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsTaskFragment extends BaseFragment implements IGoodsTaskFragment {

    @BindView(R.id.goods_task_description)
    MaterialEditText mGoodsTaskEditText;
    @BindView(R.id.goods_preview_group_name)
    TextView mGroupPreview;
    @BindView(R.id.btn_add_goods_task)
    Button mAddGoodsTaskButton;

    @Inject
    GoodsTaskPresenter<IGoodsTaskFragment> mGoodsTaskPresenter;

    int groupPosition;

    public GoodsTaskFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.btn_add_goods_task)
    void onClick(){
        mGoodsTaskPresenter.onAddTaskClicked(mGoodsTaskEditText.getText().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) groupPosition = bundle.getInt(Constants.GROUP_POSITION);

        mGoodsTaskPresenter.onAttach(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goods_task, container, false);
        ButterKnife.bind(this, v);
        mGoodsTaskPresenter.onCreate(groupPosition);

        return v;
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
    public void showChooseGroupAlertDialog(String[] groupNames) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Choose group");
        builder.setIcon(R.drawable.ic_playlist_add_check_black_24dp);
        builder.setSingleChoiceItems(groupNames, -1, (dialog, which) -> {
            mGoodsTaskPresenter.onSingleChoiceClicked(groupNames[which]);
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {

        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    @Override
    public void showAddGroupAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Create group");
        builder.setMessage("Enter the name of group that you want to create");
        builder.setIcon(R.drawable.ic_playlist_add_black_24dp);

        View view = getLayoutInflater().inflate(R.layout.alert_dialog_add_group_layout, null);
        MaterialEditText mAlertDialogEditText =
                view.findViewById(R.id.alert_dialog_add_group_edit_text);

        builder.setView(view);
        builder.setPositiveButton("Add", (dialog, which) ->
                mGoodsTaskPresenter.onDialogAddGroupClicked(mAlertDialogEditText.getText()
                        .toString()));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showGroupNamePreview(String groupName) {
        mGroupPreview.setText(groupName);
        mGroupPreview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoodsTaskPresenter.onDetach();
    }
}
