package com.devqt.cts_critical.thinking.skills.infor;

public class Score implements Comparable<Score> {

    //score date and number
    private String scoreDate;
    public int scoreNum;

    public Score(String date, int num){
        scoreDate = date;
        scoreNum = num;
    }

    //check this score against another
    public int compareTo(Score sc){
        //return 0 if equal
        //1 if passed greater than this
        //-1 if this greater than passed
        return sc.scoreNum>scoreNum? 1 : sc.scoreNum<scoreNum? -1 : 0;
    }

    //return score display text
    public String getScoreText(){
        return scoreDate+" - "+scoreNum;
    }

}
