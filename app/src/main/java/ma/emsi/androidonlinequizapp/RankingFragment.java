package ma.emsi.androidonlinequizapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.FocusFinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ma.emsi.androidonlinequizapp.Common.Common;
import ma.emsi.androidonlinequizapp.Interface.ItemClickListener;
import ma.emsi.androidonlinequizapp.Interface.RankingCallBack;
import ma.emsi.androidonlinequizapp.Model.QuestionScore;
import ma.emsi.androidonlinequizapp.Model.Ranking;
import ma.emsi.androidonlinequizapp.ViewHolder.RankingViewHolder;


public class RankingFragment extends Fragment {
    View myFragment;
    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking,RankingViewHolder>adapter;

    FirebaseDatabase database;
    DatabaseReference questionScore,rankingTbl;

    int sum=0;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment=new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        questionScore=database.getReference("Question_Score");
        rankingTbl=database.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment=inflater.inflate(R.layout.fragment_ranking,container,false);

        rankingList=(RecyclerView)myFragment.findViewById(R.id.rankingList);
        layoutManager=new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);

        updateScore(Common.currentUser.getUserName(), new RankingCallBack<Ranking>() {
            @Override
            public void callback(Ranking ranking) {
                rankingTbl.child(ranking.getUserName())
                        .setValue(ranking);
               // showRanking();

            }
        });
        adapter=new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(
                Ranking.class,
                R.layout.layout_ranking,
                RankingViewHolder.class,
                rankingTbl.orderByChild("score")
        )
        {
            @Override
            protected void populateViewHolder(RankingViewHolder viewHolder, Ranking model, int position) {

                viewHolder.txt_name.setText(model.getUserName());
                viewHolder.txt_score.setText(String .valueOf(model.getScore()));
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);
        return myFragment;
    }




    private void updateScore(final String userName, final RankingCallBack<Ranking>callBack) {

        questionScore.orderByChild("user").equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren())
                        {
                            QuestionScore ques=data.getValue(QuestionScore.class);
                            sum+=Integer.parseInt(ques.getScore());
                        }

                        Ranking ranking=new Ranking(userName,sum);
                        callBack.callback(ranking);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
