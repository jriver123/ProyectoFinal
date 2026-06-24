package uni.edu.ni.Battle.ioAPI.modelos;

public class ActualizarStatsRequest {
    private Integer level;
    private Integer coins;
    private Integer winned_matches;
    private Integer played_matches;
    private Integer storyProgress;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Integer getWinned_matches() {
        return winned_matches;
    }

    public void setWinned_matches(Integer winned_matches) {
        this.winned_matches = winned_matches;
    }

    public Integer getPlayed_matches() {
        return played_matches;
    }

    public void setPlayed_matches(Integer played_matches) {
        this.played_matches = played_matches;
    }

    public Integer getStoryProgress() {
        return storyProgress;
    }

    public void setStoryProgress(Integer storyProgress) {
        this.storyProgress = storyProgress;
    }
}

