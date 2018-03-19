package com.im.smarto.ui.add_task.types.meeting;

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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeetingTaskFragment extends BaseFragment implements IMeetingTaskFragment,
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    @BindView(R.id.meeting_task_description)        MaterialEditText        mMeetingTaskDescription;
    @BindView(R.id.meeting_fab_choose_group)        FloatingActionButton    mChooseGroupButton;
    @BindView(R.id.meeting_fab_add_group)           FloatingActionButton    mAddGroupButton;
    @BindView(R.id.meeting_button_time)             FloatingActionButton    mTimeButton;
    @BindView(R.id.meeting_button_choose_contact)   FloatingActionButton    mChooseContactButton;
    @BindView(R.id.meeting_preview_group_name)      TextView                mGroupPreview;
    @BindView(R.id.meeting_preview_contact_name)    TextView                mContactPreview;
    @BindView(R.id.meeting_time_text_view)          TextView                mTimePreview;
    @BindView(R.id.meeting_data_text_view)          TextView                mDatePreview;
    @BindView(R.id.btn_add_meeting_task)            Button                  mAddMeetingTaskPreview;

    @Inject
    MeetingTaskPresenter<IMeetingTaskFragment> mMeetingTaskPresenter;

    private TimePickerDialog mTimePickerDialog;
    private DatePickerDialog mDatePickerDialog;

    public MeetingTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMeetingTaskPresenter.onAttach(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meeting_task, container, false);
        ButterKnife.bind(this, v);
        mMeetingTaskPresenter.onCreate();

        return v;
    }

    @OnClick({R.id.meeting_fab_choose_group, R.id.meeting_fab_add_group, R.id.btn_add_meeting_task,
            R.id.meeting_button_time, R.id.meeting_button_choose_contact})
    void onClick(View view){
        switch (view.getId()) {
            case R.id.meeting_fab_choose_group:
                mMeetingTaskPresenter.chooseGroupButtonClicked();
                break;
            case R.id.meeting_fab_add_group:
                mMeetingTaskPresenter.addGroupButtonClicked();
                break;
            case R.id.btn_add_meeting_task:
                mMeetingTaskPresenter.onAddMeetingTaskClicked(mMeetingTaskDescription.getText().toString());
                break;
            case R.id.meeting_button_time:
                mMeetingTaskPresenter.onTimeButtonClicked();
                break;
            case R.id.meeting_button_choose_contact:
                mMeetingTaskPresenter.chooseContactButtonClicked();
                break;
        }
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void showChooseContactDialog(String[] contactNames) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Choose contact");
        builder.setIcon(R.drawable.ic_playlist_add_check_black_24dp);
        builder.setSingleChoiceItems(contactNames, -1, (dialog, which) -> {
            mMeetingTaskPresenter.onSingleContactChoiceClicked(contactNames[which]);
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {

        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    @Override
    public void changeContactButtonBackground(boolean b) {
        int bgRes;
        if (b) bgRes = getResources().getColor(R.color.primary_normal);
        else bgRes = getResources().getColor(R.color.secondary_normal);
        mChooseContactButton.setBackgroundColor(bgRes);
    }

    @Override
    public void hideTargetContactPreview() {
        mContactPreview.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showTargetContactPreview(String contactName) {
        mContactPreview.setText(contactName);
        mContactPreview.setVisibility(View.VISIBLE);
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
        mTimeButton.setBackgroundColor(bgRes);
    }

    @Override
    public void showTargetDatePreview(String targetDate, String targetTime) {
        mDatePreview.setText(targetDate);
        mTimePreview.setText(targetTime);
        mDatePreview.setVisibility(View.VISIBLE);
        mTimePreview.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTargetDatePreview() {
        mDatePreview.setVisibility(View.INVISIBLE);
        mTimePreview.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showChooseGroupDialog(String[] groupNames) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Choose group");
        builder.setIcon(R.drawable.ic_playlist_add_check_black_24dp);
        builder.setSingleChoiceItems(groupNames, -1, (dialog, which) -> {
            mMeetingTaskPresenter.onSingleChoiceClicked(groupNames[which]);
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {

        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    @Override
    public void showGroupNamePreview(String groupName) {
        mGroupPreview.setText(groupName);
        mGroupPreview.setVisibility(View.VISIBLE);
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
                mMeetingTaskPresenter.onDialogAddGroupClicked(mAlertDialogEditText.getText()
                        .toString()));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mMeetingTaskPresenter.onDateSet(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        mMeetingTaskPresenter.onTimeSet(hourOfDay, minute);
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
        mMeetingTaskPresenter.onDetach();
    }

}
