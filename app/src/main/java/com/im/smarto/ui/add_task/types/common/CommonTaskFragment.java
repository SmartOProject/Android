package com.im.smarto.ui.add_task.types.common;

import android.app.AlertDialog;
import android.content.Context;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommonTaskFragment extends BaseFragment implements ICommonTaskFragment,
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private static final String TAG = CommonTaskFragment.class.getSimpleName();

    @BindView(R.id.common_task_description) MaterialEditText mCommonTaskDescription;
    @BindView(R.id.button_time) FloatingActionButton mIsNeedDateButton;
    @BindView(R.id.preview_group_name) TextView mPreviewGroupName;
    @BindView(R.id.time_text_view) TextView mPreviewTime;
    @BindView(R.id.data_text_view) TextView mPreviewDate;
    @BindView(R.id.btn_add_common_task) Button mAddTaskButton;

    @Inject
    CommonTaskPresenter<ICommonTaskFragment> mCommonTaskPresenter;

    int groupPosition;

    private TimePickerDialog mTimePickerDialog;
    private DatePickerDialog mDatePickerDialog;

    public CommonTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) groupPosition = bundle.getInt(Constants.GROUP_POSITION);

        mCommonTaskPresenter.onAttach(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_common_task, container, false);
        ButterKnife.bind(this, v);
        mCommonTaskPresenter.onCreate(groupPosition);

        return v;
    }

    @OnClick({R.id.btn_add_common_task, R.id.button_time})
    void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_add_common_task:
                mCommonTaskPresenter.onAddCommonTaskClicked(mCommonTaskDescription.getText().toString());
                break;
            case R.id.button_time:
                mCommonTaskPresenter.onTimeButtonClicked();
                break;
        }
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void setGroupNamePreview(String groupName) {
        mPreviewGroupName.setText(groupName);
    }

    @Override
    public void setupCalendar() {
        Calendar calendar = Calendar.getInstance();
        mDatePickerDialog = DatePickerDialog.newInstance(
                this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        mDatePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        mDatePickerDialog.setAccentColor(getResources().getColor(R.color.secondary_light));

        mTimePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        mTimePickerDialog.setVersion(com.wdullaer.materialdatetimepicker.time.TimePickerDialog.Version.VERSION_2);
        mTimePickerDialog.setAccentColor(getResources().getColor(R.color.secondary_light));
    }

    @Override
    public void showEmptyDescriptionError() {
        Toast.makeText(getActivity(), "Description is empty", Toast.LENGTH_LONG).show();
    }

    @Override
    public void changeTimeButtonBackground(boolean isActive) {
        int bgRes;
        if (isActive) bgRes = getResources().getColor(R.color.primary_normal);
        else bgRes = getResources().getColor(R.color.secondary_normal);
        mIsNeedDateButton.setBackgroundColor(bgRes);
    }

    @Override
    public void showTargetDatePreview(String targetDate, String targetTime) {
        mPreviewDate.setText(targetDate);
        mPreviewTime.setText(targetTime);
        mPreviewDate.setVisibility(View.VISIBLE);
        mPreviewTime.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTargetDatePreview() {
        mPreviewDate.setVisibility(View.INVISIBLE);
        mPreviewTime.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showChooseGroupDialog(String[] groupNames) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Choose group");
        builder.setIcon(R.drawable.ic_playlist_add_check_black_24dp);
        builder.setSingleChoiceItems(groupNames, -1, (dialog, which) -> {
            mCommonTaskPresenter.onSingleChoiceClicked(groupNames[which]);
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {

        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    @Override
    public void showGroupNamePreview(String groupName) {
        mPreviewGroupName.setText(groupName);
        mPreviewGroupName.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAddGroupDialog() {
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
                mCommonTaskPresenter.onDialogAddGroupClicked(mAlertDialogEditText.getText()
                        .toString()));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mCommonTaskPresenter.onDateSet(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        mCommonTaskPresenter.onTimeSet(hourOfDay, minute);
    }

    @Override
    public void showDatePickerDialog() {
        mDatePickerDialog.show(getFragmentManager(), Constants.DATE_PICKER_DIALOG_TAG);
    }

    @Override
    public void showTimePickerDialog() {
        mTimePickerDialog.show(getFragmentManager(), Constants.TIME_PICKER_DIALOG_TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCommonTaskPresenter.onDetach();
    }
}
