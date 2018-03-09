package vay.enterwind.auto2000samarinda.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.module.sales.CheckpointActivity;
import vay.enterwind.auto2000samarinda.module.sales.HomeActivity;
import vay.enterwind.auto2000samarinda.module.sales.PlanningActivity;
import vay.enterwind.auto2000samarinda.module.sales.ProfilActivity;
import vay.enterwind.auto2000samarinda.module.sales.TimelineActivity;
import vay.enterwind.auto2000samarinda.module.service.HistoryActivity;
import vay.enterwind.auto2000samarinda.module.supervisor.ReportActivity;
import vay.enterwind.auto2000samarinda.module.supervisor.TrackActivity;

/**
 * Created by novay on 04/03/18.
 */

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";

    /**
     * Setup Bottom Navigation View
     * @param bottomNavigationViewEx
     */
    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);
    }

    /**
     * Emable Navigation Setup
     * @param context
     * @param callingActivity
     * @param view
     */
    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.ic_home:
                        Intent intent1 = new Intent(context, HomeActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent1);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_plan:
                        Intent intent2  = new Intent(context, PlanningActivity.class);
                        context.startActivity(intent2);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        break;

                    case R.id.ic_check:
                        Intent intent3  = new Intent(context, CheckpointActivity.class);
                        context.startActivity(intent3);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        break;

                    case R.id.ic_profil:
                        Intent intent4 = new Intent(context, ProfilActivity.class);
                        context.startActivity(intent4);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        break;

                    case R.id.ic_timeline:
                        Intent intent5  = new Intent(context, TimelineActivity.class);
                        context.startActivity(intent5);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        break;

                    // Others
                    case R.id.ic_home_others:
                        Intent homeOthers = new Intent(context, vay.enterwind.auto2000samarinda.module.others.HomeActivity.class);
                        homeOthers.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(homeOthers);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_timeline_others:
                        Intent timelineOthers = new Intent(context, vay.enterwind.auto2000samarinda.module.others.TimelineActivity.class);
                        timelineOthers.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(timelineOthers);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_history_others:
                        Intent historyOthers = new Intent(context, vay.enterwind.auto2000samarinda.module.others.HistoryActivity.class);
                        historyOthers.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(historyOthers);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_profil_others:
                        Intent profileOthers = new Intent(context, vay.enterwind.auto2000samarinda.module.others.ProfilActivity.class);
                        profileOthers.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(profileOthers);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    // General Repair & Body Painting
                    case R.id.ic_home_service:
                        Intent homeService = new Intent(context, vay.enterwind.auto2000samarinda.module.service.HomeActivity.class);
                        homeService.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(homeService);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_timeline_service:
                        Intent timelineService = new Intent(context, vay.enterwind.auto2000samarinda.module.service.TimelineActivity.class);
                        timelineService.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(timelineService);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_history_service:
                        Intent historyService = new Intent(context, HistoryActivity.class);
                        historyService.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(historyService);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_profil_service:
                        Intent profileService = new Intent(context, vay.enterwind.auto2000samarinda.module.service.ProfilActivity.class);
                        profileService.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(profileService);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    // Supervisor
                    case R.id.ic_home_svp:
                        Intent homeSvp = new Intent(context, vay.enterwind.auto2000samarinda.module.supervisor.HomeActivity.class);
                        homeSvp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(homeSvp);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_timeline_svp:
                        Intent timelineSvp = new Intent(context, vay.enterwind.auto2000samarinda.module.supervisor.TimelineActivity.class);
                        timelineSvp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(timelineSvp);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_profil_svp:
                        Intent profilSvp = new Intent(context, vay.enterwind.auto2000samarinda.module.supervisor.ProfilActivity.class);
                        profilSvp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(profilSvp);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_track_svp:
                        Intent trackSvp = new Intent(context, TrackActivity.class);
                        trackSvp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(trackSvp);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.ic_report_svp:
                        Intent reportSpv = new Intent(context, ReportActivity.class);
                        reportSpv.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(reportSpv);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                }
                return false;
            }
        });
    }

}
