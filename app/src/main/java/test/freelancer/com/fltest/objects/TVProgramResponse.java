package test.freelancer.com.fltest.objects;

import java.util.List;

/**
 * Created by Android 18 on 6/22/2015.
 */
public class TVProgramResponse {

    private List<TVProgram> results;
    private int count;

    public List<TVProgram> getResults() {
        return results;
    }

    public void setResults(List<TVProgram> results) {
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
