package ca.novigrad;

public class DaySchedule {
    private String startingTime;
    private String finishingTime;

    public DaySchedule(){
    }
    public DaySchedule(String startingTime,String finishingTime){
        this.startingTime = startingTime;
        this.finishingTime = finishingTime;
    }
    public String getStartingTime() {
        return startingTime;
    }
    public String getFinishingTime(){
        return finishingTime;
    }

    public void setFinishingTime(String finishingTime) {
        this.finishingTime = finishingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }
}
