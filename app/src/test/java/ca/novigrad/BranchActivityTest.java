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

        daySchedule = new DaySchedule("Not defined yet","Not defined yet");
        branchActivity.setDaySchedule(daySchedule,schedule);

        daySchedule = new DaySchedule("1 AM","2 PM");
        branchActivity.setDaySchedule(daySchedule,schedule);
        assertEquals("Closed",schedule.get(0));
        assertEquals("Not defined yet",schedule.get(1));
        assertEquals("1 AM to 2 PM",schedule.get(2));

    }

}