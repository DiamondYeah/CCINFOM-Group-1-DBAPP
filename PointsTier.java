public class PointsTier {
    private int tierId;
    private String tierName; 
    private int minPoints;
    private int maxPoints;

    public PointsTier(int tierId, String tierName, int minPoints, int maxPoints) {
        this.tierId = tierId;
        this.tierName = tierName;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
    }

    public int getTierId() { 
        return tierId; 
    }

    public void setTierId(int tierId) { 
        this.tierId = tierId; 
    }

    public String getTierName() { 
        return tierName; 
    }

    public void setTierName(String tierName) { 
        this.tierName = tierName; 
    }

    public int getMinPoints() { 
        return minPoints; 
    }
    
    public void setMinPoints(int minPoints) { 
        this.minPoints = minPoints; 
    }

    public int getMaxPoints() { 
        return maxPoints; 
    }

    public void setMaxPoints(int maxPoints) { 
        this.maxPoints = maxPoints; 
    }
    
    public boolean isWithinRange(int points) {
        return points >= minPoints && points <= maxPoints;
    }
}