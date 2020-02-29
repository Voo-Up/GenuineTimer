package com.example.examineetimer;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examineetimer.db.ExamineeTimerDbHandler;
import com.example.examineetimer.db.StudyTimeDO;
import com.example.examineetimer.utils.MyUtils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DiaryFragment extends Fragment {
    final private String TAG = "DiaryFragment";

    private RecyclerView mRecyclerView;
    private long mSelectedPickerTime;

    Button btnDatePicker;

    public DiaryFragment(){}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_diary, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        Calendar todayCalendar = Calendar.getInstance(Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayOnlyDateStr = sdf.format(todayCalendar.getTime());
        Log.i(TAG, "Today MilliSecond : " + todayCalendar.getTimeInMillis());
        todayCalendar.set(Calendar.HOUR, 0);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.SECOND, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);
        Log.i(TAG, "Today MilliSecond : " + todayCalendar.getTimeInMillis());
        builder.setSelection(todayCalendar.getTimeInMillis());
        mSelectedPickerTime = todayCalendar.getTimeInMillis();
        final MaterialDatePicker picker = builder.build();
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                mSelectedPickerTime = (long)selection;
                Log.i(TAG, "selected picker Time : " + mSelectedPickerTime);
                Date date = new Date(mSelectedPickerTime);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String todayOnlyDateStr = sdf.format(date);
                Log.i(TAG, "Select Only Date : " + todayOnlyDateStr);
                Log.i(TAG, "Select DateTime: " + date);
                loadAdapterList(mSelectedPickerTime);
                btnDatePicker.setText(todayOnlyDateStr);
            }
        });

        final FragmentManager fragmentManager = getFragmentManager();

        btnDatePicker = (Button)v.findViewById(R.id.btn_date_picker);
        btnDatePicker.setText(todayOnlyDateStr);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show(fragmentManager, picker.toString());
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        loadAdapterList(mSelectedPickerTime);
    }

    private void loadAdapterList(long selectedPickerTime) {
        ArrayList<DiaryListDO> arrayList = new ArrayList<>();
        ExamineeTimerDbHandler dbHandler = new ExamineeTimerDbHandler(getContext());
        Cursor cursor;

        //select DB
        Log.i(TAG, "Select table");
        if(selectedPickerTime == 0) {
            cursor = dbHandler.select(StudyTimeDO.StudyTimeEntry.TABLE_NAME);
        }
        else{
            cursor = dbHandler.selectByPickerDate(StudyTimeDO.StudyTimeEntry.TABLE_NAME, mSelectedPickerTime);
        }

        Log.i(TAG, "cursor Count : " + cursor.getCount());
        if (cursor.getCount() > 0) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(StudyTimeDO.StudyTimeEntry.COLUMN_NAME_id));

                Date date = new Date();
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(cursor.getString(cursor.getColumnIndex(StudyTimeDO.StudyTimeEntry.COLUMN_NAME_START_DATETIME)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int studySec = cursor.getInt(cursor.getColumnIndex(StudyTimeDO.StudyTimeEntry.COLUMN_NAME_STUDY_SEC));
                StudyTimeDO d = new StudyTimeDO(id, date, studySec);
                Log.i(TAG, d.getId() + ", " + d.getStartDateTime() + ", " + d.getStudySec());
                DiaryListDO dl = new DiaryListDO(MyUtils.convertSecToTimeFormatString(d.getStudySec()), MyUtils.convertDateTimeToFormatString(d.getStartDateTime()));
                arrayList.add(dl);
            }while(cursor.moveToNext());
        }

        CustomAdapter adapter = new CustomAdapter(arrayList);
        mRecyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        private ArrayList<DiaryListDO> mList;

        public class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView tvStudyTime;
            protected TextView tvStudyDate;

            public CustomViewHolder(View view) {
                super(view);
                this.tvStudyTime = (TextView) view.findViewById(R.id.mtrl_list_item_text);
                this.tvStudyDate = (TextView) view.findViewById(R.id.mtrl_list_item_secondary_text);
            }
        }

        public CustomAdapter(ArrayList<DiaryListDO> list) {
            this.mList = list;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.material_list_item_two_line, viewGroup, false);

            CustomViewHolder viewHolder = new CustomViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
            /*
            viewholder.tvStudyTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            viewholder.tvStudyDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

            viewholder.tvStudyTime.setGravity(Gravity.CENTER);
            viewholder.tvStudyDate.setGravity(Gravity.CENTER);
             */
            viewholder.tvStudyTime.setText(mList.get(position).getStudyTime());
            viewholder.tvStudyDate.setText(mList.get(position).getStudyDate());
        }

        @Override
        public int getItemCount() {
            return (null != mList ? mList.size() : 0);
        }

    }
}
