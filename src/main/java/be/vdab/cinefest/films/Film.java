package be.vdab.cinefest.films;

import java.math.BigDecimal;

class Film {
    private final long id;
    private final String titel;
    private final int jaar;
    private final long vrijePlaatsen;
    private final BigDecimal aankoopprijs;

    public Film(long id, String titel, int jaar, long vrijePlaatsen, BigDecimal aankoopprijs) {
        this.id = id;
        this.titel = titel;
        this.jaar = jaar;
        this.vrijePlaatsen = vrijePlaatsen;
        this.aankoopprijs = aankoopprijs;
    }
    public long getId() {
        return id;
    }

    public String getTitel() {
        return titel;
    }

    public int getJaar() {
        return jaar;
    }

    public long getVrijePlaatsen() {
        return vrijePlaatsen;
    }

    public BigDecimal getAankoopprijs() {
        return aankoopprijs;
    }
}
