package com.koiy.playercompanion;

public class SicBoResponse {
    private Datum[] data;
    private long count;

    public Datum[] getData() { return data; }
    public void setData(Datum[] value) { this.data = value; }

    public long getCount() { return count; }
    public void setCount(long value) { this.count = value; }

// Datum.java
    public class Datum {
        private long when;
        private long[] dice;
        private Lightning[] lightnings;
        private long totalWinners;
        private double totalPayout;

        public long getWhen() { return when; }
        public void setWhen(long value) { this.when = value; }

        public long[] getDice() { return dice; }
        public void setDice(long[] value) { this.dice = value; }

        public Lightning[] getLightnings() { return lightnings; }
        public void setLightnings(Lightning[] value) { this.lightnings = value; }

        public long getTotalWinners() { return totalWinners; }
        public void setTotalWinners(long value) { this.totalWinners = value; }

        public double getTotalPayout() { return totalPayout; }
        public void setTotalPayout(double value) { this.totalPayout = value; }
    }

// Lightning.java

    public class Lightning {
        private long number;
        private long multiplier;

        public long getNumber() { return number; }
        public void setNumber(long value) { this.number = value; }

        public long getMultiplier() { return multiplier; }
        public void setMultiplier(long value) { this.multiplier = value; }
    }


}
