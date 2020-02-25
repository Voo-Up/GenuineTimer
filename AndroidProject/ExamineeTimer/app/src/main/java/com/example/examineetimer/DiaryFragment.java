package com.example.examineetimer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DiaryFragment extends Fragment {

    final private String TAG = "DiaryFragment";
    public DiaryFragment(){}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_diary, container, false);

        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_main_list);

        ArrayList<DiaryListDO> arrayList = new ArrayList<>();
        //make dummy
        for(int i=0; i<30; i++){
            DiaryListDO d = new DiaryListDO("13:55:12", "2020-20-20");
            arrayList.add(d);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        CustomAdapter adapter = new CustomAdapter(arrayList);
        mRecyclerView.setAdapter(adapter);


        adapter.notifyDataSetChanged();

        return v;
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
