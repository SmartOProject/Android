package com.android.smarto.architecture.add_task.types.common;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.smarto.Constants;
import com.android.smarto.R;
import com.android.smarto.architecture.base.BaseFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.melnykov.fab.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommonTaskFragment extends BaseFragment implements ICommonTaskFragment,
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

    @BindView(R.id.data_text_view)      TextView                mDateTextView;
    @BindView(R.id.time_text_view)      TextView                mTimeTextView;
    @BindView(R.id.is_data_need)        CheckBox                mIsDataNeed;
    @BindView(R.id.group_spinner)       MaterialSpinner         mTypeSpinner;
    @BindView(R.id.task_description)    EditText                mTaskDescription;
    @BindView(R.id.fab_add_group)       FloatingActionButton    mFloatingButton;

    @Inject
    CommonTaskPresenter<ICommonTaskFragment> mCommonTaskPresenter;

    private TimePickerDialog mTimePickerDialog;
    private DatePickerDialog mDatePickerDialog;

    public CommonTaskFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.btn_add_common_task)
    void onClick(){
        mCommonTaskPresenter.onAddCommonTaskClicked(mTaskDescription.getText().toString());
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

        View v = inflater.inflate(R.layout.fragment_common_task, container, false);
        ButterKnife.bind(this, v);

        setupCalendar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTypeSpinner.setBackground(mContext.getDrawable(R.drawable.spinner_background));
        }
        mTypeSpinner.setOnItemSelectedListener((view, position, id, item) ->
                mCommonTaskPresenter.onSpinnerItemClick(item.toString()));

        mCommonTaskPresenter.onSpinnerSetup();

        mIsDataNeed.setOnCheckedChangeListener((buttonView, isChecked) ->
                mCommonTaskPresenter.onCheckboxStateChanged(isChecked));

        mFloatingButton.setOnClickListener(e -> createDialog());

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
            mCommonTaskPresenter.onPositiveGroupDialogClick(edittext.getText().toString()));

        alert.setNegativeButton("Cancel", (dialog, whichButton) ->
            dialog.dismiss());

        alert.show();
    }

    @Override
    public void updateSpinnerItems(List<String> groupNames) {
        mTypeSpinner.setItems(groupNames);
    }

    @Override
    public void showDateView(String date) {
        mDateTextView.setVisibility(View.VISIBLE);
        mDateTextView.setText(date);
    }

    @Override
    public void showTimeView(String time) {
        mTimeTextView.setVisibility(View.VISIBLE);
        mTimeTextView.setText(time);
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCommonTaskPresenter.onAttach(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCommonTaskPresenter.onDetach();
    }

    private void setupCalendar() {
        Calendar calendar = Calendar.getInstance();
        mDatePickerDialog = DatePickerDialog.newInstance(
                this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        mDatePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);

        mTimePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        mTimePickerDialog.setVersion(com.wdullaer.materialdatetimepicker.time.TimePickerDialog.Version.VERSION_2);
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
    public void showNoGroupToAddTaskError() {
        Toast.makeText(mContext,
                R.string.add_common_task_group_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyTaskDescriptionError() {
        Toast.makeText(mContext,
                R.string.task_description_empty_error, Toast.LENGTH_SHORT).show();
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
    public void hideDateViews() {
        mDateTextView.setVisibility(View.INVISIBLE);
        mTimeTextView.setVisibility(View.INVISIBLE);
    }
}
