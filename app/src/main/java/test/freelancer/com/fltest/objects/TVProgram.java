package test.freelancer.com.fltest.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android 18 on 6/22/2015.
 */
public class TVProgram {

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("end_time")
    private String endTime;
    private String channel;
    private String rating;
    private String name;
    //TODO: check other info


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TVProgram tvProgram = (TVProgram) o;

        if (name != null ? !name.equals(tvProgram.name) : tvProgram.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
