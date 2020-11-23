package ca.novigrad;



import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class BranchActivityTest {
    private BranchActivity branchActivity;
    private ArrayList<String> schedule;
    private DaySchedule daySchedule;

    @Test
    public void setDayScheduleTest(){
        daySchedule = new DaySchedule("Closed","Closed");
        schedule = new ArrayList<>();
        branchActivity = new BranchActivity();
        branchActivity.setDaySchedule(daySchedule,schedule);
        assertEquals("Closed",schedule.get(0));

        schedule.clear();
        daySchedule = new DaySchedule("Not defined yet","Not defined yet");
        branchActivity = new BranchActivity();
        branchActivity.setDaySchedule(daySchedule,schedule);
        assertEquals("Not defined yet",schedule.get(0));

        schedule.clear();
        daySchedule = new DaySchedule("1 AM","2 PM");
        branchActivity = new BranchActivity();
        branchActivity.setDaySchedule(daySchedule,schedule);
        assertEquals("1 AM to 2 PM",schedule.get(0));

    }

}